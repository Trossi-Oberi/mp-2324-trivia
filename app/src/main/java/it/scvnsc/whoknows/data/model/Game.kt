package it.scvnsc.whoknows.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class Game(
    //game id defined progressively
    @PrimaryKey(autoGenerate = false) val id: Int,
    @ColumnInfo var score: Int?,
    @ColumnInfo var difficulty: String,
    @ColumnInfo var category: String,
    @ColumnInfo val duration: String?,
    @ColumnInfo val date: String
) {
    /*constructor(
        id: Int,
        score: Int?,
        difficulty: String,
        category: String,
        duration: String?,
        date: String
    ) : this(id, score, difficulty, category, duration, date)*/
}
