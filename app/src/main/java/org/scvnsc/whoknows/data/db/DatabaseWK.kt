package org.scvnsc.whoknows.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.scvnsc.whoknows.data.dao.CategoryDAO
import org.scvnsc.whoknows.data.dao.QuestionDAO
import org.scvnsc.whoknows.data.dao.UserDAO
import org.scvnsc.whoknows.data.model.Category
import org.scvnsc.whoknows.data.model.Question
import org.scvnsc.whoknows.data.model.User
import org.scvnsc.whoknows.data.model.UserPreference
import org.scvnsc.whoknows.utils.Converters

@Database(entities = [Category::class, Question::class, User::class, UserPreference::class], version = 1)
@TypeConverters(Converters::class)
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

