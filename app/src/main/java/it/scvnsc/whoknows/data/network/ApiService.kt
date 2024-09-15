package it.scvnsc.whoknows.data.network

import retrofit2.http.GET
import retrofit2.http.Query

fun Int?.orZero() = this ?: 0
fun String?.orEmpty() = this ?: ""

interface ApiService {
    @GET("api_category.php")
    suspend fun getCategories(): CategoryResponse

    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") amount: Int,
        @Query("category") category: Int? = null,
        @Query("difficulty") difficulty: String? = null
    ): QuestionResponse

}