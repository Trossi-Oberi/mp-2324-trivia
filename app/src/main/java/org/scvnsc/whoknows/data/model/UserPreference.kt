package org.scvnsc.whoknows.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.scvnsc.whoknows.data.model.User

@Entity(
    tableName = "user_preferences",
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"]),
        ForeignKey(entity = Category::class, parentColumns = ["id"], childColumns = ["categoryId"])
    ]
)
data class UserPreference(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val userId: Int,
    val categoryId: Int,
    val preferredLanguage: String
)
