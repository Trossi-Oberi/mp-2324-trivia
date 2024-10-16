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
import androidx.compose.material.icons.filled.WifiOff
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
import androidx.navigation.NavHostController
import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.toLowerCase
import it.scvnsc.whoknows.R
import it.scvnsc.whoknows.services.NetworkMonitorService
import it.scvnsc.whoknows.ui.screens.components.TopBar
import it.scvnsc.whoknows.ui.theme.WhoKnowsTheme
import it.scvnsc.whoknows.ui.theme.bottom_bar_padding
import it.scvnsc.whoknows.ui.theme.buttonsTextStyle
import it.scvnsc.whoknows.ui.theme.default_elevation
import it.scvnsc.whoknows.ui.theme.disabled_elevation
import it.scvnsc.whoknows.ui.theme.fontSizeBig
import it.scvnsc.whoknows.ui.theme.fontSizeMedium
import it.scvnsc.whoknows.ui.theme.fontSizeNormal
import it.scvnsc.whoknows.ui.theme.fontSizeUpperMedium
import it.scvnsc.whoknows.ui.theme.fontSizeUpperNormal
import it.scvnsc.whoknows.ui.theme.gameButtonsTextStyle
import it.scvnsc.whoknows.ui.theme.gameQuestionTextStyle
import it.scvnsc.whoknows.ui.theme.gameScoreTextStyle
import it.scvnsc.whoknows.ui.theme.gameTimerTextStyle
import it.scvnsc.whoknows.ui.theme.game_buttons_height
import it.scvnsc.whoknows.ui.theme.game_buttons_padding_landscape
import it.scvnsc.whoknows.ui.theme.game_buttons_shape
import it.scvnsc.whoknows.ui.theme.game_buttons_spacing
import it.scvnsc.whoknows.ui.theme.heart_icon_size
import it.scvnsc.whoknows.ui.theme.heart_icon_size_landscape
import it.scvnsc.whoknows.ui.theme.home_buttons_height
import it.scvnsc.whoknows.ui.theme.home_buttons_height_landscape
import it.scvnsc.whoknows.ui.theme.home_buttons_shape
import it.scvnsc.whoknows.ui.theme.home_buttons_width
import it.scvnsc.whoknows.ui.theme.home_buttons_width_landscape
import it.scvnsc.whoknows.ui.theme.medium_spacing_height
import it.scvnsc.whoknows.ui.theme.pressed_elevation
import it.scvnsc.whoknows.ui.theme.small_padding
import it.scvnsc.whoknows.ui.theme.star_icon_size
import it.scvnsc.whoknows.ui.theme.star_icon_size_landscape
import it.scvnsc.whoknows.ui.viewmodels.GameViewModel
import it.scvnsc.whoknows.ui.viewmodels.SettingsViewModel
import it.scvnsc.whoknows.utils.DifficultyType
import it.scvnsc.whoknows.utils.getLayoutDirection
import it.scvnsc.whoknows.utils.getSurfaceRotation
import it.scvnsc.whoknows.utils.isLandscape


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GameView(
    navController: NavHostController,
    gameViewModel: GameViewModel,
    settingsViewModel: SettingsViewModel
) {

    val isOffline = NetworkMonitorService.isOffline.observeAsState().value
    val isPlaying = gameViewModel.isPlaying.observeAsState().value
    val isApiSetupComplete = gameViewModel.isApiSetupComplete.observeAsState().value

    //per evitare che venga chiamato più volte il setupAPI e per evitare che il timer venga avviato senza motivo
    var hasCalledSetupAPI by rememberSaveable { mutableStateOf(false) }
    var wasOfflineBefore by rememberSaveable { mutableStateOf(false) }


    WhoKnowsTheme(darkTheme = settingsViewModel.isDarkTheme.observeAsState().value == true) {
        Scaffold(
            content = {
                when {
                    // Mostra la schermata di errore di rete
                    isOffline == true -> {
                        NetworkErrorScreen(navController, gameViewModel)
                        wasOfflineBefore = true
                    }

                    // Mostra la schermata principale (setup dell'API)
                    isPlaying == false && isApiSetupComplete == false -> {
                        if(!hasCalledSetupAPI) {
                            gameViewModel.setupAPI()
                            hasCalledSetupAPI = true
                        }

                        GameViewMainPage(navController, gameViewModel, settingsViewModel)
                    }

                    // Mostra la schermata in-game quando si sta giocando
                    isPlaying == true -> {
                        GameViewInGame(navController, gameViewModel, settingsViewModel)
                    }

                    else -> {
                        // Mostra la schermata di attesa/preparazione se non sta giocando
                        GameViewMainPage(navController, gameViewModel, settingsViewModel)
                    }
                }
            }
        )

        LaunchedEffect(key1 = NetworkMonitorService.isOffline.observeAsState().value) {
            if (isPlaying == true && isOffline == false && wasOfflineBefore) {
                gameViewModel.resumeTimer()
            }
        }

    }
}


