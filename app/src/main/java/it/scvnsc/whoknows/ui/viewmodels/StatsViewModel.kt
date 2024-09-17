package it.scvnsc.whoknows.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import it.scvnsc.whoknows.data.db.DatabaseWK
import it.scvnsc.whoknows.repository.GameQuestionRepository
import it.scvnsc.whoknows.repository.GameRepository
import it.scvnsc.whoknows.repository.QuestionRepository

class StatsViewModel(application: Application) : AndroidViewModel(application)  {
    private val gameRepository: GameRepository
    private val questionRepository: QuestionRepository
    private val gameQuestionRepository: GameQuestionRepository

    init {
        val gameDAO = DatabaseWK.getInstance(application).gameDAO()
        val questionDAO = DatabaseWK.getInstance(application).questionDAO()
        val gameQuestionDAO = DatabaseWK.getInstance(application).gameQuestionDAO()
        gameRepository = GameRepository(gameDAO)
        questionRepository = QuestionRepository(questionDAO)
        gameQuestionRepository = GameQuestionRepository(gameQuestionDAO)
    }
}