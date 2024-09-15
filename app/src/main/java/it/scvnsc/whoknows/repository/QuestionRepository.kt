package it.scvnsc.whoknows.repository

import android.util.Log
import com.google.gson.GsonBuilder
import it.scvnsc.whoknows.data.dao.QuestionDAO
import it.scvnsc.whoknows.data.model.Question
import it.scvnsc.whoknows.data.network.ApiService
import it.scvnsc.whoknows.utils.CategoryManager
import it.scvnsc.whoknows.utils.QuestionDeserializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class QuestionRepository(private val questionDAO: QuestionDAO) {
    //Si occupa dell'interazione tra la tabella del DB questions e ViewModel del game

    //Si occupa anche di fare la chiamata API per recuperare le domande
    val gson = GsonBuilder()
        .registerTypeAdapter(Question::class.java, QuestionDeserializer())
        .create()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://opentdb.com/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    suspend fun setupCategoryManager() {
        // Step 1: Recuperare le categorie per popolare il CategoryManager (HashMap che collega categoryName e categoryID)
        val categories = apiService.getCategories()
        Log.d("Debug", "Categories: ${categories.trivia_categories[0]}")
        Log.d("Debug", "Categories: ${categories.trivia_categories[1]}")
        CategoryManager.buildCategoriesMap(categories.trivia_categories)
    }

    // Step 2: Recupera un (amount) di domande della categoria e della difficolta' scelte ogni volta che l'utente clicka Play o risponde correttamente a tutte le domande precedenti
    suspend fun retrieveQuestions(amount: Int, categoryName: String, difficulty: String): List<Question>? {
        //Prendo le nuove domande dall'API
        //TODO: Implementare Session Token per prendere domande sempre diverse
        val categoryID = CategoryManager.categories[categoryName]
        val questionResponse = categoryID?.let { id -> apiService.getQuestions(amount, id, difficulty.lowercase()) }

        //Uso il DAO per scrivere le domande nel DB
        if (questionResponse != null) {
            val fetchedQuestions = questionResponse.results
            Log.d("Debug", "Question: ${fetchedQuestions.get(0).question}")
            Log.d("Debug", "Question: ${fetchedQuestions.get(1).question}")

            //SOLO PER PROVA NE INSERISCO 2 MANUALMENTE

            withContext(Dispatchers.IO) {
                questionDAO.insertQuestions(fetchedQuestions)
            }
        }
        //Faccio una query al DB per prendere le domande
        val dbQuestions = questionDAO.getLastQuestions()
        Log.d("Debug", "DB Questions: ${dbQuestions.get(0).question}")
        Log.d("Debug", "DB Questions: ${dbQuestions.get(1).question}")
        return dbQuestions
    }


    //check della presenza di domande nel DB, se sono presenti domande non viene eseguita nuovamente la richiesta API
    fun questionsDBisEmpty(): Boolean {
        return true
    }
}