package it.scvnsc.whoknows.ui.screens.views

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Hardware
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SportsScore
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.scvnsc.whoknows.R
import it.scvnsc.whoknows.ui.screens.components.TopBar
import it.scvnsc.whoknows.ui.theme.WhoKnowsTheme
import it.scvnsc.whoknows.ui.theme.buttonsTextStyle
import it.scvnsc.whoknows.ui.theme.gameButtonsTextStyle
import it.scvnsc.whoknows.ui.theme.gameQuestionTextStyle
import it.scvnsc.whoknows.ui.theme.gameScoreTextStyle
import it.scvnsc.whoknows.ui.theme.gameTimerTextStyle
import it.scvnsc.whoknows.ui.theme.game_buttons_height
import it.scvnsc.whoknows.ui.theme.game_buttons_padding_top
import it.scvnsc.whoknows.ui.theme.game_buttons_shape
import it.scvnsc.whoknows.ui.theme.game_buttons_spacing
import it.scvnsc.whoknows.ui.theme.home_buttons_height
import it.scvnsc.whoknows.ui.theme.home_buttons_shape
import it.scvnsc.whoknows.ui.theme.home_buttons_width
import it.scvnsc.whoknows.ui.theme.medium_padding
import it.scvnsc.whoknows.ui.theme.small_padding
import it.scvnsc.whoknows.ui.viewmodels.GameViewModel
import it.scvnsc.whoknows.ui.viewmodels.SettingsViewModel
import it.scvnsc.whoknows.utils.DifficultyType
import it.scvnsc.whoknows.utils.isLandscape
import kotlinx.coroutines.delay

//TODO: inizializzare un timer dopo la chiamata API per le domande della durata di 5 secondi che modifica una variabile booleana da false a true (è possibile effettuare una nuova chiamata all'API delle domande)
// initial = true, poi diventa false passano 5 secondi e ritorna true (_canMakeNewAPICalls: LiveData<Boolean>)
// se l'utente prova ad iniziare una nuova partita mentre la variabile è false allora mostra un'animazione di caricamento fino all'aggiornamento della variabile

//TODO: se il giocatore perde la partita viene mostrato un dialog con due pulsanti: "Play again", "Change game settings"

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GameView(
    navController: NavController,
    gameViewModel: GameViewModel,
    settingsViewModel: SettingsViewModel
) {
    WhoKnowsTheme(darkTheme = settingsViewModel.isDarkTheme.observeAsState().value == true) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            //TODO: capire come implementare il logo come immagine nella home e nelle schermate
            content = {
                with(gameViewModel) {

                    if (isPlaying.observeAsState().value == false && isGameFinished.observeAsState().value == true) {
                        //TODO:: da terminare
                    }

                    if (isPlaying.observeAsState().value == false) {
                        GameViewMainPage(navController, gameViewModel, settingsViewModel)
                    }

                    if (isPlaying.observeAsState().value == true) {
                        GameViewInGame(navController, gameViewModel, settingsViewModel)
                    }
                }
            }
        )
    }
}

@Composable
fun GameViewInGame(
    navController: NavController,
    gameViewModel: GameViewModel,
    settingsViewModel: SettingsViewModel
) {
    //determino orientamento schermo
    val isLandscape = isLandscape()
    val context = LocalContext.current

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
                //TODO:: da rimuovere quella generica
                // GameTopBar(navController, gameViewModel, settingsViewModel)
                TopBar(
                    navController = navController,
                    onClick = {
                        navController.navigate("home")
                        //gameViewModel.onQuitGame()
                    },
                    navIcon = Icons.Default.Close,
                    showTitle = true,
                    title = context.getString(R.string.app_name),
                    settingsViewModel = settingsViewModel,
                    gameViewModel = gameViewModel
                )

            }

            Spacer(modifier = Modifier.size(medium_padding))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                GameBox(gameViewModel)
            }
        }
    }
}

@Composable
fun GameBox(gameViewModel: GameViewModel) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            //Game timer box
            Box(
                modifier = Modifier
                    .weight(0.5f)
                    .align(Alignment.CenterVertically)
                    .padding(start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
            ) {
                //Timer nella UI
                GameTimer(gameViewModel)
            }

            //Game score box
            Box(
                modifier = Modifier
                    .weight(0.5f)
                    .align(Alignment.CenterVertically)
                    .padding(end = 10.dp)
            ) {
                //Punteggio nella UI
                GameScore(gameViewModel)
            }
        }

        Spacer(modifier = Modifier.size(20.dp))

        //Categoria
        //TODO:: da sistemare
        //ShowCategory(gameViewModel)

        //Difficoltà
        ShowDifficulty(gameViewModel)

        Spacer(modifier = Modifier.size(20.dp))

        QuestionBox(gameViewModel)
    }
}

