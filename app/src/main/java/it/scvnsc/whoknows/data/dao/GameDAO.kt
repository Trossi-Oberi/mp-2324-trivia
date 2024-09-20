package it.scvnsc.whoknows.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import it.scvnsc.whoknows.data.model.Game

@Dao
interface GameDAO {
    @Insert
    suspend fun insertGame(game: Game): Long

    @Query("SELECT MAX(score) FROM games")
    suspend fun getMaxScore(): Int?

    @Query("SELECT * FROM games")
    suspend fun retrieveAllGames(): List<Game>

    @Query("DELETE FROM games")
    suspend fun deleteAllGames(): Int
}