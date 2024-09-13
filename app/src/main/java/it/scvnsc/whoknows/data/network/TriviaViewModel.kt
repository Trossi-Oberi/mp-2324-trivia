package it.scvnsc.whoknows.data.network

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyLog.TAG
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import it.scvnsc.whoknows.data.model.Category
import it.scvnsc.whoknows.data.model.Question
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/*
Questa classe si occuperà di effettuare le richieste HTTP utilizzando Volley e di convertire la risposta JSON in una lista di oggetti Question.
 */

class TriviaViewModel(application: Application) : AndroidViewModel(application) {
    private val requestQueue: RequestQueue = Volley.newRequestQueue(application.applicationContext)
    private val _questions = MutableLiveData<List<Question>>()
    val questions: LiveData<List<Question>> get() = _questions

    suspend fun getQuestions(amount: Int) {
        withContext(Dispatchers.IO) {
            val questionsResponse = JsonObjectRequest(
                Request.Method.GET,
                "https://opentdb.com/api.php?amount=$amount",
                null,
                { response ->
                    val jsonString = response.toString()

                    //parsing JSON con Gson
                    try {
                        val gson = Gson()
                        val triviaResponse: TriviaResponse =
                            gson.fromJson(jsonString, TriviaResponse::class.java)

                        // Stampa il JSON per verificare la struttura
                        Log.d("Debug", "JSON Response: $jsonString")

                        // Crea una nuova lista di Question e popolala
                        val newQuestions = mutableListOf<Question>()

                        //estrai e converti in oggetti Question
                        triviaResponse.results.forEach { triviaResult ->
                            val question = Question(
                                0,
                                triviaResult.type,
                                triviaResult.difficulty,
                                triviaResult.category,
                                triviaResult.question,
                                triviaResult.correct_answer,
                                triviaResult.incorrect_answers,
                                0 //verrà aggiornato in un secondo momento
                            )
                            newQuestions.add(question)
                            Log.d("Debug", "Questions size: ${newQuestions.size}")
                        }
                        Log.d("Debug", "Questions UNDERSCORE size: ${_questions.value?.size}")
                        Log.d("Debug", "NEWQuestions size: ${newQuestions.size}")

                        //TODO: SISTEMARE
                        _questions.value = newQuestions
                        Log.d("Debug", "Questions POST size: ${_questions.value?.size}")
                        Log.d("Debug", "Questions SENZA UNDERSCORE Size: ${questions.value?.size}")
                    } catch (e: JsonSyntaxException) {
                        Log.e(TAG, "Error parsing JSON: $e")
                        // Gestisci l'errore in modo appropriato
                    }
                },
                { error ->
                    Log.e(Log.ERROR.toString(), "Error fetching questions: $error")
                }
            )
            requestQueue.add(questionsResponse)

            //se è andato tutto a buon fine ottengo l'id della categoria
            //Operatore ? dopo value e' il SAFE CALL OPERATOR, se questions.value e' null l'espressione viene valutata come null senza causare NullPointerException
            if (questions.value != null) {
                Log.d("Debug", "Questions not empty")
                /*val categoryNames = questions.map { it.category }.toSet()
                val categoryIds = fetchCategoryIds(categoryNames)*/

                //aggiorno categoryIDs
                /*questions.forEachIndexed { index, question ->
                    val categoryName = question.category
                    val categoryId =
                        categoryIds.find { //cerco nella lista delle categorie delle domande ottenute dalla precedente chiamata l'id corrispondente e lo setto
                            it.name == categoryName
                        }?.id ?: 0
                    questions[index] =
                        question.copy(categoryId = categoryId) //sostituisco nella Question all'indice index il vero categoryID

                }*/
            }
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
                    Log.e(Log.ERROR.toString(), "Error fetching categories: $error")
                }
            )

            requestQueue.add(categoryRequest)
            categories.toList()
        }
    }
}

data class TriviaResponse(
    val response_code: Int,
    val results: List<TriviaResult>
)

data class TriviaResult(
    val type: String,
    val difficulty: String,
    val category: String,
    val question: String,
    val correct_answer: String,
    val incorrect_answers: List<String>,
    val categoryId: Int
)

data class CategoryResponse(
    val triviaCategories: List<Category>
)
