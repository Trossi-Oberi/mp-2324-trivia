package com.example.whoknows.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val username: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val birthDate: Date, //TODO: verify Date typo
    val avatar: ByteArray?,
    val preferredCategories: List<Int>,
    val language: String = "it", //default value
    val isOnline: Boolean = false
)
