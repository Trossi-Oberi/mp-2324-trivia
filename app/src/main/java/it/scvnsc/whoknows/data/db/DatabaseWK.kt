package it.scvnsc.whoknows.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [Category::class, Question::class, User::class, UserPreference::class, Game::class, GameQuestion::class], version = 1)
@TypeConverters(Converters::class)
abstract class DatabaseWK : RoomDatabase() {
    abstract fun categoryDAO(): CategoryDAO
    abstract fun questionDAO(): QuestionDAO
    abstract fun userDAO(): UserDAO
    abstract fun gameDAO(): GameDAO
    abstract fun gameQuestionDAO(): GameQuestionDAO

    companion object {
        @Volatile
        private var INSTANCE: DatabaseWK? = null

        //singleton for database instance
        @OptIn(InternalCoroutinesApi::class)
        fun getInstance(context: Context): DatabaseWK {
            //Se l'istanza è nulla costruiscila
            //Altrimenti restituiscila in modo thread-safe
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseWK::class.java,
                    "whoknows.db"
                ).build()
                INSTANCE = instance
                instance
            }


//            if (INSTANCE == null) {
//                INSTANCE = databaseBuilder(
//                    context,
//                    DatabaseWK::class.java,
//                    "whoknows.db"
//                )
//                    .build()
//            }
//            return INSTANCE as DatabaseWK
        }

    }


}

