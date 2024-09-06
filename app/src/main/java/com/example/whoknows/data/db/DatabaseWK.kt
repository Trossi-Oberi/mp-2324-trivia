package com.example.whoknows.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.example.whoknows.data.dao.CategoryDAO
import com.example.whoknows.data.dao.QuestionDAO
import com.example.whoknows.data.dao.UserDAO
import com.example.whoknows.data.model.Category
import com.example.whoknows.data.model.Question
import com.example.whoknows.data.model.User

@Database(entities = [Category::class, Question::class, User::class], version = 1)
abstract class DatabaseWK : RoomDatabase() {
    abstract fun categoryDAO(): CategoryDAO
    abstract fun questionDAO(): QuestionDAO
    abstract fun userDAO(): UserDAO

    companion object {
        private var instance: DatabaseWK? = null

        //singleton for database instance
        fun getInstance(context: Context): DatabaseWK {
            if (instance == null) {
                instance = databaseBuilder(
                    context,
                    DatabaseWK::class.java,
                    "whoknows.db"
                )
                    .createFromAsset("whoknows.db")
                    .build()
            }
            return instance as DatabaseWK
        }

    }


}

