package it.scvnsc.whoknows.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import it.scvnsc.whoknows.data.db.DatabaseWK
import it.scvnsc.whoknows.data.model.Question
import it.scvnsc.whoknows.repository.QuestionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.random.Random


class GameViewModel(application: Application) : AndroidViewModel(application) {

    //TODO: Cambiare la logica di come vengono passati i parametri difficulty e category alla chiamata API
    // (Ora difficulty e category vengono impostati nella scheda settings)

    private val _selectedDifficulty = MutableLiveData("MIXED") //valore di default
    private val _showDifficultySelection = MutableLiveData(false)
    val selectedDifficulty: LiveData<String> get() = _selectedDifficulty
    val showDifficultySelection: LiveData<Boolean> get() = _showDifficultySelection

    private val START_AMOUNT = 20 //Numero arbitrario (costante) di domande da prendere dall'API alla prima fetch
    private val SMALL_AMOUNT = 10 //Numero arbitrario di domande da prendere dall'API una volta esaurite le prime 20

    private var freshQuestions = mutableListOf<Question>() //Domande prese dall'API
    private var askedQuestions = mutableListOf<Question>() //Domande poste all'utente

    //Domanda posta all'utente e possibili risposte (in ordine casuale, altrimenti la risposta corretta sarebbe sempre la prima)
    private val _questionForUser = MutableLiveData<Question>()
    val questionForUser: LiveData<Question> get() = _questionForUser
    private val _shuffledAnswers = MutableLiveData<List<String>>()
    val shuffledAnswers : LiveData<List<String>> get() = _shuffledAnswers


    //Variabili booleane per controllare la logica di gioco
    private val _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean> get() = _isPlaying
    var isGameFinished = false

    private val _gameError = MutableLiveData<String>()
    val gameError: LiveData<String> get() = _gameError

    private val questionRepository: QuestionRepository

    //Timer di gioco
    private val _elapsedTime = MutableLiveData<String>()
    val elapsedTime: LiveData<String> = _elapsedTime

    //Punteggio della partita
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> = _score

    init {
        val questionDAO = DatabaseWK.getInstance(application).questionDAO()
        questionRepository = QuestionRepository(questionDAO)
        viewModelScope.launch {
            questionRepository.setupInteractionWithAPI()
        }
    }

    fun setDifficulty(difficulty: String) {
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

    fun onAnswerClicked(givenAnswer: String){
        viewModelScope.launch {
            checkAnswerCorrectness(givenAnswer)
        }
    }

    private fun checkNumberOfAvailableQuestions(): Int {
        return freshQuestions.size
    }

    private suspend fun checkAnswerCorrectness(givenAnswer: String){
        if (givenAnswer == questionForUser.value?.correct_answer){
            //Risposta corretta -> Fetch della prossima domanda, aggiornamento dello score
            updateScore()
            if (checkNumberOfAvailableQuestions() <= 5){
                //Se ci sono 5 o meno domande disponibili in memoria faccio una nuova chiamata API per prenderne altre 10
                //TODO: Fare attenzione al cambio categoria/difficolta' da parte dell'utente, se una delle due cambia allora vanno pulite tutte le domande presenti in memoria
                val newQuestions = questionRepository.retrieveQuestions(SMALL_AMOUNT, "Entertainment: Music", "easy")
                freshQuestions.addAll(newQuestions)
            }
            _questionForUser.postValue(nextQuestion())
        }else{
            //Risposta sbagliata -> Fine partita, salvataggio del game nel db, stop del timer, mostrare new record notification se nuovo record
            stopTimer()
            _isPlaying.postValue(false)
            freshQuestions = emptyList<Question>().toMutableList()
        }
        Log.d("Debug", "Answer clicked")
    }

    private suspend fun startGame() {
        //TODO: I parametri category e difficulty vanno passati come parametro dinamicamente
        //Ottimizzazione: se ci sono ancora domanda che non sono state poste all'utente non faccio una chiamata API
        if (freshQuestions.size==0){
            questionRepository.resetSessionToken()
            freshQuestions = questionRepository.retrieveQuestions(START_AMOUNT, "Entertainment: Music", "easy")
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

    private fun nextQuestion(): Question {
        val r = Random.nextInt(freshQuestions.size)
        val nextQ = freshQuestions[r]
        freshQuestions.removeAt(r)
        askedQuestions.add(nextQ)
        _shuffledAnswers.postValue(shuffleAnswers(nextQ))
        return nextQ
    }

    private fun shuffleAnswers(question : Question): MutableList<String> {
        val corrAnswer = question.correct_answer
        val incAnswers = question.incorrect_answers.toMutableList()
        val possibleAnswers = incAnswers
        possibleAnswers.add(corrAnswer)
        possibleAnswers.shuffle()
        return possibleAnswers
    }

    private fun startTimer() {
        isGameFinished = false
        viewModelScope.launch {
            var seconds = 0
            while (!isGameFinished) {
                val formattedTime = String.format(Locale.getDefault(),"%02d:%02d", seconds / 60, seconds % 60)
                _elapsedTime.postValue(formattedTime)
                delay(1000L)
                seconds++
            }
        }
    }

    private fun stopTimer(){
        isGameFinished = true
    }

    private fun updateScore(){
        _score.postValue(_score.value!! + 1)
    }


}