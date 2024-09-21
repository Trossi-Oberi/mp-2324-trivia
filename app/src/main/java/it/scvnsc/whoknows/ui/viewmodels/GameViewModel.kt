package it.scvnsc.whoknows.ui.viewmodels

import android.app.Application
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import it.scvnsc.whoknows.R
import it.scvnsc.whoknows.data.db.DatabaseWK
import it.scvnsc.whoknows.data.model.Game
import it.scvnsc.whoknows.data.model.Question
import it.scvnsc.whoknows.repository.GameQuestionRepository
import it.scvnsc.whoknows.repository.GameRepository
import it.scvnsc.whoknows.repository.QuestionRepository
import it.scvnsc.whoknows.utils.CategoryManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


class GameViewModel(application: Application) : AndroidViewModel(application) {

    //TODO: Cambiare la logica di come vengono passati i parametri difficulty e category alla chiamata API
    // (Ora difficulty e category vengono impostati nella scheda settings)

    private val DEFAULT_CATEGORY = "mixed"
    private val DEFAULT_DIFFICULTY = "mixed"

    //Difficolta' selezionata dall'utente
    private val _selectedDifficulty = MutableLiveData(DEFAULT_DIFFICULTY)
    val selectedDifficulty: LiveData<String> get() = _selectedDifficulty

    //Categoria selezionata dall'utente
    private val _selectedCategory = MutableLiveData(DEFAULT_CATEGORY)
    val selectedCategory: LiveData<String> get() = _selectedCategory

    fun getCategories(): List<String> {
        val availableCategories = CategoryManager.categories.keys.toMutableList()
        availableCategories.add("mixed")
        availableCategories.toList()
        return availableCategories
    }

    //Numero arbitrario (costante) di domande da prendere dall'API
    private val AMOUNT = 1

    //Domande poste all'utente
    private var askedQuestions = mutableListOf<Question>()

    //Domanda posta all'utente e possibili risposte (in ordine casuale, altrimenti la risposta corretta sarebbe sempre la prima)
    private val _questionForUser = MutableLiveData<Question>()
    val questionForUser: LiveData<Question> get() = _questionForUser
    private val _shuffledAnswers = MutableLiveData<List<String>>()
    val shuffledAnswers: LiveData<List<String>> get() = _shuffledAnswers

    //Controlla se si sta giocando
    private val _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean> get() = _isPlaying

    //Controlla se la partita e' finita (diverso da isPlaying perche' se non sta giocando la partita potrebbe non essere mai iniziata)
    private val _isGameFinished = MutableLiveData(false)
    val isGameFinished: LiveData<Boolean> get() = _isGameFinished

    //Memorizza la risposta clickata dall'utente
    private val _userAnswer = MutableLiveData<String>()
    val userAnswer: LiveData<String> get() = _userAnswer

    //MediaPlayer per riproduzione suono in base alla risposta selezionata
    private var mediaPlayer: MediaPlayer? = null

    //Controlla se il nuovo punteggio e' un record
    private val _isRecord = MutableLiveData(false)
    val isRecord: LiveData<Boolean> get() = _isRecord

    //Timer di gioco
    private var gameTimer = 0
    private val _elapsedTime = MutableLiveData<String>()
    val elapsedTime: LiveData<String> = _elapsedTime

    //Punteggio della partita
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> = _score

    //Da cancellare (credo)
    private val _gameError = MutableLiveData<String>()
    val gameError: LiveData<String> get() = _gameError

    //Repositories
    private val questionRepository: QuestionRepository
    private val gameRepository: GameRepository
    private val gameQuestionRepository: GameQuestionRepository

    init {
        val questionDAO = DatabaseWK.getInstance(application).questionDAO()
        val gameDAO = DatabaseWK.getInstance(application).gameDAO()
        val gameQuestionDAO = DatabaseWK.getInstance(application).gameQuestionDAO()
        questionRepository = QuestionRepository(questionDAO)
        gameRepository = GameRepository(gameDAO)
        gameQuestionRepository = GameQuestionRepository(gameQuestionDAO)
        viewModelScope.launch {
            questionRepository.setupInteractionWithAPI()
        }
    }

    fun setDifficulty(difficulty: String) {
        _selectedDifficulty.postValue(difficulty)
    }

    fun setCategory(categoryName: String) {
        _selectedCategory.postValue(categoryName)
    }

    fun onStartClicked() {
        viewModelScope.launch {
            try {
                startGame()
                _isPlaying.postValue(true)
            } catch (e: Exception) {
                _gameError.postValue("Errore durante l'avvio del gioco: ${e.message}")
            }
        }
    }

    fun onAnswerClicked(givenAnswer: String) {
        viewModelScope.launch {
            _userAnswer.postValue(givenAnswer)
            evaluateAnswer(givenAnswer)
            Log.d("Debug", "On answer clicked fine esecuzione")
        }
    }

