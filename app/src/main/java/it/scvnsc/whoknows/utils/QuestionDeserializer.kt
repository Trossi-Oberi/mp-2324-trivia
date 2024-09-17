package it.scvnsc.whoknows.utils

import android.util.Log
import androidx.core.text.HtmlCompat
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import it.scvnsc.whoknows.data.model.Question
import java.lang.reflect.Type

class QuestionDeserializer : JsonDeserializer<Question> {
    private var questionID = 0
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Question {
        val jsonObject = json.asJsonObject
        val type = jsonObject.get("type").asString
        val difficulty = jsonObject.get("difficulty").asString
        val category = HtmlCompat.fromHtml(jsonObject.get("category").asString, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
        val question = HtmlCompat.fromHtml(jsonObject.get("question").asString, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
        val correctAnswer = HtmlCompat.fromHtml(jsonObject.get("correct_answer").asString, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
        val incorrectAnswers = incAnswersHtmlParser(jsonObject.get("incorrect_answers").asJsonArray.map { it.asString })
        val categoryID = CategoryManager.categories[category].toString()
        questionID++
        // Imposta id e categoryId con i valori desiderati
        //catID e' il category ID

        Log.d("Debug", "Question ID: $questionID")

        return Question(questionID,type, difficulty, category, question, correctAnswer, incorrectAnswers,
            categoryID
        )
    }

    private fun incAnswersHtmlParser(incAnswersJson: List<String>): List<String> {
        val parsedIncAnswers = mutableListOf<String>()
        for (ans in incAnswersJson){
            parsedIncAnswers.add(HtmlCompat.fromHtml(ans, HtmlCompat.FROM_HTML_MODE_LEGACY).toString())
        }
        return parsedIncAnswers
    }


}