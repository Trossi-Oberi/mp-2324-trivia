package it.scvnsc.whoknows.repository

import it.scvnsc.whoknows.data.dao.GameDAO
import it.scvnsc.whoknows.data.model.Game

class GameRepository (private val gameDAO : GameDAO) {

    suspend fun saveGame(game: Game): Long{

        if (game.difficulty == "") game.difficulty = "Mixed"
        if (game.category == "") game.category = "Mixed"
        return gameDAO.insertGame(game)
    }

    suspend fun getMaxScore(): Int? {
        return gameDAO.getMaxScore()
    }

    suspend fun retrieveGames(): List<Game>{
        return gameDAO.retrieveAllGames()
    }

    suspend fun getAllGames(): List<Game> {
        return gameDAO.retrieveAllGames()
    }
}