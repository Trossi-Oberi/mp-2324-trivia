package com.example.whoknows.data.network

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.whoknows.data.model.Category
import com.example.whoknows.data.model.Question
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/*
Questa classe si occuperà di effettuare le richieste HTTP utilizzando Volley e di convertire la risposta JSON in una lista di oggetti Question.
 */

class TriviaApiService(private val context: Context){
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)

    suspend fun getQuestions(amount: Int): List<Question> {
        return withContext(Dispatchers.IO) {
            val questions = mutableListOf<Question>()

            val questionsResponse = JsonObjectRequest(
                Request.Method.GET,
                "https://opentdb.com/api.php?amount=$amount",
                null,
                { response ->
                    val jsonString = response.toString()

                    //parsing JSON con Gson
                    val gson = Gson()
                    val triviaResponse: TriviaResponse = gson.fromJson(jsonString, TriviaResponse::class.java)

                    //estrai e converti in oggetti Question
                    triviaResponse.results.forEach { triviaResult ->
                        val question = Question(
                            triviaResult.type,
                            triviaResult.difficulty,
                            triviaResult.category,
                            triviaResult.question,
                            triviaResult.correctAnswer,
                            triviaResult.incorrectAnswers,
                            0 //verrà aggiornato in un secondo momento
                        )
                        questions.add(question)
                    }
                },
                { error ->
                    Log.e(Log.ERROR.toString(),"Error fetching questions: $error")
                }
            )
            requestQueue.add(questionsResponse)

            //se è andato tutto a buon fine ottengo l'id della categoria
            if (questions.isNotEmpty()) {
                val categoryNames = questions.map { it.category }.toSet()
                val categoryIds = fetchCategoryIds(categoryNames)

                //aggiorno categoryIDs
                questions.forEachIndexed { index, question ->
                    val categoryName = question.category
                    val categoryId = categoryIds.find {
                        it.name == categoryName
                    }?.id ?: 0
                    questions[index] = question.copy(categoryId = categoryId)

                }
            }
            questions.toList()
        }


    }

    private suspend fun fetchCategoryIds(categoryNames: Set<String>): List<Category> {
        return withContext(Dispatchers.IO) {
            val categories = mutableListOf<Category>()
            val categoryIdsUrl = "https://opentdb.com/api_category.php"

            val categoryRequest = JsonObjectRequest(
                Request.Method.GET,
                categoryIdsUrl,
                null,
                { response ->
                    val jsonString = response.toString()

                    val gson = Gson()
                    val categoryResponse = gson.fromJson(jsonString, CategoryResponse::class.java)

                    categories.addAll(categoryResponse.triviaCategories)

                },
                { error ->
                    Log.e(Log.ERROR.toString(),"Error fetching categories: $error")
                }
            )

            requestQueue.add(categoryRequest)
            categories.toList()
        }
    }
}

data class TriviaResponse(
    val responseCode: Int,
    val results: List<TriviaResult>
)

data class TriviaResult(
    val type: String,
    val difficulty: String,
    val category: String,
    val question: String,
    val correctAnswer: String,
    val incorrectAnswers: List<String>,
    val categoryId: Int
)

data class CategoryResponse(
    val triviaCategories: List<Category>
)

//data class Category(
//    val id: Int,
//    val name: String
//)