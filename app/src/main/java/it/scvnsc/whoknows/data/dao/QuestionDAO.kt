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
    @Update
    suspend fun update(question: Question)
    @Delete
    suspend fun delete(question: Question)


}