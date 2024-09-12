package it.scvnsc.whoknows.data.dao

import androidx.room.Dao
import androidx.room.Insert
import it.scvnsc.whoknows.data.model.Game

@Dao
interface GameDAO {
    @Insert
    suspend fun insertGame(game: Game)

}