package it.scvnsc.whoknows.ui.screens.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import it.scvnsc.whoknows.R
import it.scvnsc.whoknows.data.model.Game
import it.scvnsc.whoknows.ui.screens.components.TopBar
import it.scvnsc.whoknows.ui.theme.WhoKnowsTheme
import it.scvnsc.whoknows.ui.theme.buttonsTextStyle
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

    val deletedGamesCount = statsViewModel.deletedGamesCount.observeAsState().value

    //TODO: Il codice qui sotto funziona, sostituire "Game history cleared" con stringa a piacere se si vuole dire quanti game sono stati cancellati
    // ricordarsi di sistemare anche StatsViewModel (decommentare linee di codice)
    statsViewModel.gameDeletionComplete.observe(LocalLifecycleOwner.current){ shouldShowNotification ->
        if (shouldShowNotification == true){
            Toast.makeText(context, "Game history cleared", Toast.LENGTH_SHORT).show()
            statsViewModel.gameDeletionComplete.value = false
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

                        //TODO:: known issue: Toast shows at StatsView launch
                        //Osservo il cambiamento nel numero di giochi (se cancellati per esempio)
                        /*LaunchedEffect(key1 = deletedGamesCount) {
                            Toast.makeText(
                                context,
                                if (deletedGamesCount == 0) "No games deleted" else if (deletedGamesCount == 1) "Deleted $deletedGamesCount game" else "Deleted $deletedGamesCount games",
                                Toast.LENGTH_SHORT
                            ).show()
                        }*/

                        /*LaunchedEffect(key1 = gameDeletionComplete) {
                            Toast.makeText(
                                context,
                                if (gameDeletionComplete == true) "Game history cleared" else "Game history already empty",
                                Toast.LENGTH_SHORT
                            ).show()
                        }*/

                        //Sol alternativa, dire semplicemente "Game history cleared" invece di contare il numero di game cancellati

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
                                    statsViewModel.deleteGames()
                                },
                                showThemeChange = false,
                                settingsViewModel = settingsViewModel
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            StatsPage(statsViewModel)
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun StatsPage(statsViewModel: StatsViewModel) {
    //Osservo i game che il viewmodel prende dal DAO
    val games = statsViewModel.retrievedGames.observeAsState().value

    //Inizializzo il viewmodel
    with(statsViewModel) {
        retrieveGamesOnStart()
    }

    //Colonna portante che racchiude tutta la schermata
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {
            //HEADER
            StatsHeader()
        }
        //Al suo interno usa una LazyColumn per mostrare i game
        ShowGames(games)

        /*CircularProgressIndicator(
            modifier = Modifier.size(120.dp),
            strokeWidth = 7.dp
        )*/
    }
}

@Composable
fun ShowGames(games: List<Game>?) {
    if (games != null) {
        LazyColumn {
            items(games) { game ->
                StatsRow(game)
            }
        }
    }
}

@Composable
fun StatsRow(game: Game) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .border(3.dp, Color.Magenta)

    ) {
        //BOX SCORE
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxSize()
                .weight(0.33F)
                .border(3.dp, Color.Red)

        ) {
            Text(
                text = "${game.score}",
                style = buttonsTextStyle,

                )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxSize()
                .weight(0.33F)
                .border(3.dp, Color.Red)
        ) {
            Text(
                text = game.difficulty,
                style = buttonsTextStyle,
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxSize()
                .weight(0.33F)
                .border(3.dp, Color.Red)
        ) {
            Text(
                text = game.date,
                style = buttonsTextStyle,
            )
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
