package it.scvnsc.whoknows.ui.screens.views

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import it.scvnsc.whoknows.R
import it.scvnsc.whoknows.ui.screens.components.TopBar
import it.scvnsc.whoknows.ui.theme.WhoKnowsTheme
import it.scvnsc.whoknows.ui.theme.bottom_bar_padding
import it.scvnsc.whoknows.ui.theme.buttonsTextStyle
import it.scvnsc.whoknows.ui.theme.default_elevation
import it.scvnsc.whoknows.ui.theme.disabled_elevation
import it.scvnsc.whoknows.ui.theme.gameButtonsTextStyle
import it.scvnsc.whoknows.ui.theme.gameQuestionTextStyle
import it.scvnsc.whoknows.ui.theme.gameScoreTextStyle
import it.scvnsc.whoknows.ui.theme.gameTimerTextStyle
import it.scvnsc.whoknows.ui.theme.game_buttons_height
import it.scvnsc.whoknows.ui.theme.game_buttons_shape
import it.scvnsc.whoknows.ui.theme.game_buttons_spacing
import it.scvnsc.whoknows.ui.theme.home_buttons_height
import it.scvnsc.whoknows.ui.theme.home_buttons_shape
import it.scvnsc.whoknows.ui.theme.home_buttons_width
import it.scvnsc.whoknows.ui.theme.padding_difficulty
import it.scvnsc.whoknows.ui.theme.pressed_elevation
import it.scvnsc.whoknows.ui.theme.small_padding
import it.scvnsc.whoknows.ui.theme.star_icon_size
import it.scvnsc.whoknows.ui.viewmodels.GameViewModel
import it.scvnsc.whoknows.ui.viewmodels.SettingsViewModel
import it.scvnsc.whoknows.utils.DifficultyType
import it.scvnsc.whoknows.utils.isLandscape
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

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
            content = {
                with(gameViewModel) {

                    if (isPlaying.observeAsState().value == false && isGameOver.observeAsState().value == true) {
                        //TODO:: da terminare
                    }

                    /*if (isGameTimerInterrupted.observeAsState().value==true){
                        LoadingScreen()
                    }*/

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
    val showLoading = gameViewModel.isGameTimerInterrupted.observeAsState().value

    if (isLandscape) {
        //TODO:: da sistemare
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = bottom_bar_padding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TopBar(
                    navController = navController,
                    onLeftBtnClick = {
                        navController.navigate("home")
                        //gameViewModel.onQuitGame()
                    },
                    leftBtnIcon = Icons.Default.Close,
                    showTitle = true,
                    title = context.getString(R.string.app_name),
                    settingsViewModel = settingsViewModel
                )

            }

            Spacer(modifier = Modifier.size(small_padding))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (showLoading == true) {
                    LoadingScreen()
                } else {
                    GameBox(gameViewModel)
                }
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(120.dp),
            strokeWidth = 7.dp
        )
    }

}

@Composable
fun GameBox(gameViewModel: GameViewModel) {
    val gameOver = gameViewModel.isGameOver.observeAsState().value

    if (gameOver == true) {
        GameOverScreen(gameOver)
    } else {
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

            Spacer(modifier = Modifier.size(10.dp))

            ShowDifficulty(gameViewModel)

            Spacer(modifier = Modifier.size(10.dp))

            QuestionBox(gameViewModel)
        }
    }


}

@Composable
fun GameOverScreen(isVisible: Boolean) {
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(
                initialOffsetY = { -it }
            ),
            exit = slideOutVertically(
                targetOffsetY = {
                    -it
                }
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Game Over!", fontSize = 32.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { /* Nuovo gioco */ }) {
                    Text(text = "Nuovo gioco")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { /* Menu principale */ }) {
                    Text(text = "Menu principale")
                }
            }
        }
    }
}

