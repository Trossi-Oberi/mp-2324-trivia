package it.scvnsc.whoknows.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GameQuestion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val gameId: Int,
    val questionId: Int,
)