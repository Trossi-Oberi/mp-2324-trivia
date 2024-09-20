package it.scvnsc.whoknows.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
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
    val retrievedGames: MutableLiveData<List<Game>> get() = _retrievedGames

    private val _deletedGamesCount = MutableLiveData<Int>()
    val deletedGamesCount: MutableLiveData<Int> get() = _deletedGamesCount

    private suspend fun getGames() {
        viewModelScope.launch {
            _retrievedGames.postValue(gameRepository.getAllGames())
        }
    }

    private suspend fun deleteAllGames(){
        viewModelScope.launch {
            _deletedGamesCount.postValue(gameRepository.deleteAllGames())
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