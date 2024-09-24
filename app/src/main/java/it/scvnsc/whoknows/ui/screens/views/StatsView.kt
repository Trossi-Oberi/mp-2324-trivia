package it.scvnsc.whoknows.ui.screens.views

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import it.scvnsc.whoknows.R
import it.scvnsc.whoknows.data.model.Game
import it.scvnsc.whoknows.ui.screens.components.TopBar
import it.scvnsc.whoknows.ui.theme.WhoKnowsTheme
import it.scvnsc.whoknows.ui.theme.bottom_bar_padding
import it.scvnsc.whoknows.ui.theme.buttonsTextStyle
import it.scvnsc.whoknows.ui.theme.disabled_elevation
import it.scvnsc.whoknows.ui.theme.medium_padding
import it.scvnsc.whoknows.ui.theme.rowButtonTextStyle
import it.scvnsc.whoknows.ui.theme.row_button_height
import it.scvnsc.whoknows.ui.theme.star_icon_size
import it.scvnsc.whoknows.ui.viewmodels.SettingsViewModel
import it.scvnsc.whoknows.ui.viewmodels.StatsViewModel
import it.scvnsc.whoknows.utils.isLandscape

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StatsView(
    navController: NavHostController,
    statsViewModel: StatsViewModel,
    settingsViewModel: SettingsViewModel
) {

    //TODO:: reset statistiche
    val context = LocalContext.current

    //determino orientamento schermo
    val isLandscape = isLandscape()

    //Implemento uno stato per il messaggio del toast
    val toastMessage = rememberSaveable { mutableStateOf("") }

    //Osservo i game che il viewmodel prende dal DAO
    val games = statsViewModel.retrievedGames.observeAsState().value

    statsViewModel.gameDeletionComplete.observe(LocalLifecycleOwner.current) { isCompleted ->
        if (isCompleted) {
            toastMessage.value = "Games history deleted"

            //il processo di cancellazione è terminato
            statsViewModel.setGameDeletionComplete(false)
        }
    }

    //Inizializzo il viewmodel
    with(statsViewModel) {
        retrieveGamesOnStart()
    }


    //Mostro il toast solo se il messaggio non e' vuoto
    LaunchedEffect(key1 = toastMessage.value) {
        if (toastMessage.value.isNotEmpty()) {
            Toast.makeText(context, toastMessage.value, Toast.LENGTH_SHORT).show()

            //Dopo aver mostrato il toast resetto il contenuto del messaggio
            toastMessage.value = ""
        }
    }


    WhoKnowsTheme(darkTheme = settingsViewModel.isDarkTheme.observeAsState().value == true) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            content = {
                if (isLandscape) {
                    //TODO:: da sistemare
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            TopBar(
                                navController = navController,
                                onLeftBtnClick = { navController.navigate("home") },
                                leftBtnIcon = Icons.AutoMirrored.Filled.ArrowBack,
                                showTitle = true,
                                title = context.getString(R.string.app_name),
                                showRightButton = true,
                                rightBtnIcon = Icons.Default.Delete,
                                onRightBtnClick = {
                                    if (games!!.isEmpty()) {
                                        toastMessage.value = "No games to delete"
                                    } else {
                                        statsViewModel.deleteGames()
                                    }
                                },
                                showThemeChange = false,
                                settingsViewModel = settingsViewModel
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            StatsPage(games, statsViewModel)
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun StatsPage(games: List<Game>?, statsViewModel: StatsViewModel) {
    val showGameDetails = rememberSaveable { mutableStateOf(false) }

    val gameQuestionReady = statsViewModel.gameQuestionsReady.observeAsState().value
    val selectedGame = statsViewModel.selectedGame.observeAsState().value

    if (!showGameDetails.value && gameQuestionReady == false) {
        //Colonna portante che racchiude tutta la schermata
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = bottom_bar_padding)
        ) {
            //HEADER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(row_button_height)
            ) {
                StatsHeader()
            }

            //Al suo interno usa una LazyColumn per mostrare i game
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(1.dp, Color.Red)
            ) {
                ShowGames(games, showGameDetails, statsViewModel)
            }
        }
    } else if (showGameDetails.value && gameQuestionReady == false){
        LoadingScreen()
    } else if (showGameDetails.value && gameQuestionReady == true){
        GameDetails(selectedGame!!, showGameDetails, statsViewModel)
    }


}

@Composable
fun ShowGames(
    games: List<Game>?,
    showGameDetails: MutableState<Boolean>,
    statsViewModel: StatsViewModel
) {
    if (games != null) {
        LazyColumn {
            items(games) { game ->
                StatsRow(game, showGameDetails, statsViewModel)
            }
        }
    }
}

@Composable
fun StatsHeader() {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .border(1.dp, Color.Magenta)
    ) {
        //BOX SCORE
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxSize()
                .weight(0.33F)
        ) {
            Text(
                text = "Score",
                style = buttonsTextStyle
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxSize()
                .weight(0.33F)
        ) {
            Text(
                text = "Difficulty",
                style = buttonsTextStyle
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxSize()
                .weight(0.33F)
        ) {
            Text(
                text = "Date",
                style = buttonsTextStyle
            )
        }
    }
}

