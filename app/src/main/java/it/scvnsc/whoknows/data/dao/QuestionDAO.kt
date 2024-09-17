package it.scvnsc.whoknows.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
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


    // Voglio solo le ultime domande inserite nel DB
    @Query("SELECT * FROM questions ORDER BY date DESC LIMIT :amount")
    //Room restituisce solo oggetti di tipo List, converto nella repository in MutableList
    suspend fun getLastQuestions(amount: Int): List<Question>

    @Query("SELECT * FROM questions WHERE id IN (:idList)")
    suspend fun getQuestionsByIDs(idList: List<Int>): List<Question>


}