@Composable
fun NetworkErrorScreen(navController: NavHostController, gameViewModel: GameViewModel) {
    Log.d("Debug", "NetworkErrorScreen called")

    val isPlaying = gameViewModel.isPlaying.observeAsState().value
    val isGameOver = gameViewModel.isGameOver.observeAsState().value

    var isTimerPaused by rememberSaveable { mutableStateOf(false) }

    // Stop timer quando la connessione viene persa
    if (isPlaying == true && isGameOver == false) {
        gameViewModel.pauseTimer()
        isTimerPaused = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = if (getSurfaceRotation() == 1) WindowInsets.displayCutout
                .asPaddingValues()
                .calculateStartPadding(getLayoutDirection()) else if (getSurfaceRotation()==3) WindowInsets.displayCutout
                .asPaddingValues().
                calculateEndPadding(getLayoutDirection()) else bottom_bar_padding,
                end = if (getSurfaceRotation() == 1) WindowInsets.displayCutout
                    .asPaddingValues()
                    .calculateStartPadding(getLayoutDirection()) else if (getSurfaceRotation()==3) WindowInsets.displayCutout
                    .asPaddingValues().
                    calculateEndPadding(getLayoutDirection()) else bottom_bar_padding
            )
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Icon(
                Icons.Filled.WifiOff,
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )

            Spacer(modifier = Modifier.size(small_padding))

            Text(
                text = "No internet connection",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.size(40.dp))

            if (isPlaying == true && isGameOver == false) {
                Text(
                    text = "Please wait for the connection to be restored, or press the button below to exit and save the game.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.size(40.dp))

                Button(
                    onClick = {
                        //chiudo la partita e torno alla home
                        gameViewModel.onQuitGameClicked()
                        navController.navigate("home")
                        gameViewModel.setIsPlaying(false)
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .height(60.dp),
                ) {
                    Text(
                        text = "Quit game",
                        style = buttonsTextStyle,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Text(
                    text = "Please wait for the connection to be restored to play a new game, or press the button below to go back to the main menu.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.size(40.dp))

                Button(
                    onClick = {
                        //resetto isGameOver in modo da non lasciare attiva la schermata di fine partita e isPlaying in modo da non far partire di nuovo la partita
                        navController.popBackStack()

                        if(isGameOver == true || isPlaying == true) {
                            gameViewModel.setIsPlaying(false)
                            gameViewModel.clearUserAnswer()
                            gameViewModel.setGameOver(false)
                        }
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .height(60.dp),
                ) {
                    Text(
                        text = "Go back",
                        style = buttonsTextStyle,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun GameViewInGame(
    navController: NavHostController,
    gameViewModel: GameViewModel,
    settingsViewModel: SettingsViewModel
) {
    //determino orientamento schermo
    val isLandscape = isLandscape()
    val context = LocalContext.current
    val showLoading = gameViewModel.isGameTimerInterrupted.observeAsState().value
    val isGameOver = gameViewModel.isGameOver.observeAsState().value
    val isPlaying  = gameViewModel.isPlaying.observeAsState().value

    val isDark = settingsViewModel.isDarkTheme.observeAsState().value == true
    val showExitConfirmationDialog = rememberSaveable { mutableStateOf(false) }

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

            .padding(start = if (getSurfaceRotation() == 1) WindowInsets.displayCutout
                .asPaddingValues()
                .calculateStartPadding(getLayoutDirection()) else if (getSurfaceRotation()==3) WindowInsets.displayCutout
                .asPaddingValues().
                calculateEndPadding(getLayoutDirection()) else 0.dp,
                end = if (getSurfaceRotation() == 1) WindowInsets.displayCutout
                    .asPaddingValues()
                    .calculateStartPadding(getLayoutDirection()) else if (getSurfaceRotation()==3) WindowInsets.displayCutout
                    .asPaddingValues().
                    calculateEndPadding(getLayoutDirection()) else 0.dp
            )
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TopBar(
                navController = navController,
                showLeftButton = !showLoading!!,
                onLeftBtnClick = {
                    if (isGameOver == false) {
                        showExitConfirmationDialog.value = true
                    } else {
                        navController.navigate("game")
                        gameViewModel.setIsPlaying(false)
                        gameViewModel.clearUserAnswer()
                        gameViewModel.setGameOver(false)
                    }
                },
                leftBtnIcon = if (isGameOver == false && isPlaying == true) Icons.Default.Close else Icons.AutoMirrored.Filled.ArrowBack,
                showTitle = true,
                title = context.getString(R.string.app_name),
                settingsViewModel = settingsViewModel
            )

        }

        Spacer(modifier = Modifier.size(10.dp) .fillMaxWidth())

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    /*start = if (isLandscape) 25.dp else 0.dp,
                    end = if (isLandscape) 25.dp else 0.dp,
                    bottom = 10.dp*/
                )
        ) {

            when (showLoading!!) {
                true -> {
                    LoadingScreen()
                }

                false -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        GameBox(gameViewModel, navController)
                    }
                }
            }
        }
    }

    if (showExitConfirmationDialog.value) {
        ExitConfirmationDialog(context, gameViewModel, isDark, showExitConfirmationDialog)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExitConfirmationDialog(
    context: Context,
    gameViewModel: GameViewModel,
    isDark: Boolean,
    showExitConfirmationDialog: MutableState<Boolean>
) {
    var isTimerPaused by rememberSaveable { mutableStateOf(false) }

    if(!isTimerPaused) {
        // Stop the timer during exit confirmation
        gameViewModel.pauseTimer()
        isTimerPaused = true
    }

    BasicAlertDialog(
        onDismissRequest = {
            //riavvio il timer
            gameViewModel.resumeTimer()
            isTimerPaused = false

            showExitConfirmationDialog.value = false
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .clip(RoundedCornerShape(16.dp)),
            color = if (isDark) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .padding(30.dp)
            ) {
                Text(
                    text = "Exit game",
                    style = MaterialTheme.typography.headlineSmall,
                    color = if (isDark) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Are you sure you want to exit the game?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isDark) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            //riavvio il timer
                            gameViewModel.resumeTimer()
                            isTimerPaused = false

                            showExitConfirmationDialog.value = false
                        }
                    ) {
                        Text("No")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            gameViewModel.onQuitGameClicked()

                            isTimerPaused = false

                            showExitConfirmationDialog.value = false

                            //mostra toast di uscita gioco avvenuta con successo
                            /*context.let { ctx ->
                                Toast.makeText(
                                    ctx,
                                    "Game exited successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }*/
                        }
                    ) {
                        Text("Yes")
                    }
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
fun GameBox(gameViewModel: GameViewModel, navController: NavHostController) {
    val gameOver = gameViewModel.isGameOver.observeAsState().value
    val isLandscape = isLandscape()

    AnimatedVisibility(
        visible = gameOver == true,
        enter = slideInVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            ),
            initialOffsetY = { -it }
        ),
        exit = fadeOut()
    ) {
        GameOverScreen(gameViewModel, navController)
    }

    AnimatedVisibility(visible = gameOver == false) {
        if (isLandscape) {
            GameBoxLandscape(gameViewModel)
        } else {
            GameBoxPortrait(gameViewModel)
        }
    }
}

@Composable
fun GameBoxLandscape(gameViewModel: GameViewModel) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                //.padding(start = bottom_bar_padding, end = bottom_bar_padding)
                .fillMaxWidth()
        ) {
            //Game timer box
            Box(
                modifier = Modifier
                    .weight(0.25f)
                    .align(Alignment.Top)
                //.padding(start = 10.dp, end = 10.dp)
            ) {
                //Timer nella UI
                GameTimer(gameViewModel)
            }

            //Game score box
            Box(
                modifier = Modifier
                    .weight(0.25f)
                    .align(Alignment.Top)
            ) {
                //Punteggio nella UI
                GameScore(gameViewModel)
            }

            //Difficulty Box
            Box(
                modifier = Modifier
                    .weight(0.25f)
                    .align(Alignment.Top)
            ) {
                DifficultyBox(gameViewModel)
            }

            //Lives Box
            Box(
                modifier = Modifier
                    .weight(0.25f)
                    .align(Alignment.Top)
            ) {
                LivesBox(gameViewModel)
            }
        }
        Spacer(modifier = Modifier.size(3.dp))
        QuestionBox(gameViewModel)
    }
}

@Composable
fun GameBoxPortrait(gameViewModel: GameViewModel) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(start = bottom_bar_padding, end = bottom_bar_padding)
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
            ) {
                //Timer nella UI
                GameTimer(gameViewModel)
            }

            //Game score box
            Box(
                modifier = Modifier
                    .weight(0.5f)
                    .align(Alignment.CenterVertically)
            ) {
                //Punteggio nella UI
                GameScore(gameViewModel)
            }
        }

        Spacer(modifier = Modifier.size(5.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            //Difficulty Box
            Box(
                modifier = Modifier
                    .weight(0.5f)
                    .align(Alignment.CenterVertically)
            ) {
                DifficultyBox(gameViewModel)
            }

            //Lives Box
            Box(
                modifier = Modifier
                    .weight(0.5f)
                    .align(Alignment.CenterVertically)
            ) {
                LivesBox(gameViewModel)
            }
        }

        Spacer(modifier = Modifier.size(20.dp))

        QuestionBox(gameViewModel)
    }
}

