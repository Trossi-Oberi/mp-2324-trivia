package it.scvnsc.whoknows.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games_questions")
data class GameQuestion(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo val gameID: Int,
    @ColumnInfo val questionID: Int,
){
    constructor(
        gameID: Int,
        questionID: Int
    ) : this(0, gameID, questionID)
}