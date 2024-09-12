package it.scvnsc.whoknows.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import it.scvnsc.whoknows.data.model.Category

@Entity(
    tableName = "questions",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("categoryId")
    )]
)
data class Question(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "difficulty") val difficulty: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "question") val question: String,
    @ColumnInfo(name = "correctAnswer") val correctAnswer: String,
    @ColumnInfo(name = "incorrectAnswer") val incorrectAnswers: List<String>,
    @ColumnInfo(name = "categoryId") val categoryId: Int
)
