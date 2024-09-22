package it.scvnsc.whoknows.repository

import android.util.Log
import com.google.gson.GsonBuilder
import it.scvnsc.whoknows.data.dao.QuestionDAO
import it.scvnsc.whoknows.data.model.Question
import it.scvnsc.whoknows.data.network.ApiService
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

    private var lastQuestionID: Int? = 0
    private var SESSION_TOKEN = ""

    companion object {
        //Numero arbitrario (costante) di domande da prendere dall'API
        private const val AMOUNT = 1
    }

    //Si occupa anche di fare la chiamata API per recuperare le domande
    val gson = GsonBuilder()
        .registerTypeAdapter(Question::class.java, QuestionDeserializer())
        .create()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://opentdb.com/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    suspend fun setupInteractionWithAPI() {
        buildCategoryManager()
        SESSION_TOKEN = getSessionToken().token
    }

    // Step 1: Recuperare le categorie per popolare il CategoryManager (HashMap che collega categoryName e categoryID)
    private suspend fun buildCategoryManager() {
        val categories = apiService.getCategories()
        CategoryManager.buildCategoriesMap(categories.trivia_categories)
    }

    // Step 2: Ottieni il Session Token per ottenere risposte sempre diverse dall'API
    private suspend fun getSessionToken(): TokenResponse {
        return apiService.getToken()
    }

    suspend fun resetSessionToken() {
        apiService.resetToken(SESSION_TOKEN)
    }

    //TODO: Rimuovere parametro amount, mettere fisso ad 1 (valore costante qui in QuestionRepository)
    //Step 3: Recupera un (amount) di domande della categoria e della difficolta' scelte ogni volta che l'utente clicka Play o risponde correttamente a tutte le domande precedenti
    suspend fun retrieveNewQuestion(categoryName: String, difficulty: String): Question {

        //Prendo le nuove domande dall'API
        var categoryID: String? = ""
        if (categoryName != "") {
            categoryID = CategoryManager.categories[categoryName].toString()
        }

        Log.d("Debug", "Category ID: $categoryID")
        Log.d("Debug", "Difficulty: $difficulty")

        val questionResponse: QuestionResponse
        withContext(Dispatchers.IO) {
            questionResponse = apiService.getQuestions(
                AMOUNT,
                categoryID,
                difficulty,
                SESSION_TOKEN
            )
            Log.d("Debug", "Question Response: $questionResponse")

            //Response Code = 4 -> Token Empty, non ci sono altre nuove domande disponibili, resetto il token e rieseguo la query
            if (questionResponse.response_code == 4) {
                withContext(Dispatchers.IO) {
                    resetSessionToken()
                    retrieveNewQuestion(categoryName, difficulty)
                }
            }
        }

        //TODO: Fetched questions ora e' una question sola -> Oggetto Question invece che List
        val newFetchedQuestion = questionResponse.results[0]

//        val fetchedQuestions = questionResponse.results
//        val newQuestion = fetchedQuestions[0]
        Log.d("Debug", "Fetched Question: $newFetchedQuestion")
        val newQuestionID: Long

        withContext(Dispatchers.IO) {
            newQuestionID = questionDAO.insert(newFetchedQuestion)
        }

        newFetchedQuestion.id = newQuestionID
        Log.d("Debug", "New Question ID: ${newFetchedQuestion.id}")

        return newFetchedQuestion
    }
}

