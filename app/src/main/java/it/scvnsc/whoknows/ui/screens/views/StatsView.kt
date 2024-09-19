package it.scvnsc.whoknows.ui.screens.views

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.scvnsc.whoknows.R
import it.scvnsc.whoknows.data.model.Game
import it.scvnsc.whoknows.ui.theme.DarkYellow
import it.scvnsc.whoknows.ui.theme.WhoKnowsTheme
import it.scvnsc.whoknows.ui.theme.buttonsTextStyle
import it.scvnsc.whoknows.ui.theme.topBarTextStyle
import it.scvnsc.whoknows.ui.viewmodels.SettingsViewModel
import it.scvnsc.whoknows.ui.viewmodels.StatsViewModel
import it.scvnsc.whoknows.utils.isLandscape

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
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

    WhoKnowsTheme(darkTheme = settingsViewModel.isDarkTheme.observeAsState().value == true) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            content = {
                if (isLandscape) {
                    //TODO:: da sistemare
                } else {
                    Column(

                    ) {
                        Box(

                        ) {

                            //Remove this when done
                            StatsTopBar(navController, settingsViewModel)
                            //TODO:: replace with TopBar
                        }

                        Box(

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
fun StatsTopBar(navController: NavHostController, settingsViewModel: SettingsViewModel) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .padding(top = 35.dp)
            .fillMaxWidth()
            .height(90.dp)
            .border(1.dp, Color.Red),
        verticalAlignment = Alignment.Top
    ) {
        //Go back button
        Box(
            modifier = Modifier
                .weight(0.2F)
                .fillMaxSize()
                .border(1.dp, Color.Blue)
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
                    colors = androidx.compose.material3.IconButtonDefaults.iconButtonColors(it.scvnsc.whoknows.ui.theme.DarkYellow),
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
                text = "Score",
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
                text = "Difficulty",
                style = buttonsTextStyle,
            )
        }

        Box(
            //contentAlignment = Alignment.Center,
            modifier = Modifier
                //.align(Alignment.CenterVertically)
                .fillMaxSize()
                .weight(0.33F)
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .border(3.dp, Color.Blue)
            ) {
                Text(
                    text = "Date",
                    style = buttonsTextStyle,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .border(1.dp, Color.Black)
                        .fillMaxSize()
                        .weight(0.5f)
                )
                Text(
                    text = "Date",
                    style = buttonsTextStyle,
                    textAlign = TextAlign.Right,
                    modifier = Modifier
                        .border(1.dp, Color.Black)
                        .fillMaxSize()
                        .weight(0.5f)
                )
            }

        }
    }
}
