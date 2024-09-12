package it.scvnsc.whoknows.data.dao

import androidx.room.*
import it.scvnsc.whoknows.data.model.User

@Dao
interface UserDAO {
    @Insert
    suspend fun insert(user: User)
    @Update
    suspend fun update(user: User)
    @Delete
    suspend fun delete(user: User)
}