@Composable
fun GameTimer(gameViewModel: GameViewModel) {
    val timer = gameViewModel.elapsedTime.observeAsState().value
    val isLandscape = isLandscape()
    Button(
        onClick = { /* do nothing */ },
        modifier = Modifier
            .padding(start = game_buttons_padding_landscape, end = game_buttons_padding_landscape)
            .fillMaxWidth(),
        enabled = false,
        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp, 0.dp, disabled_elevation),
        shape = CircleShape,
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(all = if (isLandscape) 0.dp else small_padding)
            ) {
                Icon(
                    Icons.Default.Timer,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = null,
                    modifier = Modifier
                        .size(size = if (isLandscape) 38.dp else 50.dp)
                        .fillMaxSize()
                        .padding(end = 10.dp)
                )

                Text(
                    text = if (isLandscape) "Time: $timer" else "Time:\n$timer",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = gameTimerTextStyle,
                    fontSize = if (isLandscape) fontSizeNormal else fontSizeUpperNormal,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        },
        colors = ButtonColors(
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
    val isLandscape = isLandscape()

    Button(
        onClick = { /* do nothing */ },
        modifier = Modifier
            .padding(start = game_buttons_padding_landscape, end = game_buttons_padding_landscape)
            .fillMaxWidth(),
        enabled = false,
        shape = CircleShape,
        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp, 0.dp, disabled_elevation),
        content = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(all = if (isLandscape) 0.dp else small_padding)
                    .padding(end = if (isLandscape) 10.dp else 0.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    Icons.Default.SportsScore,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = null,
                    modifier = Modifier
                        .size(size = if (isLandscape) 41.dp else 48.dp)
                        .padding(end = if (isLandscape) 3.dp else 10.dp)
                        .fillMaxSize()
                )

                if (isLandscape) {
                    Text(
                        text = "Score: ",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = gameScoreTextStyle,
                        fontSize = fontSizeNormal,
                        textAlign = TextAlign.Left,
                        modifier = Modifier.padding(start = 6.dp)
                    )
                    AnimatedScoreCount(currentScore)
                } else {
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Score:",
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = gameScoreTextStyle,
                            fontSize = fontSizeUpperNormal,
                            textAlign = TextAlign.Right,
                        )
                        AnimatedScoreCount(currentScore)
                    }
                }
            }
        },
        colors = ButtonColors(
            Color.Transparent,
            Color.Transparent,
            MaterialTheme.colorScheme.primary,
            Color.Transparent
        )
    )
}

