package it.scvnsc.whoknows.ui.screens.views

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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
import androidx.navigation.NavController
import it.scvnsc.whoknows.ui.theme.WhoKnowsTheme
import it.scvnsc.whoknows.ui.viewmodels.GameViewModel
import it.scvnsc.whoknows.ui.viewmodels.SettingsViewModel
import it.scvnsc.whoknows.utils.DifficultyType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameView(navController: NavController, gameViewModel: GameViewModel, settingsViewModel: SettingsViewModel) {
    WhoKnowsTheme (darkTheme = settingsViewModel.isDarkTheme.observeAsState().value == true){

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

                contentColor = Color.Blue,
                topBar = {
                    TopAppBar(
                        title = { //empty
                        },
                        actions = {
                            //pulsante per cambiare tema
                            with(settingsViewModel) {
                                IconButton(onClick = { toggleDarkTheme() }) {
                                    if (isDarkTheme.observeAsState().value == true) {
                                        Icon(androidx.compose.material.icons.Icons.Filled.DarkMode, contentDescription = null)
                                    } else {
                                        Icon(androidx.compose.material.icons.Icons.Filled.WbSunny, contentDescription = null)
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
                        Text("Area Game - Difficolta': $selectedDifficulty")



                        Button(
                            onClick = {
                                gameViewModel.onStartClicked()
                            }
                        ) {
                            Text("Start Game")
                        }

                        if (gameViewModel.isPlaying.observeAsState().value == true) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                with(gameViewModel) {
                                    questionForUser.observeAsState().value?.let {
                                        Log.d("Debug", "Question for user: ${it.question}")
                                        Text("Category: " + it.category)
                                        Text("Difficulty: " + it.difficulty)
                                        Text(it.question)
                                        ShowAnswers(it.correct_answer, it.incorrect_answers, gameViewModel)

                                    }
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
fun ShowAnswers(correctAnswer: String, incorrectAnswers: List<String>, gvm: GameViewModel) {
    val possibleAnswers = mutableListOf<String>()
    possibleAnswers.add(correctAnswer)
    possibleAnswers.addAll(incorrectAnswers)
    possibleAnswers.shuffle()
    for (ans in possibleAnswers){
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