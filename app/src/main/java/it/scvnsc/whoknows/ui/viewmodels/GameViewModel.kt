package it.scvnsc.whoknows.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import it.scvnsc.whoknows.data.db.DatabaseWK
import it.scvnsc.whoknows.data.model.Game
import it.scvnsc.whoknows.data.model.Question
import it.scvnsc.whoknows.repository.GameRepository
import it.scvnsc.whoknows.repository.QuestionRepository
import it.scvnsc.whoknows.utils.CategoryManager
import it.scvnsc.whoknows.utils.DifficultyType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.random.Random


class GameViewModel(application: Application) : AndroidViewModel(application) {

    //TODO: Cambiare la logica di come vengono passati i parametri difficulty e category alla chiamata API
    // (Ora difficulty e category vengono impostati nella scheda settings)

    private val _selectedDifficulty = MutableLiveData(DifficultyType.MIXED) //valore di default
    private val _showDifficultySelection = MutableLiveData(false)
    val selectedDifficulty: LiveData<DifficultyType> get() = _selectedDifficulty
    //val showDifficultySelection: LiveData<Boolean> get() = _showDifficultySelection

    private val _selectedCategory = MutableLiveData("") //valore di default
    val selectedCategory: LiveData<String> get() = _selectedCategory

    fun getCategories(): List<String> {
        return CategoryManager.categories.keys.toList()
    }

    fun setCategory(categoryName: String) {
        _selectedCategory.value = categoryName
    }

    /* //TODO: da controllare
    //Difficolta' e categoria impostate dall'utente
    val chosenDifficulty: String = DifficultyType.MIXED.toString() //Default MIXED
    val chosenCategory: String = "" //Default "" -> non scelgo la categoria = Mixed
*/

    private val START_AMOUNT =
        20 //Numero arbitrario (costante) di domande da prendere dall'API alla prima fetch
    private val SMALL_AMOUNT =
        10 //Numero arbitrario di domande da prendere dall'API una volta esaurite le prime 20

    private var freshQuestions = mutableListOf<Question>() //Domande prese dall'API
    private var askedQuestions = mutableListOf<Question>() //Domande poste all'utente

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

    //Controlla se il nuovo punteggio e' un record
    private val _isRecord = MutableLiveData(false)
    val isRecord : LiveData<Boolean> get() = _isRecord

    //Timer di gioco
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

    init {
        val questionDAO = DatabaseWK.getInstance(application).questionDAO()
        val gameDAO = DatabaseWK.getInstance(application).gameDAO()
        questionRepository = QuestionRepository(questionDAO)
        gameRepository = GameRepository(gameDAO)
        viewModelScope.launch {
            questionRepository.setupInteractionWithAPI()
        }
    }

    fun setDifficulty(difficulty: DifficultyType) {
        _selectedDifficulty.postValue(difficulty)
    }

    fun setShowDifficultySelection(show: Boolean) {
        _showDifficultySelection.value = show
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
            checkAnswerCorrectness(givenAnswer)
        }
    }

    private fun checkNumberOfAvailableQuestions(): Int {
        return freshQuestions.size
    }

    private suspend fun checkAnswerCorrectness(givenAnswer: String) {
        if (givenAnswer == questionForUser.value?.correct_answer) {
            //Risposta corretta -> Fetch della prossima domanda, aggiornamento dello score
            updateScore()
            if (checkNumberOfAvailableQuestions() <= 5) {
                //Se ci sono 5 o meno domande disponibili in memoria faccio una nuova chiamata API per prenderne altre 10
                //TODO: Fare attenzione al cambio categoria/difficolta' da parte dell'utente, se una delle due cambia allora vanno pulite tutte le domande presenti in memoria
                val newQuestions = questionRepository.retrieveQuestions(
                    SMALL_AMOUNT,
                    _selectedCategory,
                    _selectedDifficulty.value.toString().lowercase()
                )
                freshQuestions.addAll(newQuestions)
            }
            _questionForUser.postValue(nextQuestion())
        } else {
            //Risposta sbagliata -> Fine partita, salvataggio del game nel db, stop del timer, mostrare new record notification se nuovo record
            stopTimer()
            freshQuestions = emptyList<Question>().toMutableList()

            //Salvataggio game nel DB
            Log.d("Debug","Game ended with score: ${_score.value}")
            val playedGame = Game(_score.value, chosenDifficulty, chosenCategory, _elapsedTime.value, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))

            //Controllo se il nuovo punteggio e' un record e aggiorno isRecord di conseguenza
            checkGameRecord(playedGame)
            //TODO: Osservare isRecord per mostrare la notifica del record

            //In ogni caso salvo la partita
            gameRepository.saveGame(playedGame)
            _isPlaying.postValue(false)
        }
        Log.d("Debug", "Answer clicked")
    }

    private suspend fun checkGameRecord(playedGame: Game){
        val maxScore = gameRepository.getMaxScore() ?: 0
        Log.d("Debug", "Max score: $maxScore")
        Log.d("Debug", "Played game score: ${playedGame.score}")
        val check = playedGame.score!! > maxScore
        Log.d("Debug", "Check: $check")
        _isRecord.value = check
        Log.d("Debug", "Record: ${_isRecord.value}")
    }

    private suspend fun startGame() {
        //TODO: category e difficulty vanno passati come parametro dinamicamente
        //Ottimizzazione: se ci sono ancora domanda che non sono state poste all'utente non faccio una chiamata API
        if (freshQuestions.size == 0){
            questionRepository.resetSessionToken()
            freshQuestions = questionRepository.retrieveQuestions(START_AMOUNT, _selectedCategory.value!!, _selectedDifficulty.value!!.toString().lowercase())
        }
        Log.d("Debug", "Fresh Questions: ${freshQuestions[0].question}")
        Log.d("Debug", "Fresh Questions: ${freshQuestions[1].question}")
        _questionForUser.postValue(nextQuestion())

        //Imposto punteggio a 0
        _score.postValue(0)

        //Start timer della partita
        startTimer()

        Log.d("Debug", "Question for user: ${_questionForUser.value}")
    }

    //Funzione che ottiene la nuova domanda da presentare all'utente
    private fun nextQuestion(): Question {
        val r = Random.nextInt(freshQuestions.size)
        val nextQ = freshQuestions[r]
        freshQuestions.removeAt(r)
        askedQuestions.add(nextQ)
        _shuffledAnswers.postValue(shuffleAnswers(nextQ))
        return nextQ
    }

    //Funzione che mescola le possibili risposte alla domanda (altrimenti la risposta corretta sarebbe sempre la prima)
    private fun shuffleAnswers(question: Question): MutableList<String> {
        val corrAnswer = question.correct_answer
        val incAnswers = question.incorrect_answers.toMutableList()
        val possibleAnswers = incAnswers
        possibleAnswers.add(corrAnswer)
        possibleAnswers.shuffle()
        return possibleAnswers
    }

    //Inizializza il timer di gioco
    private fun startTimer() {
        _isGameFinished.postValue(false)
        viewModelScope.launch {
            var seconds = 0
            while (_isGameFinished.value == false) {
                val formattedTime =
                    String.format(Locale.getDefault(), "%02d:%02d", seconds / 60, seconds % 60)
                _elapsedTime.postValue(formattedTime)
                delay(1000L)
                seconds++
            }
        }
    }

    //Stoppa il timer di gioco
    private fun stopTimer() {
        _isGameFinished.postValue(true)
    }

    //Aggiorna il punteggio
    private fun updateScore() {
        _score.postValue(_score.value!! + 1)
    }
}