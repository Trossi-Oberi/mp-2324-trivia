package it.scvnsc.whoknows.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import it.scvnsc.whoknows.data.dao.CategoryDAO
import it.scvnsc.whoknows.data.dao.GameDAO
import it.scvnsc.whoknows.data.dao.GameQuestionDAO
import it.scvnsc.whoknows.data.dao.QuestionDAO
import it.scvnsc.whoknows.data.dao.UserDAO
import it.scvnsc.whoknows.data.model.Category
import it.scvnsc.whoknows.data.model.Game
import it.scvnsc.whoknows.data.model.GameQuestion
import it.scvnsc.whoknows.data.model.Question
import it.scvnsc.whoknows.data.model.User
import it.scvnsc.whoknows.data.model.UserPreference
import it.scvnsc.whoknows.utils.Converters

@Database(entities = [Category::class, Question::class, User::class, UserPreference::class, Game::class, GameQuestion::class], version = 1)
@TypeConverters(Converters::class)
abstract class DatabaseWK : RoomDatabase() {
    abstract fun categoryDAO(): CategoryDAO
    abstract fun questionDAO(): QuestionDAO
    abstract fun userDAO(): UserDAO
    abstract fun gameDAO(): GameDAO
    abstract fun gameQuestionDAO(): GameQuestionDAO

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