@Composable
fun AnimatedScoreCount(currentScore: Int?) {
    val isLandscape = isLandscape()
    AnimatedContent(
        targetState = currentScore,
        transitionSpec = {
            if (targetState!! > initialState!!) {
                slideInVertically { height -> height } + fadeIn() togetherWith
                        slideOutVertically { height -> -height } + fadeOut()
            } else {
                slideInVertically { height -> -height } + fadeIn() togetherWith
                        slideOutVertically { height -> height } + fadeOut()
            }.using(
                SizeTransform(clip = false)
            )
        },
        label = "animated score"
    ) { score ->
        Text(
            text = "$score",
            color = MaterialTheme.colorScheme.onPrimary,
            style = gameScoreTextStyle,
            fontSize = if (isLandscape) fontSizeNormal else fontSizeUpperNormal,
            textAlign = if (isLandscape) TextAlign.Left else TextAlign.Right,
            modifier = Modifier.padding(top = if (isLandscape) 0.dp else 0.dp)
        )
    }
}

@Composable
fun DifficultyBox(gameViewModel: GameViewModel) {

    val isLandscape = isLandscape()

    Button(
        onClick = { /* do nothing */ },
        modifier = Modifier
            .padding(start = game_buttons_padding_landscape, end = game_buttons_padding_landscape)
            .fillMaxWidth(),
        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp, 0.dp, disabled_elevation),
        enabled = false,
        colors = ButtonColors(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary
        ),
        content = {
            if (isLandscape) {
                //Landscape orientation -> Row
                DifficultyBoxLandscape(gameViewModel)
            } else {
                DifficultyBoxPortrait(gameViewModel)
            }

        }
    )
}

