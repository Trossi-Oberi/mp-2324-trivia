package it.scvnsc.whoknows.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import it.scvnsc.whoknows.data.db.DatabaseWK
import it.scvnsc.whoknows.data.model.Game
import it.scvnsc.whoknows.repository.GameQuestionRepository
import it.scvnsc.whoknows.repository.GameRepository
import it.scvnsc.whoknows.repository.QuestionRepository
import kotlinx.coroutines.launch

class StatsViewModel(application: Application) : AndroidViewModel(application) {


    private val gameRepository: GameRepository
    private val questionRepository: QuestionRepository
    private val gameQuestionRepository: GameQuestionRepository

    private val _retrievedGames = MutableLiveData<List<Game>>()
    val retrievedGames: LiveData<List<Game>> get() = _retrievedGames

    private val _selectedGame = MutableLiveData<Game>()
    val selectedGame: LiveData<Game> get() = _selectedGame

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
        viewModelScope.launch {
            _retrievedGames.postValue(gameRepository.getAllGames())
        }
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