    private suspend fun evaluateAnswer(givenAnswer: String) {
        //Risposta corretta -> Fetch della prossima domanda, aggiornamento dello score
        if (givenAnswer == questionForUser.value?.correct_answer) {
            updateScore()
            playSound(R.raw.correct_answer)
            _questionForUser.value = nextQuestion()
        } else {
            //Risposta sbagliata -> Fine partita, salvataggio del game nel db, stop del timer, mostrare new record notification se nuovo record
            stopTimer()
            playSound(R.raw.wrong_answer)
            //Salvataggio game nel DB
            Log.d("Debug", "Game ended with score: ${_score.value}")
            val playedGame = Game(
                _score.value,
                _selectedDifficulty.value!!,
                _selectedCategory.value!!,
                _elapsedTime.value,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            )
            Log.d("Debug", "New game instance created with ID: ${playedGame.id}")

            //Controllo se il nuovo punteggio e' un record e aggiorno isRecord di conseguenza
            checkGameRecord(playedGame)
            //TODO: Osservare isRecord per mostrare la notifica del record

            //In ogni caso salvo la partita
            saveGameAndQuestions(playedGame, askedQuestions)
            _isPlaying.postValue(false)
        }
    }

    private fun playSound(answerSound: Int) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(getApplication(), answerSound)
        mediaPlayer?.start()
        mediaPlayer?.setOnCompletionListener {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    private suspend fun saveGameAndQuestions(
        playedGame: Game,
        askedQuestions: MutableList<Question>
    ) {
        val newGameID = gameRepository.saveGame(playedGame)
        playedGame.id = newGameID
        Log.d("Debug", "Game saved with ID: ${playedGame.id}")
        gameQuestionRepository.saveGameAndQuestions(playedGame, askedQuestions)
    }

    private suspend fun checkGameRecord(playedGame: Game) {
        val maxScore = gameRepository.getMaxScore() ?: 0
        Log.d("Debug", "Max score: $maxScore")
        Log.d("Debug", "Played game score: ${playedGame.score}")
        val check = playedGame.score!! > maxScore
        Log.d("Debug", "Check: $check")
        _isRecord.value = check
        Log.d("Debug", "Record: ${_isRecord.value}")
    }

    private suspend fun startGame() {
        //Resetto il timer di gioco e il token per le domande
        _elapsedTime.postValue("")
        questionRepository.resetSessionToken()

        //Resetto la lista delle domande poste all'utente
        askedQuestions.clear()

        //Imposto punteggio a 0
        _score.postValue(0)

        //Fetcho la nuova domanda
        _questionForUser.postValue(nextQuestion())

        //Start timer della partita
        startTimer()
        Log.d("Debug", "Question for user: ${_questionForUser.value}")
    }

    //Funzione che converte la stringa "mixed" in "" in modo da far funzionare la richiesta all'API
    private fun convertMixed(text: String): String {
        if (text != "mixed") {
            return text
        }
        return ""
    }

    //TODO: L'API prende una domanda alla volta
    //Funzione che ottiene la nuova domanda da presentare all'utente (l'API fornisce le domande in ordine casuale)
    private suspend fun nextQuestion(): Question {
        val newQuestion = questionRepository.retrieveNewQuestion(
            AMOUNT,
            convertMixed(_selectedCategory.value.toString()),
            convertMixed(_selectedDifficulty.value.toString().lowercase())
        )
        askedQuestions.add(newQuestion)
        _shuffledAnswers.postValue(shuffleAnswers(newQuestion))
        return newQuestion
    }

    //Funzione che mescola le possibili risposte alla domanda (altrimenti la risposta corretta sarebbe sempre la prima)
    private fun shuffleAnswers(question: Question): MutableList<String> {
        val corrAnswer = question.correct_answer
        val incAnswers = question.incorrect_answers.toMutableList()
        incAnswers.add(corrAnswer)
        incAnswers.shuffle()
        return incAnswers
    }

    //Inizializza il timer di gioco
    private fun startTimer() {
        _isGameFinished.value =
            false //per forza cosi', se utilizzo postValue non si aggiorna il timer
        viewModelScope.launch {
            gameTimer = 0
            while (_isGameFinished.value == false) {
                val formattedTime =
                    String.format(Locale.getDefault(), "%02d:%02d", gameTimer / 60, gameTimer % 60)
                _elapsedTime.postValue(formattedTime)
                delay(1000L)
                gameTimer++
            }
        }
    }

    //Stoppa il timer di gioco
    private fun stopTimer() {
        _isGameFinished.postValue(true)
    }

    //Aggiorna il punteggio
    private fun updateScore() {
        //TODO: Idea: fare che easy vale 1 punto, medium vale 2 punti, hard vale 3 punti
        _score.postValue(_score.value!! + 1)
    }
}