@Composable
fun DifficultyBoxPortrait(gameViewModel: GameViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = "Difficulty:",
            fontSize = fontSizeUpperNormal,
            style = gameButtonsTextStyle,
            color = Color.White,
        )
        PrintDifficultyStars(gameViewModel)
    }
}

@Composable
fun PrintDifficultyStars(gameViewModel: GameViewModel) {
    val isLandscape = isLandscape()

    when (gameViewModel.questionForUser.observeAsState().value?.difficulty) {
        DifficultyType.Easy.toString().lowercase() -> {
            Icon(
                Icons.Default.Star,
                tint = MaterialTheme.colorScheme.onPrimary,
                contentDescription = null,
                modifier = Modifier
                    .size(size = if (isLandscape) star_icon_size_landscape else star_icon_size)
                    .fillMaxSize()
            )
        }

        DifficultyType.Medium.toString().lowercase() -> {
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                for (i in 1..2) {
                    Icon(
                        Icons.Default.Star,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = null,
                        modifier = Modifier
                            .size(size = if (isLandscape) star_icon_size_landscape else star_icon_size)
                            .fillMaxSize()
                    )
                }
            }
        }

        DifficultyType.Hard.toString().lowercase() -> {

            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                for (i in 1..3) {
                    Icon(
                        Icons.Default.Star,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = null,
                        modifier = Modifier
                            .size(size = if (isLandscape) star_icon_size_landscape else star_icon_size)
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun DifficultyBoxLandscape(gameViewModel: GameViewModel) {
    Row(
        modifier = Modifier
            .height(41.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = "Difficulty:",
            fontSize = fontSizeNormal,
            style = gameButtonsTextStyle,
            color = Color.White,
            modifier = Modifier
                .padding(end = 5.dp)
        )
        PrintDifficultyStars(gameViewModel)
    }
}

@Composable
fun LivesBox(gameViewModel: GameViewModel) {
    val isLandscape = isLandscape()
    Button(
        onClick = { /* do nothing */ },
        modifier = Modifier
            .padding(start = game_buttons_padding_landscape, end = game_buttons_padding_landscape)
            .fillMaxWidth(),
        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp, 0.dp, disabled_elevation),
        enabled = false,
        colors = ButtonColors(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary
        ),
        content = {
            if (isLandscape) {
                LivesBoxLandscape(gameViewModel)
            } else {
                LivesBoxPortrait(gameViewModel)
            }

        }
    )
}

@Composable
fun LivesBoxLandscape(gameViewModel: GameViewModel) {
    Row(
        modifier = Modifier
            .height(41.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = "Lives:",
            fontSize = fontSizeNormal,
            style = gameButtonsTextStyle,
            color = Color.White,
            modifier = Modifier
                .padding(end = 6.dp)
        )
        PrintRemainingLives(gameViewModel)
    }
}


@Composable
fun PrintRemainingLives(gameViewModel: GameViewModel) {
    val isLandscape = isLandscape()
    val remainingLives = gameViewModel.lives.observeAsState().value ?: 0

    // Funzione per mostrare un cuore con animazione quando si perde una vita
    @Composable
    fun AnimatedHeart(show: Boolean) {
        val infiniteTransition = rememberInfiniteTransition(label = "")

        // Animating the scale to pulse between 0.9f and 1.1f
        val scale by infiniteTransition.animateFloat(
            initialValue = 0.9f,
            targetValue = 1.0f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 700, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        )

        // Animate alpha for fade out effect when life is lost
        val alpha by animateFloatAsState(
            targetValue = if (show) 1f else 0f,
            animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing), label = ""
        )

        Icon(
            Icons.Default.Favorite,
            tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = alpha),
            contentDescription = null,
            modifier = Modifier
                .size(if (isLandscape) heart_icon_size_landscape else heart_icon_size)
                .graphicsLayer {
                    scaleX = if (show) scale else 0f
                    scaleY = if (show) scale else 0f

                }
                .alpha(alpha) // Apply fade out animation
        )
    }

    // Animazione cuore spezzato quando le vite sono 0
    if (remainingLives == 0) {
        Icon(
            Icons.Default.HeartBroken,
            tint = MaterialTheme.colorScheme.onPrimary,
            contentDescription = null,
            modifier = Modifier
                .size(if (isLandscape) heart_icon_size_landscape else heart_icon_size)
                .fillMaxSize()
        )
    } else {
        // Animazione cuori rimanenti
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            for (i in 1..remainingLives) {
                AnimatedHeart(show = true)
            }
        }
    }
}


