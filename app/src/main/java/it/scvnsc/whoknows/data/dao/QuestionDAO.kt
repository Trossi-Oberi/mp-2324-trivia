package it.scvnsc.whoknows.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import it.scvnsc.whoknows.data.model.Question



@Dao
interface QuestionDAO {

    @Insert
    suspend fun insert(question: Question)
    @Insert
    suspend fun insertQuestions(questions: List<Question>)
    @Update
    suspend fun update(question: Question)
    @Delete
    suspend fun delete(question: Question)


    //TODO: Sistemare, voglio solo le ultime 20 domande inserite nel DB, devo fare un parametro data per Question
    @androidx.room.Query("SELECT * FROM questions")
    suspend fun getLastQuestions(): List<Question>


}