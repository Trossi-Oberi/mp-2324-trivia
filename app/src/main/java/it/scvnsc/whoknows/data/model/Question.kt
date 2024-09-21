package it.scvnsc.whoknows.data.model

import android.annotation.SuppressLint
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Date
import java.text.SimpleDateFormat

@Entity(tableName = "questions")
data class Question(
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "difficulty") val difficulty: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "question") val question: String,
    @ColumnInfo(name = "correctAnswer") val correct_answer: String,
    @ColumnInfo(name = "incorrectAnswer") val incorrect_answers: List<String>,
    @ColumnInfo(name = "categoryId") val categoryId: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "givenAnswer") var givenAnswer: String
) :Serializable{
    @SuppressLint("SimpleDateFormat")
    constructor(
        type: String,
        difficulty: String,
        category: String,
        question: String,
        correct_answer: String,
        incorrect_answers: List<String>,
        categoryId: String
    ) : this(0,type, difficulty, category, question, correct_answer, incorrect_answers, categoryId,
        date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), "")
}