@Composable
fun LivesBoxPortrait(gameViewModel: GameViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = "Lives:",
            fontSize = fontSizeUpperNormal,
            style = gameButtonsTextStyle,
            color = Color.White
        )
        PrintRemainingLives(gameViewModel)
    }
}


@Composable
fun QuestionBox(gameViewModel: GameViewModel) {
    val isLandscape = isLandscape()
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            //Domanda nella UI
            ShowQuestion(gameViewModel)
        }

        Spacer(
            modifier = Modifier
                .size(size = if (isLandscape) 10.dp else 0.dp)
                .fillMaxWidth()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            //Possibili risposte nella UI
            if (isLandscape) {
                ShowAnswersLandscape(gameViewModel)
            } else {
                ShowAnswersPortrait(gameViewModel)
            }


        }
    }
}

@Composable
fun ShowAnswersLandscape(gvm: GameViewModel) {
    val question = gvm.questionForUser.observeAsState().value
    val answers = gvm.shuffledAnswers.observeAsState().value
    val givenAnswer = gvm.userAnswer.observeAsState().value

    Column{
        LazyVerticalGrid(
            userScrollEnabled = false,
            modifier = Modifier
                .fillMaxSize(),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(all = 8.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),

            ) {
            items(answers ?: emptyList()) { answer ->
                AnswerButton(
                    answerText = answer,
                    isCorrect = question?.correct_answer == answer,
                    isSelected = givenAnswer == answer,
                    gvm = gvm
                )
            }
        }
    }
}

@Composable
fun ShowQuestion(gameViewModel: GameViewModel) {
    Log.d("Debug", "++++++ Question ++++++\n")
    gameViewModel.questionForUser.observeAsState().value?.let { Log.d("Debug", it.question) }
    Log.d("Debug", "\n++++++ Question ++++++")

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
    }
    Text(
        //text = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
        text = gameViewModel.questionForUser.observeAsState().value?.question ?: "",
        style = gameQuestionTextStyle,
        textAlign = TextAlign.Center,
    )
}

@Composable
fun ShowAnswersPortrait(gvm: GameViewModel) {
    val question = gvm.questionForUser.observeAsState().value
    val answers = gvm.shuffledAnswers.observeAsState().value
    val givenAnswer = gvm.userAnswer.observeAsState().value

    Column(
        verticalArrangement = Arrangement.spacedBy(game_buttons_spacing),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp)
    ) {

        Log.d("GameView", "++++++ Answers list ++++++\n")

        for (ans in answers!!) {
            AnswerButton(
                answerText = ans,
                isCorrect = question?.correct_answer.equals(ans),
                isSelected = givenAnswer.equals(ans),
                gvm = gvm
            )
        }

        Log.d("GameView", "\n++++++ Answers list ++++++")

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
    val isLandscape = isLandscape()
    val isAnswerSelected = gvm.isAnswerSelected.observeAsState().value

    Log.d("GameView", "Answer: $answerText -> $isCorrect")


    Button(
        elevation = ButtonDefaults.buttonElevation(default_elevation, pressed_elevation),
        shape = RoundedCornerShape(game_buttons_shape),
        modifier = Modifier
            .fillMaxWidth()
            .height(height = if (isLandscape) 58.dp else game_buttons_height),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        onClick = {
            if (isAnswerSelected == false) {
                gvm.onAnswerClicked(answerText)
            }
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
    navController: NavHostController,
    gameViewModel: GameViewModel,
    settingsViewModel: SettingsViewModel
) {
    //determino orientamento schermo
    val isLandscape = isLandscape()
    val showLoading = gameViewModel.isGameTimerInterrupted.observeAsState().value!!
    val context = LocalContext.current

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

            .padding(start = if (getSurfaceRotation() == 1) WindowInsets.displayCutout
                .asPaddingValues()
                .calculateStartPadding(getLayoutDirection()) else if (getSurfaceRotation()==3) WindowInsets.displayCutout
                .asPaddingValues().
                calculateEndPadding(getLayoutDirection()) else 0.dp,
                end = if (getSurfaceRotation() == 1) WindowInsets.displayCutout
                    .asPaddingValues()
                    .calculateStartPadding(getLayoutDirection()) else if (getSurfaceRotation()==3) WindowInsets.displayCutout
                    .asPaddingValues().
                    calculateEndPadding(getLayoutDirection()) else 0.dp
            )
            .statusBarsPadding()
            .navigationBarsPadding()

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TopBar(
                title = context.getString(R.string.app_name),
                navController = navController,
                onLeftBtnClick = { navController.navigate("home") },
                leftBtnIcon = Icons.AutoMirrored.Filled.ArrowBack,
                showTitle = isLandscape,
                showRightButton = !showLoading,
                showLeftButton = !showLoading,
                settingsViewModel = settingsViewModel
            )
        }

        when (showLoading) {
            true -> LoadingScreen()
            false -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    GameMenuButtons(gameViewModel)
                }
            }
        }
    }
}


