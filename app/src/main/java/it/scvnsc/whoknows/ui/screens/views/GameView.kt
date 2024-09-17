package it.scvnsc.whoknows.ui.screens.views

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Hardware
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.scvnsc.whoknows.R
import it.scvnsc.whoknows.ui.theme.WhoKnowsTheme
import it.scvnsc.whoknows.ui.theme.buttonsTextStyle
import it.scvnsc.whoknows.ui.theme.home_buttons_height
import it.scvnsc.whoknows.ui.theme.home_buttons_shape
import it.scvnsc.whoknows.ui.theme.home_buttons_width
import it.scvnsc.whoknows.ui.theme.small_padding
import it.scvnsc.whoknows.ui.theme.topBarTextStyle
import it.scvnsc.whoknows.ui.viewmodels.GameViewModel
import it.scvnsc.whoknows.ui.viewmodels.SettingsViewModel
import it.scvnsc.whoknows.utils.DifficultyType

//TODO: inizializzare un timer dopo la chiamata API per le domande della durata di 5 secondi che modifica una variabile booleana da false a true (è possibile effettuare una nuova chiamata all'API delle domande)
// initial = true, poi diventa false passano 5 secondi e ritorna true (_canMakeNewAPICalls: LiveData<Boolean>)
// se l'utente prova ad iniziare una nuova partita mentre la variabile è false allora mostra un'animazione di caricamento fino all'aggiornamento della variabile

//TODO: se il giocatore perde la partita viene mostrato un dialog con due pulsanti: "Play again", "Change game settings"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameView(
    navController: NavController,
    gameViewModel: GameViewModel,
    settingsViewModel: SettingsViewModel
) {
    WhoKnowsTheme(darkTheme = settingsViewModel.isDarkTheme.observeAsState().value == true) {

        val context = LocalContext.current

        Surface {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize(),
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = context.getString(R.string.app_name),
                                style = topBarTextStyle,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { navController.navigate("home") }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        },
                        actions = {
                            //pulsante per cambiare tema
                            with(settingsViewModel) {
                                IconButton(onClick = { toggleDarkTheme() }) {
                                    if (isDarkTheme.observeAsState().value == true) {
                                        Icon(
                                            androidx.compose.material.icons.Icons.Filled.DarkMode,
                                            contentDescription = null
                                        )
                                    } else {
                                        Icon(
                                            androidx.compose.material.icons.Icons.Filled.WbSunny,
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        }
                    )
                },


                //TODO: capire come implementare il logo come immagine nella home e nelle schermate
                content = { padding ->
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        if (gameViewModel.isPlaying.observeAsState().value == false) {
                            GameViewMainPage(gameViewModel)
                        }

                        if (gameViewModel.isPlaying.observeAsState().value == true) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                with(gameViewModel) {
                                    //Timer nella UI
                                    GameTimer(this)

                                    //Punteggio nella UI
                                    GameScore(this)

                                    //Domanda nella UI
                                    ShowQuestion(this)

                                    //Possibili risposte nella UI
                                    ShowAnswers(this)
                                }
                            }
                        }
                    }

                }
            )

        }

    }
}