@Composable
fun StatsRow(game: Game, showGameDetails: MutableState<Boolean>, statsViewModel: StatsViewModel) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(row_button_height)
            .clickable {
                with(statsViewModel) {
                    setSelectedGame(game)
                    retrieveSelectedGameQuestions()
                }
                showGameDetails.value = true
            }
            .fillMaxWidth()
    ) {
        //BOX SCORE
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxSize()
                .weight(0.33F)

        ) {
            Text(
                text = "${game.score}",
                style = rowButtonTextStyle
            )
        }

        //BOX DIFFICULTY
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxSize()
                .weight(0.33F)

        ) {
            Text(
                text = game.difficulty,
                style = rowButtonTextStyle
            )
        }

        //BOX DATE
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxSize()
                .weight(0.33F)
        ) {
            Text(
                text = game.date,
                style = rowButtonTextStyle
            )
        }
    }
}

@Composable
fun GameDetails(
    game: Game,
    showGameDetails: MutableState<Boolean>,
    statsViewModel: StatsViewModel
) {
    //Colonna portante che racchiude tutta la schermata dei dettagli del gioco
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = bottom_bar_padding)
            .border(1.dp, Color.Red)
    ) {
        //HEADER
        Header()

        //GAME DETAILS
        GameDetailsBox(game)

        //QUESTIONS BOX
        //TODO:: sistemare stampa delle domande
        QuestionsBox(game, statsViewModel)

        //GO BACK
        GoBackButton(showGameDetails, statsViewModel)
    }
}

@Composable
fun Header() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(row_button_height)
            .border(1.dp, Color.Red)
    ) {
        Text(
            text = "Game details",
            style = buttonsTextStyle
        )
    }
}


@Composable
fun GameDetailsBox(game: Game) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .border(1.dp, Color.Blue)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = medium_padding)
                    .weight(0.5f)
                    .border(1.dp, Color.Green)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(
                        text = "Score: ${game.score}",
                        style = buttonsTextStyle,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(0.5F)
                            .border(1.dp, Color.Green),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Duration: ${game.duration}",
                        style = buttonsTextStyle,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(0.5F)
                            .align(Alignment.CenterVertically),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .weight(0.5F)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Date:\n ${game.date}",
                            style = buttonsTextStyle,
                            textAlign = TextAlign.Center,
                            maxLines = 3
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(0.5F)
                            .fillMaxWidth()
                    ) {
                        Button(
                            onClick = { /* do nothing */ },
                            modifier = Modifier
                                .fillMaxWidth(),
                            elevation = ButtonDefaults.buttonElevation(
                                0.dp,
                                0.dp,
                                0.dp,
                                0.dp,
                                disabled_elevation
                            ),
                            enabled = false,
                            content = {
                                when (game.difficulty) {
                                    "easy" -> {
                                        Icon(
                                            Icons.Default.Star,
                                            tint = MaterialTheme.colorScheme.onPrimary,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(star_icon_size)
                                                .fillMaxSize()
                                        )
                                    }

                                    "medium" -> {
                                        Row {
                                            for (i in 1..2) {
                                                Icon(
                                                    Icons.Default.Star,
                                                    tint = MaterialTheme.colorScheme.onPrimary,
                                                    contentDescription = null,
                                                    modifier = Modifier
                                                        .size(star_icon_size)
                                                        .fillMaxSize()
                                                )
                                            }
                                        }
                                    }

                                    "hard" -> {
                                        Row {
                                            for (i in 1..3) {
                                                Icon(
                                                    Icons.Default.Star,
                                                    tint = MaterialTheme.colorScheme.onPrimary,
                                                    contentDescription = null,
                                                    modifier = Modifier
                                                        .size(star_icon_size)
                                                        .fillMaxSize()
                                                )
                                            }
                                        }
                                    }

                                    else -> {
                                        Text("Unknown difficulty")
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun QuestionsBox(game: Game, statsViewModel: StatsViewModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .border(1.dp, Color.Blue)
            .background(Color.Red)
    ) {
        QuestionsHeader()

        QuestionsList(statsViewModel)
    }
}

@Composable
fun QuestionsList(statsViewModel: StatsViewModel) {
    LazyColumn {
        with(statsViewModel) {
            items(selectedGameQuestions.value!!.size) {
                for (question in selectedGameQuestions.value!!) {
                    Box(){
                        Row(){
                            Text(text = question.question)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuestionsHeader() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(row_button_height)
            .border(1.dp, Color.Blue)
    ) {
        Text(
            text = "Questions",
            style = buttonsTextStyle
        )
    }
}


@Composable
fun GoBackButton(showGameDetails: MutableState<Boolean>, statsViewModel: StatsViewModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(row_button_height)
            .padding(start = 50.dp, end = 50.dp, top = 30.dp)
    ) {
        Button(
            onClick = {
                showGameDetails.value = false
                statsViewModel.setGameQuestionsReady(false)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(row_button_height),
        ) {
            Text("Torna indietro")
        }
    }
}

