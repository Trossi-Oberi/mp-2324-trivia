package it.scvnsc.whoknows.data.network

data class TokenResponse (
    val response_code: Int,
    val response_message: String,
    val token: String
)