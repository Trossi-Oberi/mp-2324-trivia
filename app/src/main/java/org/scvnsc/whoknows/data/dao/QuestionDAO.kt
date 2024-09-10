package org.scvnsc.whoknows.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import org.scvnsc.whoknows.data.model.Question

@Dao
interface QuestionDAO {
    @Insert
    fun insert(question: Question)
    @Update
    fun update(question: Question)
    @Delete
    fun delete(question: Question)


}