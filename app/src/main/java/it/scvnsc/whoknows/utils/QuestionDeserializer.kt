package it.scvnsc.whoknows.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import it.scvnsc.whoknows.data.model.Question
import java.lang.reflect.Type
import java.util.Date

class QuestionDeserializer : JsonDeserializer<Question> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Question? {
        val jsonObject = json.asJsonObject
        val type = jsonObject.get("type").asString
        val difficulty = jsonObject.get("difficulty").asString
        val category = jsonObject.get("category").asString
        val question = jsonObject.get("question").asString
        val correctAnswer = jsonObject.get("correct_answer").asString
        val incorrectAnswers = jsonObject.get("incorrect_answers").asJsonArray.map { it.asString }
        val categoryID = CategoryManager.categories[category]

        // Imposta id e categoryId con i valori desiderati
        //catID e' il category ID
        return categoryID?.let { catID ->
            Question(type, difficulty, category, question, correctAnswer, incorrectAnswers, catID)
        }
    }
}