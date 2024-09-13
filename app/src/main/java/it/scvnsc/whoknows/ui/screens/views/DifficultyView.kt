package it.scvnsc.whoknows.ui.screens.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.scvnsc.whoknows.ui.viewmodels.GameViewModel
import it.scvnsc.whoknows.utils.DifficultyType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DifficultyView(navController: NavController, gameViewModel: GameViewModel) {
    val BUTTON_WIDTH = 120.dp
    val BUTTON_HEIGHT = 70.dp
    val BUTTON_PADDING = 8.dp
    Surface {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),

            contentColor = Color.Blue,
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
            //TODO: fare stub per simulare esecuzione partita, quindi domande finte e risposta standard
            //TODO: capire come implementare il logo come immagine nella home e nelle schermate
            //TODO: prima di iniziare la partita schermata che chiede la difficolta': facile, medio, difficile, mista
            content = { padding ->
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)

                ) {
                    Text("Choose difficulty level")


                    Row (
                        modifier = Modifier.padding(12.dp)
                    ){
                        //colonna 1, bottoni per difficolta' facile e difficile
                        Column (
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(6.dp)

                        ){
                            //bottone difficolta' easy
                            Button(
                                onClick = {
                                    gameViewModel.setDifficulty(DifficultyType.EASY.toString())
                                    navController.navigate("game")
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .width(BUTTON_WIDTH)
                                    .height(BUTTON_HEIGHT)
                                    .padding(BUTTON_PADDING)

                                ) {
                                Text(
                                    text = DifficultyType.EASY.toString()
                                )
                            }

                            //bottone difficolta' hard
                            Button(
                                onClick = {
                                    gameViewModel.setDifficulty(DifficultyType.HARD.toString())
                                    navController.navigate("game")
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .width(BUTTON_WIDTH)
                                    .height(BUTTON_HEIGHT)
                                    .padding(BUTTON_PADDING)

                                ) {
                                Text(
                                    text = DifficultyType.HARD.toString()
                                )
                            }

                        }
                        //colonna 2, bottoni per difficolta' media e mista
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(6.dp)
                        ) {
                            //bottone difficolta' media
                            Button(
                                onClick = {
                                    gameViewModel.setDifficulty(DifficultyType.MEDIUM.toString())
                                    navController.navigate("game")
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .width(BUTTON_WIDTH)
                                    .height(BUTTON_HEIGHT)
                                    .padding(BUTTON_PADDING)

                                ) {
                                Text(
                                    text = DifficultyType.MEDIUM.toString()
                                )
                            }

                            //bottone difficolta' mixed
                            Button(
                                onClick = {
                                    gameViewModel.setDifficulty(DifficultyType.MIXED.toString())
                                    navController.navigate("game")
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .width(BUTTON_WIDTH)
                                    .height(BUTTON_HEIGHT)
                                    .padding(BUTTON_PADDING)

                                ) {
                                Text(
                                    text = DifficultyType.MIXED.toString()
                                )
                            }
                        }

                    }


                }

            }
        )
    }
}