@Composable
fun GameViewMainPage(gameViewModel: GameViewModel) {
    var showDifficultySelectionDialog by rememberSaveable { mutableStateOf(false) }
    var showCategorySelectionDialog by rememberSaveable { mutableStateOf(false) }

    var selectedDifficulty by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf("") }

    Box {
        if (showDifficultySelectionDialog) {
            DifficultySelectionDialog(
                onDismissRequest = {
                    showDifficultySelectionDialog = false
                },
                onDifficultySelected = {
                    selectedDifficulty = it
                    gameViewModel.setDifficulty(it.lowercase())
                },
                gameViewModel = gameViewModel
            )
        }

        if (showCategorySelectionDialog) {
            CategorySelectionDialog(
                onDismissRequest = {
                    showCategorySelectionDialog = false
                },
                categories = gameViewModel.getCategories(),
                onCategorySelected = {
                    selectedCategory = it
                    gameViewModel.setCategory(it)
                },
                gameViewModel = gameViewModel
            )
        }


        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Button(
                onClick = {
                    gameViewModel.onStartClicked()
                },
                shape = RoundedCornerShape(home_buttons_shape),
                modifier = Modifier
                    .height(home_buttons_height)
                    .width(home_buttons_width)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(small_padding),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Filled.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )

                    Spacer(modifier = Modifier.size(15.dp))

                    Text(
                        text = "Start Game",
                        style = buttonsTextStyle,
                        textAlign = TextAlign.Center
                    )
                }

            }

            Button(
                onClick = {
                    showDifficultySelectionDialog = true
                },
                shape = RoundedCornerShape(home_buttons_shape),
                modifier = Modifier
                    .height(home_buttons_height)
                    .width(home_buttons_width)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(small_padding),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Filled.Hardware,
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )

                    Spacer(modifier = Modifier.size(15.dp))

                    Text(
                        text = "Difficulty",
                        style = buttonsTextStyle,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Button(
                onClick = {
                    showCategorySelectionDialog = true
                },
                shape = RoundedCornerShape(home_buttons_shape),
                modifier = Modifier
                    .height(home_buttons_height)
                    .width(home_buttons_width)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(small_padding),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Filled.Category,
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )

                    Spacer(modifier = Modifier.size(15.dp))

                    Text(
                        text = "Category",
                        style = buttonsTextStyle,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


@Composable
fun DifficultySelectionDialog(
    onDismissRequest: () -> Unit,
    onDifficultySelected: (String) -> Unit,
    gameViewModel: GameViewModel
) {

    var radioSelected by rememberSaveable { mutableStateOf(gameViewModel.selectedDifficulty.value) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Select difficulty") },
        text = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                for (diff in DifficultyType.entries) {
//                    TextButton(onClick = {
//                        onDifficultySelected(diff.toString())
//                        onDismissRequest()
//                        Log.d("Debug", "Selected difficulty: $diff")
//                    }) {
//                        Text(text = diff.toString())
//                    }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (diff
                                    .toString()
                                    .lowercase() == gameViewModel.selectedDifficulty.value),
                                onClick = {
                                    onDifficultySelected(diff.toString())
                                    Log.d("Debug", "$radioSelected")
                                    radioSelected = diff
                                        .toString()
                                        .lowercase()
                                    Log.d("Debug", "Selected category: ${diff.toString().lowercase()}")
                                }
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            onClick = {
                                onDifficultySelected(diff.toString())
                                radioSelected = diff
                                    .toString()
                                    .lowercase()
                                Log.d("Debug", "Selected category: ${diff.toString().lowercase()}")
                            },
                            selected = (diff
                                .toString()
                                .lowercase() == gameViewModel.selectedDifficulty.value),
                            enabled = true
                        )

                        Text(
                            text = diff.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }


                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismissRequest,
                enabled = true,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ok")
            }
        }
    )
}

@Composable
fun CategorySelectionDialog(
    onDismissRequest: () -> Unit,
    categories: List<String>,
    onCategorySelected: (String) -> Unit,
    gameViewModel: GameViewModel
) {

    var radioSelected by rememberSaveable { mutableStateOf(gameViewModel.selectedCategory.value) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Select category") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                LazyColumn {
                    itemsIndexed(categories) { _, category ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (category == gameViewModel.selectedCategory.value),
                                    onClick = {
                                        onCategorySelected(category)
                                        radioSelected = category
                                        Log.d("Debug", "Selected category: $category")
                                    }
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                onClick = {
                                    onCategorySelected(category)
                                    radioSelected = category
                                    Log.d("Debug", "Selected category: $category")
                                },
                                selected = category == radioSelected,
                                enabled = true
                            )

                            Text(
                                text = category,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismissRequest,
                enabled = true,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ok")
            }
        }
    )
}

@Composable
fun GameScore(gameViewModel: GameViewModel) {
    val currentScore = gameViewModel.score.observeAsState().value
    Text("Score: $currentScore")
}

@Composable
fun GameTimer(gameViewModel: GameViewModel) {
    val timer = gameViewModel.elapsedTime.observeAsState().value
    Text("Game time: $timer")
}

@Composable
fun ShowQuestion(gameViewModel: GameViewModel) {
    val currentQuestion = gameViewModel.questionForUser.observeAsState().value
    Log.d("Debug", "Question for user: $currentQuestion")
    Text("Category: " + (currentQuestion?.category ?: ""))
    Text("Difficulty: " + (currentQuestion?.difficulty ?: ""))
    Text(currentQuestion?.question ?: "")
}

@Composable
fun ShowAnswers(gvm: GameViewModel) {
    val answers = gvm.shuffledAnswers.observeAsState().value
    for (ans in answers!!) {
        Button(onClick = {
            gvm.onAnswerClicked(ans)
        }) {
            Text(ans)
        }
    }
}


