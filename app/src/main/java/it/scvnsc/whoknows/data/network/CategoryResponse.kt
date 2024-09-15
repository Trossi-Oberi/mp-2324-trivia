package it.scvnsc.whoknows.data.network

import it.scvnsc.whoknows.data.model.Category

data class CategoryResponse(
    val trivia_categories: List<Category>
)