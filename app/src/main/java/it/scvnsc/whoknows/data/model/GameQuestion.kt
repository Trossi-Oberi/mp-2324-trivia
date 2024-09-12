package it.scvnsc.whoknows.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GameQuestion(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val gameId: Int,
    val questionId: Int,
)