package org.scvnsc.whoknows.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "categories")
data class Category(
    //category id defined by TriviaAPI
    @PrimaryKey(autoGenerate = false) val id: Int,
    val name: String
)
