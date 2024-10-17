package it.scvnsc.whoknows.ui.screens.views

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import it.scvnsc.whoknows.R
import it.scvnsc.whoknows.data.model.Game
import it.scvnsc.whoknows.ui.screens.components.TopBar
import it.scvnsc.whoknows.ui.theme.WhoKnowsTheme
import it.scvnsc.whoknows.ui.theme.buttonsTextStyle
import it.scvnsc.whoknows.ui.theme.difficulty_icon_size
import it.scvnsc.whoknows.ui.theme.fontSizeMedium
import it.scvnsc.whoknows.ui.theme.fontSizeNormal
import it.scvnsc.whoknows.ui.theme.fontSizeUpperNormal
import it.scvnsc.whoknows.ui.theme.gameheader_height
import it.scvnsc.whoknows.ui.theme.header_height
import it.scvnsc.whoknows.ui.theme.header_height_landscape
import it.scvnsc.whoknows.ui.theme.rowButtonTextStyle
import it.scvnsc.whoknows.ui.theme.row_button_height
import it.scvnsc.whoknows.ui.theme.star_icon_size
import it.scvnsc.whoknows.ui.viewmodels.SettingsViewModel
import it.scvnsc.whoknows.ui.viewmodels.StatsViewModel
import it.scvnsc.whoknows.utils.getLayoutDirection
import it.scvnsc.whoknows.utils.getSurfaceRotation
import it.scvnsc.whoknows.utils.isLandscape

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StatsView(
    navController: NavHostController,
    statsViewModel: StatsViewModel,
    settingsViewModel: SettingsViewModel
) {
    val context = LocalContext.current

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

    val showGameDetails = statsViewModel.showGameDetails.observeAsState().value


    WhoKnowsTheme(darkTheme = settingsViewModel.isDarkTheme.observeAsState().value == true) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            content = {
                Column(
                    modifier = Modifier
                        .paint(
                            // Replace with your image id
                            painterResource(
                                id = if (settingsViewModel.isDarkTheme.observeAsState().value == true) R.drawable.puzzle_bg_black else R.drawable.puzzle_bg_white
                            ),
                            contentScale = ContentScale.Crop
                        )
                        .fillMaxSize()
                        .padding(
                            start = if (getSurfaceRotation() == 1) WindowInsets.displayCutout
                                .asPaddingValues()
                                .calculateStartPadding(getLayoutDirection()) else if (getSurfaceRotation() == 3) WindowInsets.displayCutout
                                .asPaddingValues()
                                .calculateEndPadding(getLayoutDirection()) else 0.dp,
                            end = if (getSurfaceRotation() == 1) WindowInsets.displayCutout
                                .asPaddingValues()
                                .calculateStartPadding(getLayoutDirection()) else if (getSurfaceRotation() == 3) WindowInsets.displayCutout
                                .asPaddingValues()
                                .calculateEndPadding(getLayoutDirection()) else 0.dp
                        )
                        .statusBarsPadding()
                        .navigationBarsPadding()
                ) {
                    //TOP APP BAR
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        TopBar(
                            navController = navController,
                            onLeftBtnClick = {
                                if (showGameDetails == true) {
                                    statsViewModel.setShowGameDetails(false)
                                    statsViewModel.setGameQuestionsReady(false)
                                } else {
                                    navController.navigate("home")
                                }
                            },
                            leftBtnIcon = Icons.AutoMirrored.Filled.ArrowBack,
                            showTitle = true,
                            title = context.getString(R.string.app_name),
                            showRightButton = !showGameDetails!!,
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

                    //STATS PAGE
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        StatsPage(games, statsViewModel, settingsViewModel)
                    }
                }
            }
        )
    }
}

@Composable
fun StatsPage(
    games: List<Game>?,
    statsViewModel: StatsViewModel,
    settingsViewModel: SettingsViewModel
) {
    val gameQuestionReady = statsViewModel.gameQuestionsReady.observeAsState().value
    val selectedGame = statsViewModel.selectedGame.observeAsState().value

    val isLandscape = isLandscape()

    if (statsViewModel.showGameDetails.value == false && gameQuestionReady == false) {
        //MAIN BOX
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = if (isLandscape) 0.dp else 15.dp,
                    end = if (isLandscape) 0.dp else 15.dp,
                    bottom = if (isLandscape) 0.dp else 15.dp
                )
        ) {
            //Colonna portante che racchiude tutta la schermata
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                //HEADER
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (isLandscape) header_height_landscape else header_height)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                        )
                ) {
                    StatsHeader(settingsViewModel)
                }

                //Al suo interno usa una LazyColumn per mostrare i game
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                            RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                        )

                ) {
                    ShowGames(games, statsViewModel, settingsViewModel)
                }
            }
        }
    } else if (statsViewModel.showGameDetails.value == true && gameQuestionReady == false) {
        LoadingScreen()
    } else if (statsViewModel.showGameDetails.value == true && gameQuestionReady == true) {
        GameDetails(selectedGame!!, statsViewModel, settingsViewModel)
    }
}

