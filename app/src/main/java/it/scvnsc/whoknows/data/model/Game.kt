package it.scvnsc.whoknows.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class Game(
    //game id defined progressively
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo var score: Int?,
    @ColumnInfo var difficulty: String,
    @ColumnInfo var category: String,
    @ColumnInfo val duration: String?,
    @ColumnInfo val date: String
) {
    constructor(
        score: Int?,
        difficulty: String,
        category: String,
        duration: String?,
        date: String
    ) : this(0, score, difficulty, category, duration, date)
}
