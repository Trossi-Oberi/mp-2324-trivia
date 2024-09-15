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
import kotlinx.coroutines.launch


class GameViewModel(application: Application) : AndroidViewModel(application) {

    //TODO: Cambiare la logica di come vengono passati i parametri difficulty e category alla chiamata API
    // (Ora difficulty e category vengono impostati nella scheda settings)
    private val _selectedDifficulty = MutableLiveData("MIXED") //valore di default
    private val _showDifficultySelection = MutableLiveData(false)
    val selectedDifficulty: LiveData<String> get() = _selectedDifficulty
    val showDifficultySelection: LiveData<Boolean> get() = _showDifficultySelection

    val AMOUNT = 20 //Numero arbitrario (costante) di domande da prendere dall'API

    private var freshQuestions: List<Question>?

    //Variabili booleane per controllare la logica di gioco
    private val _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean> get() = _isPlaying

    private val _gameError = MutableLiveData<String>()
    val gameError: LiveData<String> get() = _gameError

    private val questionRepository: QuestionRepository

    /*private val _question1 = MutableLiveData<Question>()
    val question1: LiveData<Question> get() = _question1
    private val _question2 = MutableLiveData<Question>()
    val question2: LiveData<Question> get() = _question2*/



    init {
        freshQuestions = emptyList()
        val questionDAO = DatabaseWK.getInstance(application).questionDAO()
        questionRepository = QuestionRepository(questionDAO)
        viewModelScope.launch {
            questionRepository.setupInteractionWithAPI()
        }
    }

    fun setDifficulty(difficulty: String) {
        _selectedDifficulty.value = difficulty
    }

    fun setShowDifficultySelection(show: Boolean) {
        _showDifficultySelection.value = show
    }

    fun onStartClicked() {
        viewModelScope.launch {
            try {
                startGame()
                _isPlaying.value = true
            } catch (e: Exception) {
                _gameError.value = "Errore durante l'avvio del gioco: ${e.message}"
            }
        }
    }

    suspend fun startGame() {

        //TODO: I parametri category e difficulty vanno passati come parametro dinamicamente
        freshQuestions = questionRepository.retrieveQuestions(AMOUNT, "Entertainment: Music", "easy")
       /* Log.d("Debug", "Questions: ${freshQuestions?.get(0)}")
        _question1.value= freshQuestions?.get(0)
        _question2.value= freshQuestions?.get(0)*/
    }


}