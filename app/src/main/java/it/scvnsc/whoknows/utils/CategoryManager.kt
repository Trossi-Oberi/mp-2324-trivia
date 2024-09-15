package it.scvnsc.whoknows.utils

import it.scvnsc.whoknows.data.model.Category

class CategoryManager {
    //crea una Mappa con la coppia (categoryName, categoryID)

    companion object CatManager{
        var categories: MutableMap<String, Int> = mutableMapOf()

        fun buildCategoriesMap(categoriesList: List<Category>) {
            categoriesList.forEach { category ->
                categories[category.name] = category.id
            }
        }
    }

}
