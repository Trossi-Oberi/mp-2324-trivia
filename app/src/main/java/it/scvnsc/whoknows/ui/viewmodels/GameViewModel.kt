package it.scvnsc.whoknows.ui.viewmodels

import android.app.Application
import android.content.Context
import android.database.sqlite.SQLiteException
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
import it.scvnsc.whoknows.data.network.NetworkResult
import it.scvnsc.whoknows.repository.GameQuestionRepository
import it.scvnsc.whoknows.repository.GameRepository
import it.scvnsc.whoknows.repository.QuestionRepository
import it.scvnsc.whoknows.services.NetworkMonitorService
import it.scvnsc.whoknows.utils.CategoryManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val DEFAULT_CATEGORY = "Mixed"
    private val DEFAULT_DIFFICULTY = "Mixed"
    private val SCORE_EASY_DIFFICULTY = 1
    private val SCORE_MEDIUM_DIFFICULTY = 2
    private val SCORE_HARD_DIFFICULTY = 3
    private val WAIT_TIME = 500L
    private val STARTING_LIVES = 3

    //Difficolta' selezionata dall'utente
    private val _selectedDifficulty = MutableLiveData(DEFAULT_DIFFICULTY)
    val selectedDifficulty: LiveData<String> get() = _selectedDifficulty

    //Categoria selezionata dall'utente
    private val _selectedCategory = MutableLiveData(DEFAULT_CATEGORY)
    val selectedCategory: LiveData<String> get() = _selectedCategory

    fun getCategories(): List<String> {
        val availableCategories = mutableListOf("Mixed")
        availableCategories.addAll(CategoryManager.categories.keys)
        availableCategories.toList()
        return availableCategories
    }

    //Domande poste all'utente
    private var askedQuestions = mutableListOf<Question>()

    //Domanda posta all'utente e possibili risposte (in ordine casuale, altrimenti la risposta corretta sarebbe sempre la prima)
    private val _questionForUser = MutableLiveData<Question?>()
    val questionForUser: LiveData<Question?> get() = _questionForUser
    private val _shuffledAnswers = MutableLiveData<List<String>>()
    val shuffledAnswers: LiveData<List<String>> get() = _shuffledAnswers

    //Controlla se si sta giocando
    private val _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean> get() = _isPlaying

    //Controlla se la partita e' finita (diverso da isPlaying perche' se non sta giocando la partita potrebbe non essere mai iniziata)
    private val _isGameOver = MutableLiveData(false)
    val isGameOver: LiveData<Boolean> get() = _isGameOver

    //Memorizza la risposta clickata dall'utente (serve per aggiornare l'interfaccia GameView con i colori verde se corretta e rosso se errata)
    private val _userAnswer = MutableLiveData<String>()
    val userAnswer: LiveData<String> get() = _userAnswer

    //Fa in modo che solo una risposta sia selezionabile
    private val _isAnswerSelected = MutableLiveData(false)
    val isAnswerSelected: LiveData<Boolean> get() = _isAnswerSelected

    //MediaPlayer per riproduzione suono in base alla risposta selezionata
    private var mediaPlayer: MediaPlayer? = null

    //Controlla se il nuovo punteggio e' un record
    private val _isRecord = MutableLiveData(false)
    val isRecord: LiveData<Boolean> get() = _isRecord

    //Timer di gioco
    private var gameTimer = 0
    private val _elapsedTime = MutableLiveData<String>()
    val elapsedTime: LiveData<String> = _elapsedTime
    private val _isGameTimerInterrupted = MutableLiveData<Boolean?>()
    val isGameTimerInterrupted: LiveData<Boolean?> = _isGameTimerInterrupted

    private val _lastGame = MutableLiveData<Game>()
    val lastGame: LiveData<Game> get() = _lastGame

    //Timer per nuova richiesta API
    private var apiTimerJob: Job? = null

    //Punteggio della partita
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> = _score

    //Vite dell'utente
    private val _lives = MutableLiveData<Int>()
    val lives: LiveData<Int> = _lives

    private val _isApiSetupComplete = MutableLiveData(false)
    val isApiSetupComplete: LiveData<Boolean> get() = _isApiSetupComplete

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
    }

    fun setDifficulty(difficulty: String) {
        _selectedDifficulty.postValue(difficulty)
    }

    fun setCategory(categoryName: String) {
        _selectedCategory.postValue(categoryName)
    }

    fun setIsPlaying(isPlaying: Boolean) {
        _isPlaying.postValue(isPlaying)
    }

    fun setGameOver(isGameOver: Boolean) {
        _isGameOver.value = isGameOver
    }

    fun onStartClicked() {
        viewModelScope.launch {
            _isGameOver.value = false
            _isGameTimerInterrupted.value = true
            startGame()
            _isPlaying.value = true
            _isGameTimerInterrupted.value = false
        }
    }

    fun onAnswerClicked(givenAnswer: String) {
        _isAnswerSelected.value = true
        viewModelScope.launch {
            _userAnswer.postValue(givenAnswer)
            evaluateAnswer(givenAnswer)
        }
    }

    private suspend fun evaluateAnswer(givenAnswer: String) {
        //Risposta corretta -> Fetch della prossima domanda, aggiornamento dello score
        if (givenAnswer == questionForUser.value?.correct_answer) {
            //Aggiorno il punteggio e riproduco il suono di risposta corretta
            updateScore()
            playSound(R.raw.correct_answer)
            delay(WAIT_TIME)

            //imposto la risposta data dall'utente nella domanda corrente e aggiorno il database
            _questionForUser.value?.givenAnswer = givenAnswer

            questionRepository.updateLastQuestion(
                _questionForUser.value!!.id,
                givenAnswer
            )
            fetchNewQuestion()

        } else {
            //Risposta sbagliata -> -1 vita, prossima domanda
            //Risposta sbagliata &&  Fine partita, salvataggio del game nel db, stop del timer, mostrare new record notification se nuovo record
            _lives.value = _lives.value!! - 1
            Log.d("Debug", "Current lives: ${_lives.value}")
            playSound(R.raw.wrong_answer)
            delay(WAIT_TIME)

            //imposto la risposta data dall'utente nella domanda corrente e aggiorno il database
            _questionForUser.value?.givenAnswer = givenAnswer
            questionRepository.updateLastQuestion(_questionForUser.value!!.id, givenAnswer)

            if (_lives.value == 0){
                //Salvataggio game nel DB
                Log.d("Debug", "Game ended with score: ${_score.value}")
                val playedGame = Game(
                    _score.value!!,
                    _selectedDifficulty.value!!,
                    _selectedCategory.value!!,
                    _elapsedTime.value!!,
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
                )
                //Controllo se il nuovo punteggio e' un record e aggiorno isRecord di conseguenza
                checkGameRecord(playedGame)

                //In ogni caso salvo la partita
                saveGameAndQuestions(playedGame, askedQuestions)

                //Devo settare isPlaying ad off solamente se l'utente clicka main menu o game menu
                //Altrimenti isPlaying rimane true e isGameOver torna false, per iniziare una nuova partita
                _isGameOver.postValue(true)

                //salvo nella variabile lastGame l'ultima partita salvata
                _lastGame.value = gameRepository.getLastGame()
            }else{
                fetchNewQuestion()
            }
        }
        _isAnswerSelected.value = false
    }

    private suspend fun fetchNewQuestion() {

        //Fetcho la prossima domanda, se la richiesta API fallisce aspetto che torni la connessione e riproviamo
        _questionForUser.value = nextQuestion()
        if (_questionForUser.value == null) {
            while (true) {
                if (NetworkMonitorService.isOffline.value == false) {
                    _questionForUser.value = nextQuestion()
                    break
                }
            }
        }
        //Resetto la risposta data dall'utente e riattivo il timer per continuare a giocare
        _userAnswer.value = ""
        _isGameTimerInterrupted.value = false
    }

    private fun playSound(answerSound: Int) {
        val sharedPreferences = getApplication<Application>().getSharedPreferences(
            "app_prefs",
            Context.MODE_PRIVATE
        )
        val isSoundEnabled = sharedPreferences.getBoolean("isSoundEnabled", false)
        Log.d("Debug", "isSoundEnabled: $isSoundEnabled")
        if (!isSoundEnabled) return

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
        val isNewRecord = playedGame.score!! > maxScore

        if (maxScore == 0) {
            Log.d("Debug", "New record: ${playedGame.score}")
        } else {
            when (isNewRecord) {
                true -> Log.d("Debug", "New record: ${playedGame.score}")
                false -> Log.d("Debug", "No new record")
            }
        }

        _isRecord.value = isNewRecord
    }

    private suspend fun startGame() {
        //Resetto il timer di gioco e il token per le domande
        _elapsedTime.postValue("")
        questionRepository.resetSessionToken()

        //Resetto la lista delle domande poste all'utente
        askedQuestions.clear()

        //Imposto punteggio a 0
        _score.postValue(0)

        //Imposto le vite al valore di partenza
        _lives.postValue(STARTING_LIVES)

        //Fetcho la nuova domanda
        _questionForUser.value = nextQuestion()
        //Se la richiesta API fallisce aspetto che torni la connessione e riproviamo
        if (_questionForUser.value == null) {
            while (true) {
                if (NetworkMonitorService.isOffline.value == false) {
                    _questionForUser.value = nextQuestion()
                    break
                }
            }
        }
        Log.d("Debug", "Current question: ${_questionForUser.value}")
        //Start timer della partita
        startTimer()
    }

    //Funzione che converte la stringa "mixed" in "" in modo da far funzionare la richiesta all'API
    private fun convertMixed(text: String): String {
        if (text != "Mixed") {
            return text
        }
        return ""
    }

    //Funzione che ottiene la nuova domanda da presentare all'utente (l'API fornisce le domande in ordine casuale)
    private suspend fun nextQuestion(): Question? {

        if (apiTimerJob != null) {
            Log.d("Debug", "API timer non è null")
            _isGameTimerInterrupted.value = true
            apiTimerJob?.join()
            Log.d("Debug", "API timer joinato")
        }

        val newQuestion: Question
        try {
            when (val result = questionRepository.retrieveNewQuestion(
                convertMixed(_selectedCategory.value.toString()),
                convertMixed(_selectedDifficulty.value.toString()).lowercase()
            )) {
                is NetworkResult.Success -> {
                    //Log.d("Debug", "New question retrieved: ${result.data}")
                    newQuestion = result.data
                }

                is NetworkResult.Error -> {
                    Log.e("WhoKnows", "Error: ${result.exception.message}")
                    return null
                }
            }
        } catch (e: SQLiteException) {
            Log.e("Database", "Error retrieving new question: ${e.message}")
            return null
        }

        apiCountdownTimer()
        askedQuestions.add(newQuestion)
        _shuffledAnswers.value = shuffleAnswers(newQuestion)
        _isGameTimerInterrupted.value = false
        return newQuestion
    }

    //Funzione che viene chiamata quando viene fatta una nuova richiesta API, setta il booleano a false e dopo 5 secondi lo setta di nuovo a true
    private fun apiCountdownTimer() {
        apiTimerJob = viewModelScope.launch {
            delay(5200L)
        }
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
        _isGameOver.value = false //per forza cosi', se utilizzo postValue non si aggiorna il timer
        viewModelScope.launch {
            gameTimer = 0
            while (_isGameOver.value == false) {
                if (_isGameTimerInterrupted.value == false) {
                    val formattedTime =
                        String.format(
                            Locale.getDefault(),
                            "%02d:%02d",
                            gameTimer / 60,
                            gameTimer % 60
                        )
                    _elapsedTime.postValue(formattedTime)
                    delay(1000L)
                    gameTimer++
                } else {
                    delay(1000)
                }
            }
        }
    }

    //Aggiorna il punteggio
    private fun updateScore() {

        val increment = when (_questionForUser.value?.difficulty) {
            "easy" -> SCORE_EASY_DIFFICULTY
            "medium" -> SCORE_MEDIUM_DIFFICULTY
            "hard" -> SCORE_HARD_DIFFICULTY
            else -> 1
        }
        _score.postValue(_score.value!! + increment)
    }

    fun clearUserAnswer() {
        _userAnswer.value = ""
    }

    fun onQuitGameClicked() {
        viewModelScope.launch {
            quitGame()
        }
    }

    private suspend fun quitGame() {
        //imposto la risposta come non data
        _questionForUser.value?.givenAnswer = ""

        //Salvataggio game nel DB
        Log.d("Debug", "Game ended with score: ${_score.value}")
        val playedGame = Game(
            _score.value!!,
            _selectedDifficulty.value!!,
            _selectedCategory.value!!,
            _elapsedTime.value!!,
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
        )

        //Controllo se il nuovo punteggio e' un record e aggiorno isRecord di conseguenza
        checkGameRecord(playedGame)

        //In ogni caso salvo la partita
        saveGameAndQuestions(playedGame, askedQuestions)

        askedQuestions.clear()

        //salvo nella variabile lastGame l'ultima partita salvata
        _lastGame.value = gameRepository.getLastGame()

        //resetto il game timer
        _isGameTimerInterrupted.value = false

        //imposto gameOver a true in modo da far comparire la schermata di gameOver
        _isGameOver.value = true

    }

    fun pauseTimer() {
        _isGameTimerInterrupted.value = true
        Log.d("Debug", "Timer paused")
        // Logica per fermare il timer
    }

    fun resumeTimer() {
        _isGameTimerInterrupted.postValue(false)
        //_isGameTimerInterrupted.value = false
        Log.d("Debug", "Timer resumed")
        // Logica per riprendere il timer
    }

    fun setupAPI() {
        viewModelScope.launch {
            //provo a fare il setup dell'api all'avvio del programma, se fallisce loggo un errore
            try {
                questionRepository.setupInteractionWithAPI()
                _isApiSetupComplete.value = true
            } catch (e: Exception) {
                Log.e("Error", "API error: ${e.message}")
                throw e
            }
        }
    }

}