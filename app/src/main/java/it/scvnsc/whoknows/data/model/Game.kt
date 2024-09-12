package it.scvnsc.whoknows.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

//un game si dice vinto se l'utente risponde correttamente a 10 domande di fila

@Entity(tableName = "games")
data class Game(
    //game id defined progressively
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo val userID: Int,
    //var questions: List<Question>, //l'entity game contiene la lista delle domande che sono state poste all'utente
    @ColumnInfo val date: String,
    @ColumnInfo var score: Int,
    @ColumnInfo val duration: Long

)
