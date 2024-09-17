package it.scvnsc.whoknows.repository

import android.util.Log
import it.scvnsc.whoknows.data.dao.GameQuestionDAO
import it.scvnsc.whoknows.data.model.Game
import it.scvnsc.whoknows.data.model.GameQuestion
import it.scvnsc.whoknows.data.model.Question

class GameQuestionRepository (private val gameQuestionDAO: GameQuestionDAO){

    suspend fun saveGameAndQuestions(playedGame: Game, askedQuestions: MutableList<Question>) {
        Log.d("Debug", "Game saved with ID: ${playedGame.id}")
        Log.d("Debug", "Questions saved with IDs: ${askedQuestions.map { it.id }}")
        gameQuestionDAO.insertGameWithQuestions(playedGame, askedQuestions)
    }

    suspend fun getQuestionsIDs(game: Game): List<Int> {
        return gameQuestionDAO.getQuestionsIDs(game.id)
    }
}