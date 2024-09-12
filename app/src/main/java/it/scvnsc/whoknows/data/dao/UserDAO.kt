package it.scvnsc.whoknows.data.dao

import androidx.room.*
import it.scvnsc.whoknows.data.model.User

@Dao
interface UserDAO {
    @Insert
    fun insert(user: User)
    @Update
    fun update(user: User)
    @Delete
    fun delete(user: User)
}