package it.scvnsc.whoknows.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import it.scvnsc.whoknows.data.db.DatabaseWK
import it.scvnsc.whoknows.data.model.Game
import it.scvnsc.whoknows.data.model.Question
import it.scvnsc.whoknows.repository.GameQuestionRepository
import it.scvnsc.whoknows.repository.GameRepository
import it.scvnsc.whoknows.repository.QuestionRepository
import kotlinx.coroutines.launch

class StatsViewModel(application: Application) : AndroidViewModel(application) {

    private val gameRepository: GameRepository
    private val questionRepository: QuestionRepository
    private val gameQuestionRepository: GameQuestionRepository

    private val _gameQuestionsReady = MutableLiveData(false)
    val gameQuestionsReady: LiveData<Boolean> get() = _gameQuestionsReady

    private val _showGameDetails = MutableLiveData(false)
    val showGameDetails: LiveData<Boolean> get() = _showGameDetails

    fun setShowGameDetails(value: Boolean){
        _showGameDetails.value = value
    }

    fun setGameQuestionsReady(value: Boolean){
        _gameQuestionsReady.value = value
    }

    private val _retrievedGames = MutableLiveData<List<Game>>()
    val retrievedGames: LiveData<List<Game>> get() = _retrievedGames

    private val _selectedGame = MutableLiveData<Game>()
    val selectedGame: LiveData<Game> get() = _selectedGame

    private val _selectedGameQuestions = MutableLiveData<List<Question>>()
    val selectedGameQuestions: LiveData<List<Question>> get() = _selectedGameQuestions

    private val _selectedGameQuestionsIDs = MutableLiveData<List<Int>>()
    val selectedGameQuestionsIDs: LiveData<List<Int>> get() = _selectedGameQuestionsIDs

    //Booleano che indica quando la cancellazione delle partite passate e' completata
    private val _gameDeletionComplete = MutableLiveData(false)
    val gameDeletionComplete: LiveData<Boolean> get() = _gameDeletionComplete

    fun setGameDeletionComplete(value: Boolean){
        _gameDeletionComplete.value = value
    }

    fun setSelectedGame(game: Game){
        _selectedGame.value = game
    }

    private suspend fun getGames() {
        _retrievedGames.postValue(gameRepository.getAllGames())
    }

    private suspend fun deleteAllGames(){
        viewModelScope.launch {
            gameRepository.deleteAllGames()
            _gameDeletionComplete.postValue(true)
            _retrievedGames.postValue(gameRepository.getAllGames())
        }
    }

    fun retrieveGamesOnStart(){
        viewModelScope.launch {
            getGames()
        }
    }

    private suspend fun getQuestions(){
        viewModelScope.launch {
            //pulisco la lista precedentemente creata
            _selectedGameQuestions.value = emptyList()

            //eseguo l'operazione di get delle domande
            val questionIDs = gameQuestionRepository.getQuestionsIDs(_selectedGame.value!!)

            //Ottengo le domande della partita selezionata tramite l'ID delle domande
            _selectedGameQuestions.value = questionRepository.getQuestionsByIDs(questionIDs)

            _gameQuestionsReady.value = true
        }
    }

    fun retrieveSelectedGameQuestions(){
        viewModelScope.launch {
            getQuestions()
        }
    }

    fun deleteGames(){
        viewModelScope.launch {
            deleteAllGames()
        }
    }

    init {
        val gameDAO = DatabaseWK.getInstance(application).gameDAO()
        val questionDAO = DatabaseWK.getInstance(application).questionDAO()
        val gameQuestionDAO = DatabaseWK.getInstance(application).gameQuestionDAO()
        gameRepository = GameRepository(gameDAO)
        questionRepository = QuestionRepository(questionDAO)
        gameQuestionRepository = GameQuestionRepository(gameQuestionDAO)
    }

}