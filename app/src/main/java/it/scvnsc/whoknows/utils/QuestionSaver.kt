package it.scvnsc.whoknows.utils

import android.os.Bundle
import androidx.compose.runtime.saveable.Saver
import it.scvnsc.whoknows.data.model.Question

class QuestionSaver {
    companion object QuestSaver{
        fun questionSaver() = Saver<Question, Bundle>(
            { question ->
                val bundle = Bundle()
                bundle.putInt("id", question.id)
                bundle.putString("type", question.type)
                bundle.putString("difficulty", question.difficulty)
                bundle.putString("category", question.category)
                bundle.putString("question", question.question)
                bundle.putString("correct_answer",question.correct_answer)
                bundle.putStringArray("incorrect_answers",question.incorrect_answers.toTypedArray())
                bundle.putInt("categoryId",question.categoryId)
                bundle.putString("date",question.date)
                return@Saver bundle
            },

            { bundle ->
                val id = bundle.getInt("id")
                val type = bundle.getString("type") ?: ""
                val difficulty = bundle.getString("difficulty") ?: ""
                val category = bundle.getString("category") ?: ""
                val question = bundle.getString("question") ?: ""
                val correct_answer = bundle.getString("correct_answer") ?: ""
                val incorrect_answers = bundle.getStringArray("incorrect_answers") ?: emptyArray()
                val categoryId = bundle.getInt("categoryId")
                val date = bundle.getString("date") ?: ""
                return@Saver Question(id, type, difficulty, category,question,correct_answer,incorrect_answers.toList(),categoryId,date) // Create a new Question object
            }
        )
    }

}