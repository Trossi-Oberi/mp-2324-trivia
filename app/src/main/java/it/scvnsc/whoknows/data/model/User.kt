package it.scvnsc.whoknows.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val username: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val birthDate: String, //TODO: verify Date typo
    val avatar: ByteArray?,
    val preferredCategories: List<String>,
    val language: String = "it", //default value
    val isOnline: Boolean = false
)