@Composable
fun GameTimer(gameViewModel: GameViewModel) {
    val timer = gameViewModel.elapsedTime.observeAsState().value

    Button(
        onClick = { /* do nothing */ },
        modifier = Modifier
            .fillMaxWidth(),
        enabled = false,
        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp, 0.dp, disabled_elevation),
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
        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp, 0.dp, disabled_elevation),
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
            .padding(start = padding_difficulty, end = padding_difficulty)
            .fillMaxWidth(),
        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp, 0.dp, disabled_elevation),
        enabled = false,
        content = {
            when (gameViewModel.questionForUser.observeAsState().value?.difficulty) {
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            //Domanda nella UI
            ShowQuestion(gameViewModel)
        }

        Spacer(modifier = Modifier.size(30.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            //Possibili risposte nella UI
            ShowAnswers(gameViewModel)
        }
    }
}

@Composable
fun ShowQuestion(gameViewModel: GameViewModel) {
    Log.d("Debug", "Question for user: ${gameViewModel.questionForUser.observeAsState().value}")
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = gameViewModel.questionForUser.observeAsState().value?.question ?: "",
            style = gameQuestionTextStyle
        )
    }
}

@Composable
fun ShowAnswers(gvm: GameViewModel) {
    val question = gvm.questionForUser.observeAsState().value
    val answers = gvm.shuffledAnswers.observeAsState().value
    val givenAnswer = gvm.userAnswer.observeAsState().value

    Column(
        verticalArrangement = Arrangement.spacedBy(game_buttons_spacing),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        for (ans in answers!!) {
            AnswerButton(
                answerText = ans,
                isCorrect = question?.correct_answer == ans,
                isSelected = givenAnswer == ans,
                gvm = gvm
            )
        }
    }
}

@Composable
fun AnswerButton(
    answerText: String,
    isCorrect: Boolean,
    isSelected: Boolean,
    gvm: GameViewModel
) {
    val backgroundColor = when {
        isSelected && isCorrect -> Color.Green
        isSelected && !isCorrect -> Color.Red
        else -> MaterialTheme.colorScheme.primary
    }

    Log.d("Debug", "I am button $answerText, isCorrect: $isCorrect")

    Button(
        elevation = ButtonDefaults.buttonElevation(default_elevation, pressed_elevation),
        shape = RoundedCornerShape(game_buttons_shape),
        modifier = Modifier
            .fillMaxWidth()
            .height(game_buttons_height),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        onClick = {
            gvm.onAnswerClicked(answerText)
        }
    ) {
        Text(
            text = answerText,
            style = gameButtonsTextStyle.copy(
                fontSize = when {
                    answerText.length <= 20 -> 18.sp
                    answerText.length <= 40 -> 16.sp
                    answerText.length <= 60 -> 14.sp
                    else -> 12.sp
                }
            ),
            textAlign = TextAlign.Center,
            maxLines = 2
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
    val showLoading  = gameViewModel.isGameTimerInterrupted.observeAsState().value
    if (isLandscape) {
        //TODO:: da sistemare
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    // Replace with your image id
                    painterResource(
                        id = if (settingsViewModel.isDarkTheme.observeAsState().value == true) R.drawable.puzzle_bg_black else R.drawable.puzzle_bg_white
                    ),
                    contentScale = ContentScale.Crop
                )

        ) {
            if (showLoading==true){
                LoadingScreen()
            }else{
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    TopBar(
                        navController = navController,
                        onLeftBtnClick = { navController.navigate("home") },
                        leftBtnIcon = Icons.AutoMirrored.Filled.ArrowBack,
                        showTitle = false,
                        showRightButton = true,
                        settingsViewModel = settingsViewModel
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
            elevation = ButtonDefaults.buttonElevation(default_elevation, pressed_elevation),
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
            elevation = ButtonDefaults.buttonElevation(default_elevation, pressed_elevation),
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
            elevation = ButtonDefaults.buttonElevation(default_elevation, pressed_elevation),
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
                    .height(200.dp)
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
                elevation = ButtonDefaults.buttonElevation(default_elevation, pressed_elevation),
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
                elevation = ButtonDefaults.buttonElevation(default_elevation, pressed_elevation),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ok")
            }
        }
    )
}