@Composable
fun ShowGames(
    games: List<Game>?,
    statsViewModel: StatsViewModel,
    settingsViewModel: SettingsViewModel
) {
    if (games != null) {
        LazyColumn {
            items(games) { game ->
                StatsRow(game, statsViewModel, settingsViewModel)
            }
        }
    }
}

@Composable
fun StatsHeader(settingsViewModel: SettingsViewModel) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
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
                style = buttonsTextStyle,
                color = if (settingsViewModel.isDarkTheme.observeAsState().value == true) MaterialTheme.colorScheme.onPrimary else Color.Black
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
                style = buttonsTextStyle,
                color = if (settingsViewModel.isDarkTheme.observeAsState().value == true) MaterialTheme.colorScheme.onPrimary else Color.Black
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
                style = buttonsTextStyle,
                color = if (settingsViewModel.isDarkTheme.observeAsState().value == true) MaterialTheme.colorScheme.onPrimary else Color.Black
            )
        }
    }
}

@Composable
fun StatsRow(game: Game, statsViewModel: StatsViewModel, settingsViewModel: SettingsViewModel) {
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
                statsViewModel.setShowGameDetails(true)
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
                style = rowButtonTextStyle,
                fontSize = fontSizeMedium
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
            when (game.difficulty) {
                "Easy" -> {
                    Icon(
                        Icons.Default.Star,
                        tint = if (settingsViewModel.isDarkTheme.observeAsState().value == true) MaterialTheme.colorScheme.onPrimary else Color.Black,
                        contentDescription = null,
                        modifier = Modifier
                            .size(star_icon_size)
                            .fillMaxSize()
                    )
                }

                "Medium" -> {
                    Row {
                        for (i in 1..2) {
                            Icon(
                                Icons.Default.Star,
                                tint = if (settingsViewModel.isDarkTheme.observeAsState().value == true) MaterialTheme.colorScheme.onPrimary else Color.Black,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(star_icon_size)
                                    .fillMaxSize()
                            )
                        }
                    }
                }

                "Hard" -> {
                    Row {
                        for (i in 1..3) {
                            Icon(
                                Icons.Default.Star,
                                tint = if (settingsViewModel.isDarkTheme.observeAsState().value == true) MaterialTheme.colorScheme.onPrimary else Color.Black,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(star_icon_size)
                                    .fillMaxSize()
                            )
                        }
                    }
                }

                else -> {
                    Icon(
                        Icons.Default.Shuffle,
                        tint = if (settingsViewModel.isDarkTheme.observeAsState().value == true) MaterialTheme.colorScheme.onPrimary else Color.Black,
                        contentDescription = null,
                        modifier = Modifier
                            .size(star_icon_size)
                            .fillMaxSize()
                    )
                    /*Text(
                        text = "Mixed",
                        style = rowButtonTextStyle,
                        fontSize = fontSizeMedium
                    )*/
                }
            }
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
                style = rowButtonTextStyle,
                fontSize = fontSizeNormal,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun GameDetails(
    game: Game,
    statsViewModel: StatsViewModel,
    settingsViewModel: SettingsViewModel
) {
    val isLandscape = isLandscape()

    if (isLandscape) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            LazyColumn {
                item {
                    //HEADER
                    GameDetailsHeader()

                    //GAME DETAILS
                    GameDetailsBox(game, settingsViewModel)
                }

                item {
                    //QUESTIONS BOX
                    QuestionsBox(statsViewModel)
                }
            }

            /*

            //DETAILS BOX
            DetailsBox(game, settingsViewModel)

            //QUESTIONS BOX
            QuestionsBox(statsViewModel)

             */

        }

    } else {
        //Colonna portante che racchiude tutta la schermata dei dettagli del gioco
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            //DETAILS BOX
            DetailsBox(game, settingsViewModel)

            //QUESTIONS BOX
            QuestionsBox(statsViewModel)
        }
    }
}

