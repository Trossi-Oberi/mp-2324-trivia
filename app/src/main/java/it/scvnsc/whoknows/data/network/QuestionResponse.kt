package it.scvnsc.whoknows.data.network

import it.scvnsc.whoknows.data.model.Question

data class QuestionResponse(
    val response_code: Int,
    val results: MutableList<Question>
)