/*
@Composable
fun GameTopBar(
    navController: NavController,
    gameViewModel: GameViewModel,
    settingsViewModel: SettingsViewModel
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .padding(top = 35.dp)
            .fillMaxWidth()
            .height(90.dp)
            .border(1.dp, Color.Red),
        verticalAlignment = Alignment.Top
    ) {
        //Quit game button
        //TODO:: fare Quit Game button
        Box(
            modifier = Modifier
                .weight(0.2F)
                .fillMaxSize()
                .border(1.dp, Color.Blue)
        ) {
            IconButton(
                onClick = {
                    //DA CAMBIARE
                    navController.navigate("game")
                },
                colors = IconButtonDefaults.iconButtonColors(DarkYellow),
                modifier = Modifier
                    .align(Alignment.Center)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        //App title
        Box(
            modifier = Modifier
                .weight(0.6F)
                .fillMaxSize()
                .border(1.dp, Color.Blue)
        ) {
            Text(
                text = context.getString(R.string.app_name),
                style = topBarTextStyle,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }

        //Theme (dark/light switch button)
        Box(
            modifier = Modifier
                .weight(0.2F)
                .fillMaxSize()
                .border(1.dp, Color.Magenta)
        ) {
            with(settingsViewModel) {
                IconButton(
                    onClick = { toggleDarkTheme() },
                    colors = IconButtonDefaults.iconButtonColors(DarkYellow),
                    modifier = Modifier
                        .align(Alignment.Center)
                ) {
                    if (isDarkTheme.value == true) {
                        Icon(
                            Icons.Filled.DarkMode,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Icon(
                            Icons.Filled.WbSunny,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}
*/

@Composable
fun GameTimer(gameViewModel: GameViewModel) {
    val timer = gameViewModel.elapsedTime.observeAsState().value

    Button(
        onClick = { /* do nothing */ },
        modifier = Modifier
            .fillMaxWidth(),
        enabled = false,
        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp, 0.dp, 5.dp),
        shape = CircleShape,
        content = {
            Row(
                modifier = Modifier
                    .padding(small_padding)
            ) {
                Icon(
                    Icons.Default.Timer,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .fillMaxSize()
                        .padding(end = 10.dp)
                )

                Text(
                    "Time:\n$timer",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = gameTimerTextStyle,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        },
        colors = ButtonDefaults.buttonColors(
            Color.Transparent,
            Color.Transparent,
            MaterialTheme.colorScheme.primary,
            Color.Transparent
        )
    )
}

@Composable
fun GameScore(gameViewModel: GameViewModel) {
    val currentScore = gameViewModel.score.observeAsState().value

    Button(
        onClick = { /* do nothing */ },
        modifier = Modifier
            .fillMaxWidth(),
        enabled = false,
        shape = CircleShape,
        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp, 0.dp, 5.dp),
        content = {
            Row(
                modifier = Modifier
                    .padding(small_padding)
            ) {
                Icon(
                    Icons.Default.SportsScore,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .fillMaxSize()
                        .padding(end = 10.dp)
                )

                Text(
                    "Score:\n$currentScore",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = gameScoreTextStyle,
                    textAlign = TextAlign.Right,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        },
        colors = ButtonDefaults.buttonColors(
            Color.Transparent,
            Color.Transparent,
            MaterialTheme.colorScheme.primary,
            Color.Transparent
        )
    )
}

@Composable
fun ShowDifficulty(gameViewModel: GameViewModel) {
    Button(
        onClick = { /* do nothing */ },
        modifier = Modifier
            .padding(start = 80.dp, end = 80.dp)
            .fillMaxWidth(),
        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp, 0.dp, 5.dp),
        enabled = false,
        content = {
            when (gameViewModel.questionForUser.observeAsState().value?.difficulty) {
                "easy" -> {
                    Icon(
                        Icons.Default.Star,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
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
                                    .size(50.dp)
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
                                    .size(50.dp)
                                    .fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    )
}

//TODO:: da completare
@Composable
fun ShowCategory(gameViewModel: GameViewModel) {
    Text("Category: " + (gameViewModel.questionForUser.observeAsState().value?.category ?: ""))
}

@Composable
fun QuestionBox(gameViewModel: GameViewModel) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp)
    ) {
        //Domanda nella UI
        ShowQuestion(gameViewModel)

        //Possibili risposte nella UI
        ShowAnswers(gameViewModel)
    }

}

@Composable
fun ShowQuestion(gameViewModel: GameViewModel) {
    Log.d("Debug", "Question for user: ${gameViewModel.questionForUser.observeAsState().value}")

    Text(
        text = gameViewModel.questionForUser.observeAsState().value?.question ?: "",
        style = gameQuestionTextStyle,
        textAlign = TextAlign.Center
    )
}

@Composable
fun ShowAnswers(gvm: GameViewModel) {
    val question = gvm.questionForUser.observeAsState().value
    val answers = gvm.shuffledAnswers.observeAsState().value

    Column(
        verticalArrangement = Arrangement.spacedBy(game_buttons_spacing),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = game_buttons_padding_top)
    ) {
        for (ans in answers!!) {
            AnswerButton(
                answerText = ans,
                //isCorrect = question?.correct_answer == ans,
                gvm = gvm
            )
//            Button(
//                onClick = {
//                    gvm.onAnswerClicked(ans)
//                },
//                elevation = ButtonDefaults.buttonElevation(5.dp, 0.dp, 0.dp, 0.dp, 20.dp),
//                shape = RoundedCornerShape(game_buttons_shape),
//                modifier = Modifier
//                    //.padding(start = 20.dp, end = 20.dp)
//                    .fillMaxWidth()
//                    .height(game_buttons_height)
//            ) {
//                Text(
//                    text = ans,
//                    style = gameButtonsTextStyle,
//                    textAlign = TextAlign.Center
//                )
//            }
        }
    }
}

@Composable
fun AnswerButton(
    answerText: String,
    //isCorrect: Boolean,
    gvm: GameViewModel
) {
    val isCorrect = gvm.isAnswerCorrect.observeAsState().value
    val isClicked = gvm.isAnswerSelected.observeAsState().value

    //TODO: Vedere se si può fare qualcosa di più carino animando invece di cambiare direttamente colore
    val backgroundColor = when {
        !isClicked!! && !isCorrect!! -> MaterialTheme.colorScheme.primary
        isClicked && !isCorrect!! -> Color.Red
        else -> Color.Green
    }

    Log.d("Debug", "I am button $answerText, isCorrect: $isCorrect")

    Button(
        elevation = ButtonDefaults.buttonElevation(5.dp, 0.dp, 0.dp, 0.dp, 20.dp),
        shape = RoundedCornerShape(game_buttons_shape),
        modifier = Modifier
            //.padding(start = 20.dp, end = 20.dp)
            .fillMaxWidth()
            .height(game_buttons_height),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        onClick = { gvm.onAnswerClicked(answerText) },
    ) {
        Text(
            text = answerText,
            style = gameButtonsTextStyle,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun GameViewMainPage(
    navController: NavController,
    gameViewModel: GameViewModel,
    settingsViewModel: SettingsViewModel
) {
    //determino orientamento schermo
    val isLandscape = isLandscape()

    if (isLandscape) {
        //TODO:: da sistemare
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    // Replace with your image id
                    painterResource(
                        id = if (settingsViewModel.isDarkTheme.observeAsState().value == true) R.drawable.dark_background_pattern else R.drawable.light_background_pattern
                    ),
                    contentScale = ContentScale.Crop
                )

        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                //TODO:: da rimuovere quella generica
                // MainPageTopBar(navController, settingsViewModel)
                TopBar(
                    navController = navController,
                    onClick = { navController.navigate("home") },
                    navIcon = Icons.AutoMirrored.Filled.ArrowBack,
                    showTitle = false,
                    settingsViewModel = settingsViewModel,
                    gameViewModel = null
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                MainPageButtons(gameViewModel)
            }
        }
    }
}

@Composable
fun MainPageButtons(
    gameViewModel: GameViewModel
) {
    val context = LocalContext.current

    var showDifficultySelectionDialog by rememberSaveable { mutableStateOf(false) }
    var showCategorySelectionDialog by rememberSaveable { mutableStateOf(false) }

    var selectedDifficulty by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.spacedBy(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp, bottom = 100.dp)
    ) {
        //Difficulty Selection Dialog
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

        //Category selection dialog
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

        //App title
        AppTitle(context)

        //Start game button
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
                    style = buttonsTextStyle
                )
            }

        }

        //Choose difficulty button
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

        //Choose category button
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

