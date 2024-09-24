package it.scvnsc.whoknows.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import it.scvnsc.whoknows.data.model.Question

@Dao
interface QuestionDAO {

    @Insert
    suspend fun insert(question: Question): Long
    @Insert
    suspend fun insertQuestions(questions: List<Question>)
    @Delete
    suspend fun delete(question: Question)


    // Voglio solo le ultime domande inserite nel DB
    @Query("SELECT * FROM questions ORDER BY date DESC LIMIT :amount")
    //Room restituisce solo oggetti di tipo List, converto nella repository in MutableList
    suspend fun getLastQuestions(amount: Int): List<Question>

    // Voglio tutte le domande inserite nel DB aventi quella lista di id
    @Query("SELECT * FROM questions WHERE id IN (:idList)")
    suspend fun getQuestionsByIDs(idList: List<Int>): List<Question>

    @Query("SELECT MAX(id) FROM questions")
    suspend fun getLastID(): Int?

    // Voglio solo l'ultima domanda inserita nel DB
    @Query("SELECT * FROM questions ORDER BY id DESC LIMIT 1")
    suspend fun getLastInsertedQuestion(): Question?

    // Voglio aggiornare una domanda specifica basandomi sul suo id
    @Query("UPDATE questions SET givenAnswer = :givenAnswer WHERE id = :questionId")
    suspend fun updateLastQuestion(questionId: Int, givenAnswer: String)


}