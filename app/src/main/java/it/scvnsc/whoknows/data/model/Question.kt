package it.scvnsc.whoknows.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class Question(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "difficulty") val difficulty: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "question") val question: String,
    @ColumnInfo(name = "correctAnswer") val correct_answer: String,
    @ColumnInfo(name = "incorrectAnswer") val incorrect_answers: List<String>,
    @ColumnInfo(name = "categoryId") val categoryId: Int
){
    constructor(
        type: String,
        difficulty: String,
        category: String,
        question: String,
        correct_answer: String,
        incorrect_answers: List<String>,
        categoryId: Int
    ) : this(0,type, difficulty, category, question, correct_answer, incorrect_answers, categoryId)
}
