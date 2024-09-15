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
    @ColumnInfo val difficulty: String,
    //var questions: List<Question>, //l'entity game contiene la lista delle domande che sono state poste all'utente
    @ColumnInfo val date: String,
    @ColumnInfo var score: Int,
    @ColumnInfo val duration: Long

) {
//per utilizzare l'autogenerate dell'id creare un costruttore

//questo costruttore crea User senza id che verrà aggiunto automaticamente da Room
//    constructor(
//    username: String,
//    password: String,
//    firstName: String,
//    lastName: String,
//    birthDate: String,
//    avatar: ByteArray? = null,
//    preferredCategories: List<String> = emptyList(),
//    language: String = "it",
//    isOnline: Boolean = false
//    ) : this(
//    id = 0, //l'id verrà incrementato da Room
//    username = username,
//    password = password,
//    firstName = firstName,
//    lastName = lastName,
//    birthDate = birthDate,
//    avatar = avatar,
//    preferredCategories = preferredCategories,
//    language = language,
//    isOnline = isOnline
//    )
}
