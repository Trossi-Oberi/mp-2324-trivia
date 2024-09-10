package com.example.whoknows.data.dao

import androidx.room.*
import android.database.Cursor
import com.example.whoknows.data.model.User

@Dao
interface UserDAO {
    @Insert
    fun insert(user: User)
    @Update
    fun update(user: User)
    @Delete
    fun delete(user: User)
}