package it.scvnsc.whoknows.ui.screens.views

import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import it.scvnsc.whoknows.R
import it.scvnsc.whoknows.data.model.Question
import it.scvnsc.whoknows.ui.theme.WhoKnowsTheme
import it.scvnsc.whoknows.ui.theme.fontSizeMedium
import it.scvnsc.whoknows.ui.theme.titleTextStyle
import it.scvnsc.whoknows.ui.theme.topBarTextStyle
import it.scvnsc.whoknows.ui.viewmodels.GameViewModel
import it.scvnsc.whoknows.ui.viewmodels.SettingsViewModel
import it.scvnsc.whoknows.utils.DifficultyType
import it.scvnsc.whoknows.utils.QuestionSaver

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameView(
    navController: NavController,
    gameViewModel: GameViewModel,
    settingsViewModel: SettingsViewModel
) {
    WhoKnowsTheme(darkTheme = settingsViewModel.isDarkTheme.observeAsState().value == true) {

        val context = LocalContext.current

        var showDifficultySelection by rememberSaveable { mutableStateOf(true) }
        var selectedDifficulty by rememberSaveable { mutableStateOf("") }

        //TODO: Fare in modo che la difficolta' venga richiesta solamente la prima volta in assoluto che viene
        // aperta la schermata, quindi se l'utente gioca piu' di una partita non deve scegliere di nuovo la difficolta'


        if (showDifficultySelection) {
            DifficultySelectionDialog(onDifficultySelected = {
                selectedDifficulty = it
                gameViewModel.setDifficulty(it)
                gameViewModel.setShowDifficultySelection(false)
                showDifficultySelection = false
            })
        }

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
                            Button(
                                onClick = {
                                    gameViewModel.onStartClicked()
                                }
                            ) {
                                Text("Start Game")
                            }
                        }

                        if (gameViewModel.isPlaying.observeAsState().value == true) {


                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text("Prova")
                                //TODO:: DA SISTEMARE URGENTE
//                                with(gameViewModel) {
//                                    //TODO: da sistemare
//                                    val currentQuestion by questionForUser.observeAsState()
//                                    val savedCurrentQuestion = rememberSaveable(
//                                        saver = QuestionSaver.questionSaver(),
//                                        stateSaver = currentQuestion
//                                    )
//                                    val shuffledAnswers by rememberSaveable {
//                                        mutableStateOf(
//                                            shuffledAnswers.value
//                                        )
//                                    }
//                                    val timer by rememberSaveable { mutableStateOf(elapsedTime.value) }
//                                    val currentScore by rememberSaveable { mutableStateOf(score.value) }
//
//                                    //Timer nella UI
//                                    Text("Game time: $timer")
//
//                                    //Punteggio nella UI
//                                    Text("Score: $currentScore")
//
//                                    //Domanda nella UI
//                                    ShowQuestion(currentQuestion)
//
//                                    //Possibili risposte nella UI
//                                    ShowAnswers(shuffledAnswers!!, gameViewModel)
                            }
                        }
                    }
                }
            )
        }
    }
}

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

@Composable
fun DifficultySelectionDialog(onDifficultySelected: (String) -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = "Select difficulty") },
        text = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // Pulsanti per le difficoltà
                for (diff in DifficultyType.entries) {
                    Button(
                        onClick = {
                            onDifficultySelected(diff.toString())

                        }
                    ) {
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