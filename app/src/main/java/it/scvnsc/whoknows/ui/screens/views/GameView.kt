package it.scvnsc.whoknows.ui.screens.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import it.scvnsc.whoknows.ui.viewmodels.GameViewModel
import it.scvnsc.whoknows.utils.DifficultyType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameView(navController: NavController, gameViewModel: GameViewModel) {

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

            //TODO: Implementare Menu a Hamburger per il cambio di difficolta'
            /*topBar = {
                TopAppBar(
                    title = {Text("WhoKnows")},
                    navigationIcon = {
                        IconButton(onClick = {isMenuOpen = !isMenuOpen}){
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    }

                )
            },
            drawerContent = {*/

            //},
            bottomBar = {
                NavigationBar(
                    containerColor = Color.LightGray
                ) {
                    NavigationBarItem(
                        selected = true,
                        icon = { Icon(Icons.Default.Home, "Home") },
                        label = { Text(text = "Home") },
                        enabled = false,
                        onClick = { /* non fare nulla */ }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.BarChart, "Stats") },
                        label = { Text(text = "Stats") },
                        selected = false,
                        enabled = true,
                        onClick = {
                            navController.navigate("stats")
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Settings, "Settings") },
                        label = { Text(text = "Settings") },
                        selected = false,
                        enabled = true,
                        onClick = {
                            navController.navigate("settings")
                        }
                    )
                }
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
                        Column {
                            /*with(gameViewModel) {
                                question1.observeAsState().value?.let {
                                    Text(it.category)
                                    Text(it.question)
                                }
                                question2.observeAsState().value?.let {
                                    Text(it.category)
                                    Text(it.question)
                                }

                            }*/
                        }
                    }
                }

            }
        )
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