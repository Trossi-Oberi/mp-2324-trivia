package com.example.whoknows.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.whoknows.data.dao.CategoryDAO
import com.example.whoknows.data.dao.QuestionDAO
import com.example.whoknows.data.dao.UserDAO
import com.example.whoknows.data.model.Category
import com.example.whoknows.data.model.Question
import com.example.whoknows.data.model.User
import com.example.whoknows.data.model.UserPreference

@Database(entities = [Category::class, Question::class, User::class, UserPreference::class], version = 1)
@TypeConverters()
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