@Composable
fun DetailsBox(game: Game, settingsViewModel: SettingsViewModel) {
    val isLandScape = isLandscape()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = if (isLandScape) 100.dp else 15.dp,
                end = if (isLandScape) 100.dp else 15.dp,
                top = 10.dp
            )
    ) {
        Column {
            //HEADER
            GameDetailsHeader()

            //GAME DETAILS
            GameDetailsBox(game, settingsViewModel)
        }
    }
}

@Composable
fun GameDetailsHeader() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(gameheader_height)
            .background(
                MaterialTheme.colorScheme.primary,
                RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
            )
    ) {
        Text(
            text = "Game details",
            style = buttonsTextStyle,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}


@Composable
fun GameDetailsBox(game: Game, settingsViewModel: SettingsViewModel) {
    val isLandScape = isLandscape()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isLandScape) 80.dp else 170.dp)
            .background(
                MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isLandScape) {
            Row {
                //Score column
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(0.25F)
                        .fillMaxSize()
                ) {
                    Text(
                        text = "Score: ${game.score}",
                        style = buttonsTextStyle,
                        textAlign = TextAlign.Center,
                        fontSize = fontSizeUpperNormal
                    )
                }


                //Duration column
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(0.25F)
                        .fillMaxSize()
                ) {
                    Text(
                        text = "Duration: ${game.duration}",
                        style = buttonsTextStyle,
                        textAlign = TextAlign.Center,
                        fontSize = fontSizeUpperNormal
                    )
                }

                //Date column
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(0.25F)
                        .fillMaxSize()
                ) {
                    val date = game.date
                    val dateParts = date.split(" ")

                    Text(
                        text = "${dateParts[0]}\n${dateParts[1]}",
                        style = buttonsTextStyle,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        fontSize = fontSizeUpperNormal
                    )
                }

                //Difficulty column
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(0.25F)
                        .fillMaxSize()
                ) {
                    when (game.difficulty) {
                        "easy" -> {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Default.Star,
                                    tint = if (settingsViewModel.isDarkTheme.observeAsState().value == true) MaterialTheme.colorScheme.onPrimary else Color.Black,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(difficulty_icon_size)
                                )

                                Spacer(modifier = Modifier.size(5.dp))

                                Text(
                                    text = "Easy",
                                    fontSize = fontSizeUpperNormal
                                )
                            }
                        }

                        "medium" -> {

                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Row {
                                    for (i in 1..2) {
                                        Icon(
                                            Icons.Default.Star,
                                            tint = if (settingsViewModel.isDarkTheme.observeAsState().value == true) MaterialTheme.colorScheme.onPrimary else Color.Black,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(difficulty_icon_size)
                                                .fillMaxSize()
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.size(5.dp))

                                Text(
                                    text = "Medium",
                                    fontSize = fontSizeUpperNormal
                                )
                            }
                        }

                        "hard" -> {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Row {
                                    for (i in 1..3) {
                                        Icon(
                                            Icons.Default.Star,
                                            tint = if (settingsViewModel.isDarkTheme.observeAsState().value == true) MaterialTheme.colorScheme.onPrimary else Color.Black,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(difficulty_icon_size)
                                                .fillMaxSize()
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.size(5.dp))

                                Text(
                                    text = "Hard",
                                    fontSize = fontSizeUpperNormal
                                )
                            }

                        }

                        else -> {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Icon(
                                    Icons.Default.Shuffle,
                                    tint = if (settingsViewModel.isDarkTheme.observeAsState().value == true) MaterialTheme.colorScheme.onPrimary else Color.Black,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(difficulty_icon_size)
                                        .fillMaxSize()
                                )

                                Spacer(modifier = Modifier.size(5.dp))

                                Text(
                                    text = "Mixed",
                                    fontSize = fontSizeUpperNormal
                                )
                            }
                        }
                    }
                }


            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //First row
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(0.5f),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "Score: ${game.score}",
                            style = buttonsTextStyle,
                            modifier = Modifier
                                .weight(0.5F),
                            textAlign = TextAlign.Center,
                            fontSize = fontSizeUpperNormal
                        )

                        Text(
                            text = "Duration: ${game.duration}",
                            style = buttonsTextStyle,
                            modifier = Modifier
                                .weight(0.5F),
                            textAlign = TextAlign.Center,
                            fontSize = fontSizeUpperNormal
                        )
                    }
                }

                //Secondo row
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        //Date column
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(0.5F)
                                .fillMaxSize()
                        ) {
                            val date = game.date
                            val dateParts = date.split(" ")

                            Text(
                                text = "${dateParts[0]}\n${dateParts[1]}",
                                style = buttonsTextStyle,
                                textAlign = TextAlign.Center,
                                maxLines = 2,
                                fontSize = fontSizeUpperNormal
                            )
                        }

                        //Difficulty column
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(0.5F)
                                .fillMaxSize()
                        ) {
                            when (game.difficulty) {
                                "easy" -> {
                                    Row(
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Icon(
                                            Icons.Default.Star,
                                            tint = if (settingsViewModel.isDarkTheme.observeAsState().value == true) MaterialTheme.colorScheme.onPrimary else Color.Black,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(difficulty_icon_size)
                                        )

                                        Spacer(modifier = Modifier.size(15.dp))

                                        Text(
                                            text = "Easy",
                                            fontSize = fontSizeUpperNormal
                                        )
                                    }
                                }

                                "medium" -> {

                                    Row(
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {

                                        Row {
                                            for (i in 1..2) {
                                                Icon(
                                                    Icons.Default.Star,
                                                    tint = if (settingsViewModel.isDarkTheme.observeAsState().value == true) MaterialTheme.colorScheme.onPrimary else Color.Black,
                                                    contentDescription = null,
                                                    modifier = Modifier
                                                        .size(difficulty_icon_size)
                                                        .fillMaxSize()
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.size(15.dp))

                                        Text(
                                            text = "Medium",
                                            fontSize = fontSizeUpperNormal
                                        )
                                    }
                                }

                                "hard" -> {
                                    Row(
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {

                                        Row {
                                            for (i in 1..3) {
                                                Icon(
                                                    Icons.Default.Star,
                                                    tint = if (settingsViewModel.isDarkTheme.observeAsState().value == true) MaterialTheme.colorScheme.onPrimary else Color.Black,
                                                    contentDescription = null,
                                                    modifier = Modifier
                                                        .size(difficulty_icon_size)
                                                        .fillMaxSize()
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.size(15.dp))

                                        Text(
                                            text = "Hard",
                                            fontSize = fontSizeUpperNormal
                                        )
                                    }

                                }

                                else -> {
                                    Row(
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Icon(
                                            Icons.Default.Shuffle,
                                            tint = if (settingsViewModel.isDarkTheme.observeAsState().value == true) MaterialTheme.colorScheme.onPrimary else Color.Black,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(difficulty_icon_size)
                                                .fillMaxSize()
                                        )

                                        Spacer(modifier = Modifier.size(15.dp))

                                        Text(
                                            text = "Mixed",
                                            fontSize = fontSizeUpperNormal
                                        )

                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuestionsBox(statsViewModel: StatsViewModel) {
    val isLandScape = isLandscape()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = if (isLandScape) 0.dp else 15.dp,
                end = if (isLandScape) 0.dp else 15.dp,
                top = 20.dp,
                bottom = 20.dp
            )
    ) {
        Column {
            QuestionsHeader()

            QuestionsList(statsViewModel)
        }
    }
}

@Composable
fun QuestionsHeader() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(gameheader_height)
            .background(
                MaterialTheme.colorScheme.primary,
                RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
            )
    ) {
        Text(
            text = "Questions",
            style = buttonsTextStyle,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun QuestionsList(statsViewModel: StatsViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
            )
    ) {
        if (isLandscape()) {

            Column{
                with(statsViewModel) {
                    for (i in 0 until selectedGameQuestions.value!!.size) {
                        val question = selectedGameQuestions.value!![i]

                        Box(
                            modifier = Modifier
                                .padding(
                                    start = 10.dp,
                                    end = 10.dp,
                                    top = 5.dp,
                                )
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = question.question,
                                    modifier = Modifier.weight(1f)
                                )

                                Spacer(modifier = Modifier.size(10.dp))

                                if (question.givenAnswer == question.correct_answer) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Correct",
                                        tint = Color.Green
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Incorrect",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    }
                }
            }

        } else {
            LazyColumn {
                with(statsViewModel) {
                    items(selectedGameQuestions.value!!.size) { index ->

                        val question = selectedGameQuestions.value!![index]

                        Box(
                            modifier = Modifier
                                .padding(
                                    start = 10.dp,
                                    end = 10.dp,
                                    top = 5.dp,
                                )
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = question.question,
                                    modifier = Modifier.weight(1f)
                                )

                                Spacer(modifier = Modifier.size(10.dp))

                                if (question.givenAnswer == question.correct_answer) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Correct",
                                        tint = Color.Green
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Incorrect",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}


