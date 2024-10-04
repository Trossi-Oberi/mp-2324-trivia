package it.scvnsc.whoknows.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api_category.php")
    suspend fun getCategories(): CategoryResponse

    @GET("api_token.php?command=request")
    suspend fun getToken(): TokenResponse

    @GET("api_token.php?command=reset")
    suspend fun resetToken(
        @Query("token") token: String
    ): TokenResponse


    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") amount: Int,
        @Query("category") category: String? = "",
        @Query("difficulty") difficulty: String? = "",
        @Query("token") token: String? = null
    ): QuestionResponse

}