/*
@Composable
fun MainPageTopBar(navController: NavController, settingsViewModel: SettingsViewModel) {
    //Top app bar
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        //Go back button
        Box(
            modifier = Modifier
                .weight(0.2F)
                .fillMaxSize()
        ) {
            IconButton(
                onClick = { navController.navigate("home") },
                colors = IconButtonDefaults.iconButtonColors(DarkYellow),
                modifier = Modifier
                    .align(Alignment.Center)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Spacer(
            modifier = Modifier
                .weight(0.6F)
                .fillMaxSize()
        )

        //Theme (dark/light switch button)
        Box(
            modifier = Modifier
                .weight(0.2F)
                .fillMaxSize()
        ) {
            with(settingsViewModel) {
                IconButton(
                    onClick = { toggleDarkTheme() },
                    colors = androidx.compose.material3.IconButtonDefaults.iconButtonColors(
                        it.scvnsc.whoknows.ui.theme.DarkYellow
                    ),
                    modifier = androidx.compose.ui.Modifier
                        .align(androidx.compose.ui.Alignment.Center)
                ) {
                    if (isDarkTheme.value == true) {
                        Icon(
                            androidx.compose.material.icons.Icons.Filled.DarkMode,
                            contentDescription = null,
                            tint = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Icon(
                            androidx.compose.material.icons.Icons.Filled.WbSunny,
                            contentDescription = null,
                            tint = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}
 */


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
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (diff
                                    .toString()
                                    .lowercase() == gameViewModel.selectedDifficulty.value),
                                onClick = {
                                    onDifficultySelected(diff.toString())
                                    radioSelected = diff
                                        .toString()
                                        .lowercase()
                                }
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            onClick = {
                                onDifficultySelected(diff.toString())
                                radioSelected = diff.toString().lowercase()
                                Log.d(
                                    "Debug",
                                    "Selected category: ${diff.toString().lowercase()}"
                                )
                            },
                            selected = (diff.toString().lowercase() == radioSelected),
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