@Composable
fun GameMenuButtons(
    gameViewModel: GameViewModel
) {
    val context = LocalContext.current
    val isLandscape = isLandscape()

    var showDifficultySelectionDialog by rememberSaveable { mutableStateOf(false) }
    var showCategorySelectionDialog by rememberSaveable { mutableStateOf(false) }

    var selectedDifficulty by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf("") }

    @Composable
    fun StartGameButton(gameViewModel: GameViewModel) {
        //Start game button
        Button(
            onClick = {
                gameViewModel.onStartClicked()
            },
            elevation = ButtonDefaults.buttonElevation(default_elevation, pressed_elevation),
            shape = RoundedCornerShape(home_buttons_shape),
            modifier = Modifier
                .height(height = if (isLandscape) home_buttons_height_landscape else home_buttons_height)
                .width(width = if (isLandscape) home_buttons_width_landscape else home_buttons_width)
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
    }

    @Composable
    fun SelectDifficultyButton() {
        //Choose difficulty button
        Button(
            onClick = {
                showDifficultySelectionDialog = true
            },
            shape = RoundedCornerShape(home_buttons_shape),
            elevation = ButtonDefaults.buttonElevation(default_elevation, pressed_elevation),
            modifier = Modifier
                .height(height = if (isLandscape) home_buttons_height_landscape else home_buttons_height)
                .width(width = if (isLandscape) home_buttons_width_landscape else home_buttons_width)
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
                    fontSize = if (isLandscape) 23.sp else fontSizeMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    @Composable
    fun SelectCategoryButton() {
        //Choose category button
        Button(
            onClick = {
                showCategorySelectionDialog = true
            },
            shape = RoundedCornerShape(home_buttons_shape),
            elevation = ButtonDefaults.buttonElevation(default_elevation, pressed_elevation),
            modifier = Modifier
                .height(height = if (isLandscape) home_buttons_height_landscape else home_buttons_height)
                .width(width = if (isLandscape) home_buttons_width_landscape else home_buttons_width)
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
                    textAlign = TextAlign.Center,
                    fontSize = if (isLandscape) 23.sp else fontSizeMedium
                )
            }
        }
    }

    if (isLandscape) {
        Column(
            verticalArrangement = Arrangement.spacedBy(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            //Difficulty Selection Dialog
            if (showDifficultySelectionDialog) {
                DifficultySelectionDialog(
                    onDismissRequest = {
                        showDifficultySelectionDialog = false
                    },
                    onDifficultySelected = {
                        selectedDifficulty = it
                        gameViewModel.setDifficulty(it)
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
                    //categories = categories!!,
                    onCategorySelected = {
                        selectedCategory = it
                        gameViewModel.setCategory(it)
                    },
                    gameViewModel = gameViewModel
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                StartGameButton(gameViewModel)
                SelectDifficultyButton()
                SelectCategoryButton()
            }
        }
    } else {
        Column(
            verticalArrangement = Arrangement.spacedBy(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp)
        ) {
            //Difficulty Selection Dialog
            if (showDifficultySelectionDialog) {
                DifficultySelectionDialog(
                    onDismissRequest = {
                        showDifficultySelectionDialog = false
                    },
                    onDifficultySelected = {
                        selectedDifficulty = it
                        gameViewModel.setDifficulty(it)
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
                    //categories = categories!!,
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
            StartGameButton(gameViewModel)

            //Choose difficulty button
            SelectDifficultyButton()

            //Choose category button
            SelectCategoryButton()

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
                    Log.d("Debug", "Difficulty: $diff")
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (diff.toString() == gameViewModel.selectedDifficulty.value),
                                onClick = {
                                    onDifficultySelected(diff.toString())
                                    radioSelected = diff.toString()
                                }
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            onClick = {
                                onDifficultySelected(diff.toString())
                                radioSelected = diff.toString()
                                Log.d(
                                    "Debug",
                                    "Selected category: ${diff.toString().lowercase()}"
                                )
                            },
                            selected = (diff.toString() == radioSelected),
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
                elevation = ButtonDefaults.buttonElevation(
                    default_elevation,
                    pressed_elevation
                ),
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
                elevation = ButtonDefaults.buttonElevation(
                    default_elevation,
                    pressed_elevation
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ok")
            }
        }
    )
}

@Composable
fun GameOverScreen(
    gameViewModel: GameViewModel,
    navController: NavHostController,
) {
    val isRecord = gameViewModel.isRecord.observeAsState().value
    val isLandscape = isLandscape()

    @Composable
    fun MainMenuButton() {
        Button(
            elevation = ButtonDefaults.buttonElevation(
                default_elevation,
                pressed_elevation
            ),
            shape = RoundedCornerShape(game_buttons_shape),
            modifier = Modifier
                .width(280.dp)
                .height(game_buttons_height)
                .padding(
                    start = if (isLandscape) 15.dp else 0.dp,
                    end = if (isLandscape) 15.dp else 0.dp
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            onClick = {
                //torna alla home page senza salvare nulla sullo stack del navController
                navController.popBackStack()

                gameViewModel.setIsPlaying(false)
                gameViewModel.clearUserAnswer()
                gameViewModel.setGameOver(false)
            }) {
            Text(text = "Main Menu", fontSize = fontSizeNormal)
        }
    }

    @Composable
    fun GameMenuButton() {
        Button(
            elevation = ButtonDefaults.buttonElevation(
                default_elevation,
                pressed_elevation
            ),
            shape = RoundedCornerShape(game_buttons_shape),
            modifier = Modifier
                .width(280.dp)
                .height(game_buttons_height)
                .padding(
                    start = if (isLandscape) 15.dp else 0.dp,
                    end = if (isLandscape) 15.dp else 0.dp
                ),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            onClick = {
                /*setta isPlaying off e rimane su gameView*/
                gameViewModel.clearUserAnswer()
                gameViewModel.setGameOver(false)
                gameViewModel.setIsPlaying(false)
            }) {
            Text(text = "Game Menu", fontSize = fontSizeNormal)
        }
    }

    @Composable
    fun PlayAgainButton(gameViewModel: GameViewModel) {
        Button(
            elevation = ButtonDefaults.buttonElevation(
                default_elevation,
                pressed_elevation
            ),
            shape = RoundedCornerShape(game_buttons_shape),
            modifier = Modifier
                .width(280.dp)
                .height(game_buttons_height)
                .padding(
                    start = if (isLandscape) 15.dp else 0.dp,
                    end = if (isLandscape) 15.dp else 0.dp
                ),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            onClick = {
                gameViewModel.onStartClicked()
            }) {
            Text(text = "Play again", fontSize = fontSizeNormal)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
                /*.padding(all = if (isLandscape) 0.dp else 16.dp)
                .padding(top = if (isLandscape) 5.dp else 0.dp),*/
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if (isLandscape) Arrangement.Top else Arrangement.Center

        ) {
            //3 Bottoni: Main menu, Game menu, Play again
            Text(
                text = "Game Over!",
                fontSize = if (isLandscape) 40.sp else fontSizeBig,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(height = if (isLandscape) 15.dp else medium_spacing_height))

            if (isRecord == true) {
                Text(
                    text = "🎉 New record! 🎉",
                    fontSize = if (isLandscape) 30.sp else fontSizeUpperMedium,
                    color = MaterialTheme.colorScheme.primary, // Gold color
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(height = if (isLandscape) 8.dp else 15.dp))
            }

            Text(
                text = "Score: ${gameViewModel.lastGame.value?.score}",
                fontSize = if (isLandscape) 30.sp else fontSizeUpperMedium,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )

            if (isRecord == false) {
                Spacer(modifier = Modifier.height(height = if (isLandscape) 40.dp else 0.dp))
            }

            if (isLandscape) {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MainMenuButton()
                    GameMenuButton()
                    PlayAgainButton(gameViewModel)
                }
            } else {
                Spacer(modifier = Modifier.height(50.dp))
                MainMenuButton()
                Spacer(modifier = Modifier.height(15.dp))
                GameMenuButton()
                Spacer(modifier = Modifier.height(15.dp))
                PlayAgainButton(gameViewModel)
                Spacer(modifier = Modifier.height(90.dp))
            }
        }
    }
}



