package it.scvnsc.whoknows.data.network

import android.media.session.MediaSession.Token
import retrofit2.http.GET
import retrofit2.http.Query

fun Int?.orZero() = this ?: 0
fun String?.orEmpty() = this ?: ""

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
        @Query("category") category: Int? = null,
        @Query("difficulty") difficulty: String? = null,
        @Query("token") token: String? = null
    ): QuestionResponse

}