package it.scvnsc.whoknows.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import it.scvnsc.whoknows.data.model.Game
import it.scvnsc.whoknows.data.model.GameQuestion
import it.scvnsc.whoknows.data.model.Question

@Dao
interface GameQuestionDAO {

    //Inserisce nel DB una lista di GameQuestion (corrispondenza tra partita e domanda)
    @Insert
    suspend fun insertGameQuestions(gameQuestions: List<GameQuestion>)

    //Transazione che inserisce insieme (atomicamente) la partita e le domande (solo gli ID)
    @Transaction
    suspend fun insertGameWithQuestions(game: Game, questions: List<Question>) {
        val gameQuestions = questions.map { GameQuestion(0, gameID = game.id, questionID = it.id) }
        insertGameQuestions(gameQuestions)
    }

    //Questa query mi restituisce una lista di interi che sono gli ID delle domande fatte durante la partita
    @Query("SELECT questionID FROM games_questions WHERE gameId = :gameID")
    suspend fun getQuestionsIDs(gameID: Long): List<Int>


}