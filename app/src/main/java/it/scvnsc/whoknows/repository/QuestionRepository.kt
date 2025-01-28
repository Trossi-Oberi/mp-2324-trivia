package it.scvnsc.whoknows.repository

import android.database.sqlite.SQLiteException
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import it.scvnsc.whoknows.data.dao.QuestionDAO
import it.scvnsc.whoknows.data.model.Question
import it.scvnsc.whoknows.data.network.ApiService
import it.scvnsc.whoknows.data.network.NetworkResult
import it.scvnsc.whoknows.data.network.QuestionResponse
import it.scvnsc.whoknows.data.network.TokenResponse
import it.scvnsc.whoknows.utils.CategoryManager
import it.scvnsc.whoknows.utils.QuestionDeserializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class QuestionRepository(private val questionDAO: QuestionDAO) {
    //Si occupa dell'interazione tra la tabella del DB questions e ViewModel del game

    private var SESSION_TOKEN = ""

    companion object {
        //Numero arbitrario (costante) di domande da prendere dall'API
        private const val AMOUNT = 1
    }

    //Si occupa anche di fare la chiamata API per recuperare le domande
    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(Question::class.java, QuestionDeserializer())
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://opentdb.com/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private val apiService = retrofit.create(ApiService::class.java)


    suspend fun setupInteractionWithAPI(): NetworkResult<Unit> = withContext(Dispatchers.IO) {
        try {
            buildCategoryManager()
            when (val tokenResponse = getSessionToken()) {
                is NetworkResult.Success -> SESSION_TOKEN = tokenResponse.data.token
                is NetworkResult.Error -> return@withContext NetworkResult.Error(tokenResponse.exception)
            }
            NetworkResult.Success(Unit)
        } catch (e: Exception) {
            Log.e("QuestionRepository", "Error setting up interaction with API: $e")
            NetworkResult.Error(e)
            throw e
        }
    }

    // Step 1: Recuperare le categorie per popolare il CategoryManager (HashMap che collega categoryName e categoryID)
    private suspend fun buildCategoryManager(): NetworkResult<Unit> = withContext(Dispatchers.IO) {
        try {
            val categories = apiService.getCategories()
            CategoryManager.buildCategoriesMap(categories.trivia_categories)
            Log.d("QuestionRepository", "Categories built: ${CategoryManager.categories}")
            NetworkResult.Success(Unit)
        } catch (e: Exception) {
            Log.e("QuestionRepository", "Error building categories map: $e")
            NetworkResult.Error(e)
            throw e
        }
    }

    // Step 2: Ottieni il Session Token per ottenere risposte sempre diverse dall'API
    private suspend fun getSessionToken(): NetworkResult<TokenResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getToken()
            NetworkResult.Success(response)
        } catch (e: Exception) {
            NetworkResult.Error(e)
            Log.e("QuestionRepository", "Error getting session token: $e")
            throw e
        }
    }

    suspend fun resetSessionToken(): NetworkResult<Unit> = withContext(Dispatchers.IO) {
        try {
            apiService.resetToken(SESSION_TOKEN)

            Log.d("QuestionRepository", "Session token reset: $SESSION_TOKEN")

            NetworkResult.Success(Unit)
        } catch (e: Exception) {
            NetworkResult.Error(e)
            Log.d("Debug", "Error resetting session token: $e")
            throw e
        }
    }

    //Step 3: Recupera una domanda della categoria e della difficolta' scelte ogni volta che l'utente clicka Play o risponde correttamente a tutte le domande precedenti
    suspend fun retrieveNewQuestion(categoryName: String, difficulty: String): NetworkResult<Question> = withContext(Dispatchers.IO) {
        try {
            //Prendo le nuove domande dall'API
            var categoryID: String? = ""
            if (categoryName != "") {
                categoryID = CategoryManager.categories[categoryName].toString()
            }

            val questionResponse: QuestionResponse = apiService.getQuestions(
                AMOUNT,
                categoryID,
                difficulty,
                SESSION_TOKEN
            )

            //Response Code = 4 -> Token Empty, non ci sono altre nuove domande disponibili, resetto il token e rieseguo la query
            if (questionResponse.response_code == 4) {
                resetSessionToken()
                retrieveNewQuestion(categoryName, difficulty)
            }

            val newFetchedQuestion = questionResponse.results[0]
            val newQuestionID: Long = questionDAO.insert(newFetchedQuestion)
            newFetchedQuestion.id = newQuestionID

            NetworkResult.Success(newFetchedQuestion)
        } catch (e: SQLiteException) {
            Log.e("Database", "Error inserting new question in database: $e")
            NetworkResult.Error(e)
            throw e
        } catch (e: Exception) {
            Log.e("WhoKnows", "Error retrieving new question: $e")
            NetworkResult.Error(e)
            throw e
        }
    }

    suspend fun updateLastQuestion(questionID: Long, givenAnswer: String) {
        try {
            questionDAO.updateLastQuestion(questionID.toInt(), givenAnswer)
        } catch (e: SQLiteException) {
            Log.e("Database", "Error updating last question in database: $e")
            throw e
        }
    }

    suspend fun getQuestionsByIDs(questionsIDs: List<Int>): List<Question> {
        try {
            return questionDAO.getQuestionsByIDs(questionsIDs)
        } catch (e: SQLiteException) {
            Log.e("Database", "Error retrieving questions by IDs", e)
            throw e
        }
    }
}

