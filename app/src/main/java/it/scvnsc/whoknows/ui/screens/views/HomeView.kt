package it.scvnsc.whoknows.ui.screens.views

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.scvnsc.whoknows.R
import it.scvnsc.whoknows.ui.theme.WhoKnowsTheme
import it.scvnsc.whoknows.ui.theme.buttonsTextStyle
import it.scvnsc.whoknows.ui.theme.home_buttons_height
import it.scvnsc.whoknows.ui.theme.home_buttons_padding
import it.scvnsc.whoknows.ui.theme.home_buttons_shape
import it.scvnsc.whoknows.ui.theme.home_buttons_width
import it.scvnsc.whoknows.ui.theme.small_padding
import it.scvnsc.whoknows.ui.theme.titleTextStyle
import it.scvnsc.whoknows.ui.theme.top_bar_height
import it.scvnsc.whoknows.ui.theme.top_bar_padding
import it.scvnsc.whoknows.ui.viewmodels.SettingsViewModel
import it.scvnsc.whoknows.utils.isLandscape

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    navController: NavHostController,
    settingsViewModel: SettingsViewModel
) {

    //determino orientamento schermo
    val isLandscape = isLandscape()
    val context = LocalContext.current

    WhoKnowsTheme(darkTheme = settingsViewModel.isDarkTheme.observeAsState().value == true) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            //contenuto principale
            content = {
                if(isLandscape) {
                    //TODO:: da sistemare
                } else {
                    Column (
                        modifier = Modifier.fillMaxSize()
                    ){
                        Box (
                            modifier = Modifier
                                .padding(top = top_bar_padding)
                                .height(top_bar_height)
                                .fillMaxWidth()
                                .border(1.dp, Color.Red)
                        ){
                            HomeViewTopBar(settingsViewModel)
                        }

                        Box (
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color.Green)
                        ){
                            HomeViewButtons(navController)
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun HomeViewButtons(navController: NavHostController) {
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.spacedBy(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp, bottom = 100.dp)
    ) {
        //Titolo applicazione
        AppTitle(context)

        //tre bottoni principali dell'applicazione
        Button(
            onClick = {
                navController.navigate("game")
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
                    text = "Play",
                    style = buttonsTextStyle,
                    textAlign = TextAlign.Center
                )
            }

        }

        Button(
            onClick = {
                navController.navigate("stats")
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
                    Icons.Filled.Leaderboard,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )

                Spacer(modifier = Modifier.size(15.dp))

                Text(
                    text = "Your games",
                    style = buttonsTextStyle,
                    textAlign = TextAlign.Center
                )
            }
        }

        Button(
            onClick = {
                navController.navigate("settings")
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
                    Icons.Filled.Settings,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )

                Spacer(modifier = Modifier.size(15.dp))

                Text(
                    text = "Settings",
                    style = buttonsTextStyle,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun HomeViewTopBar(settingsViewModel: SettingsViewModel) {
    Row(
        modifier = Modifier
            .fillMaxSize(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Absolute.Right
    ) {
        Spacer(
            modifier = Modifier
                .weight(0.8F)
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
fun AppTitle(context: Context) {
    Text(
        text = context.getString(R.string.app_name),
        style = titleTextStyle,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
    )
}

