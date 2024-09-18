package it.scvnsc.whoknows.ui.screens.views

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.scvnsc.whoknows.R
import it.scvnsc.whoknows.data.model.Game
import it.scvnsc.whoknows.ui.theme.WhoKnowsTheme
import it.scvnsc.whoknows.ui.theme.buttonsTextStyle
import it.scvnsc.whoknows.ui.theme.topBarTextStyle
import it.scvnsc.whoknows.ui.viewmodels.StatsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsView(
    navController: NavHostController,
    statsViewModel: StatsViewModel,
) {

    //TODO:: reset statistiche

    //Osservo i game che il viewmodel prende dal DAO
    val games = statsViewModel.retrievedGames.observeAsState().value
    with(statsViewModel){
        retrieveGamesOnStart()
    }

    val context = LocalContext.current

    WhoKnowsTheme (/*darkTheme = settingsViewModel.isDarkTheme.observeAsState().value == true*/) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = context.getString(R.string.app_name),
                            style = topBarTextStyle,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigate("home") }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    actions = {
                        //pulsante per cambiare tema
                        /*with(settingsViewModel) {
                            IconButton(onClick = { toggleDarkTheme() }) {
                                if (isDarkTheme.observeAsState().value == true) {
                                    Icon(
                                        androidx.compose.material.icons.Icons.Filled.DarkMode,
                                        contentDescription = null
                                    )
                                } else {
                                    Icon(
                                        androidx.compose.material.icons.Icons.Filled.WbSunny,
                                        contentDescription = null
                                    )
                                }
                            }
                        }*/
                    }
                )
            },
            content = { padding ->
                //Colonna portante che racchiude tutta la schermata
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
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
        )
    }
}

@Composable
fun ShowGames(games: List<Game>?) {
    if (games != null) {
        LazyColumn {
            items(games){
                game -> StatsRow(game)
            }
        }
    }
}

@Composable
fun StatsRow(game: Game) {
    Row (
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .border(3.dp, Color.Magenta)

    ){
        //BOX SCORE
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxSize()
                .weight(0.33F)
                .border(3.dp, Color.Red)

        ){
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
        ){
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
        ){
            Text(
                text = game.date,
                style = buttonsTextStyle,
            )
        }
    }
}

@Composable
fun StatsHeader() {
    Row (
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .border(3.dp, Color.Magenta)

    ){
        //BOX SCORE
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxSize()
                .weight(0.33F)
                .border(3.dp, Color.Red)

        ){
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
        ){
            Text(
                text = "Difficulty",
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
        ){
            Text(
                text = "Date",
                style = buttonsTextStyle,
            )
        }
    }
}
