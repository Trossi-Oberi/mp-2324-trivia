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
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameView(
    navController: NavController,
    gameViewModel: GameViewModel,
    settingsViewModel: SettingsViewModel
) {
    WhoKnowsTheme(darkTheme = settingsViewModel.isDarkTheme.observeAsState().value == true) {

        val context = LocalContext.current

        //TODO: Fare in modo che la difficolta' venga richiesta solamente la prima volta in assoluto che viene
        // aperta la schermata, quindi se l'utente gioca piu' di una partita non deve scegliere di nuovo la difficolta'

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
                //Provvisorio
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
                                Text("Prova")
                                //TODO:: DA SISTEMARE URGENTE
                                with(gameViewModel) {
                                    //TODO: da sistemare

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
                    with(gameViewModel) {
                        when (it) {
                            "EASY" -> setDifficulty(DifficultyType.EASY)
                            "MEDIUM" -> setDifficulty(DifficultyType.MEDIUM)
                            "HARD" -> setDifficulty(DifficultyType.HARD)
                            else -> setDifficulty(DifficultyType.MIXED)
                        }
                    }
                    //TODO:: rimuovere gameViewModel.setShowDifficultySelection(false)
                }
            )
        }

        if (showCategorySelectionDialog) {
            CategorySelectionDialog(
                onCategorySelected = {
                    selectedCategory = it
                    gameViewModel.setCategory(it)
                    //TODO:: rimuovere gameViewModel.setShowCategorySelection(false)
                },
                onDismissRequest = {
                    showCategorySelectionDialog = false
                },
                categories = gameViewModel.getCategories()
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
    onDifficultySelected: (String) -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Select difficulty") },
        text = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
//                // Pulsanti per le difficoltà
//                for (diff in DifficultyType.entries) {
//                    Button(
//                        onClick = {
//                            onDifficultySelected(diff.toString())
//                        }
//                    ) {
//                        Text(text = diff.toString())
//                    }
//                }

                for (diff in DifficultyType.entries) {
                    TextButton(onClick = {
                        onDifficultySelected(diff.toString())
                        onDismissRequest()
                        Log.d("Debug", "Selected difficulty: $diff")
                    }) {
                        Text(text = diff.toString())
                    }
                }

            }
        },
        confirmButton = {
            // Se necessario, un pulsante "Conferma"
        }
    )
}

@Composable
fun CategorySelectionDialog(
    onDismissRequest: () -> Unit,
    categories: List<String>,
    onCategorySelected: (String) -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Select category") },
        text = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                LazyColumn {
                    itemsIndexed(categories) { index, category ->
                        TextButton(onClick = {
                            onCategorySelected(category)
                            onDismissRequest()
                            Log.d("Debug", "Selected category: $category")
                        }) {
                            Text(text = category)
                        }
                    }
                }
            }
        },
        confirmButton = {
            // Se necessario, un pulsante "Conferma"
        }
    )

//    Dialog(onDismissRequest = onDismissRequest) {
//        Surface(shape = MaterialTheme.shapes.medium, tonalElevation = 24.dp) {
//            Column {
//                LazyColumn {
//                    itemsIndexed(categories) { index, category ->
//                        TextButton(onClick = {
//                            onCategorySelected(category)
//                            onDismissRequest()
//                        }) {
//                            Text(text = category)
//                        }
//                    }
//                }
//            }
//        }
//    }
}

/*
@Composable
fun ShowQuestion(currentQuestion: Question?) {
    Log.d("Debug", "Question for user: $currentQuestion")
    Text("Category: " + (currentQuestion?.category ?: ""))
    Text("Difficulty: " + (currentQuestion?.difficulty ?: ""))
    Text(currentQuestion?.question ?: "")
}

@Composable
fun ShowAnswers(answers: List<String>, gvm: GameViewModel) {
    for (ans in answers) {
        Button(onClick = {
            gvm.onAnswerClicked(ans)
        }) {
            Text(ans)
        }
    }
}
*/

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

//@Composable
//fun DifficultySelectionDialog(onDifficultySelected: (String) -> Unit) {
//    AlertDialog(
//        onDismissRequest = {},
//        title = { Text(text = "Select difficulty") },
//        text = {
//            Column(
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally,
//                modifier = Modifier
//                    .fillMaxSize()
//            ) {
//                // Pulsanti per le difficoltà
//                for (diff in DifficultyType.entries) {
//                    Button(
//                        onClick = {
//                            onDifficultySelected(diff.toString())
//
//                        }
//                    ) {
//                        Text(text = diff.toString())
//                    }
//                }
//
//            }
//        },
//        confirmButton = {
//            // Se necessario, un pulsante "Conferma"
//        }
//    )
//}