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
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.scvnsc.whoknows.R
import it.scvnsc.whoknows.ui.screens.components.TopBar
import it.scvnsc.whoknows.ui.theme.WhoKnowsTheme
import it.scvnsc.whoknows.ui.theme.bottom_bar_padding
import it.scvnsc.whoknows.ui.theme.buttonsTextStyle
import it.scvnsc.whoknows.ui.theme.default_elevation
import it.scvnsc.whoknows.ui.theme.home_buttons_height
import it.scvnsc.whoknows.ui.theme.home_buttons_height_landscape
import it.scvnsc.whoknows.ui.theme.home_buttons_shape
import it.scvnsc.whoknows.ui.theme.home_buttons_width
import it.scvnsc.whoknows.ui.theme.home_buttons_width_landscape
import it.scvnsc.whoknows.ui.theme.pressed_elevation
import it.scvnsc.whoknows.ui.theme.small_padding
import it.scvnsc.whoknows.ui.theme.titleTextStyle
import it.scvnsc.whoknows.ui.viewmodels.SettingsViewModel
import it.scvnsc.whoknows.utils.isLandscape

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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
                    Column (
                        modifier = Modifier
                            .paint(
                                // Replace with your image id
                                painterResource(
                                    id = if (settingsViewModel.isDarkTheme.observeAsState().value == true) R.drawable.puzzle_bg_black else R.drawable.puzzle_bg_white
                                ),
                                contentScale = ContentScale.Crop
                            )
                            .fillMaxSize()
                    ) {
                        Box (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = bottom_bar_padding, end = bottom_bar_padding)
                        ){
                            TopBar(
                                showTitle = true,
                                title = context.getString(R.string.app_name),
                                showRightButton = true,
                                showThemeChange = true,
                                settingsViewModel = settingsViewModel
                            )
                        }

                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(40.dp),
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Box (
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = bottom_bar_padding, end = bottom_bar_padding)
                            ){
                                HomeViewButtons(navController)
                            }
                        }
                    }
                } else {
                    Column (
                        modifier = Modifier
                            .paint(
                                // Replace with your image id
                                painterResource(
                                    id = if (settingsViewModel.isDarkTheme.observeAsState().value == true) R.drawable.puzzle_bg_black else R.drawable.puzzle_bg_white
                                ),
                                contentScale = ContentScale.Crop
                            )
                            .fillMaxSize()

                    ){
                        Box (
                            modifier = Modifier
                                .fillMaxWidth()
                        ){
                            TopBar(
                                showTitle = false,
                                showRightButton = true,
                                showThemeChange = true,
                                settingsViewModel = settingsViewModel
                            )
                        }

                        Box (
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = bottom_bar_padding)
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
    val isLandscape = isLandscape()

    if (isLandscape){
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxSize()
        ) {
            //play button
            Button(
                onClick = {
                    navController.navigate("game")
                },
                elevation = ButtonDefaults.buttonElevation(default_elevation, pressed_elevation),
                shape = RoundedCornerShape(home_buttons_shape),
                modifier = Modifier
                    .height(home_buttons_height_landscape)
                    .width(home_buttons_width_landscape)
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


            //stats button
            Button(
                onClick = {
                    navController.navigate("stats")
                },
                shape = RoundedCornerShape(home_buttons_shape),
                elevation = ButtonDefaults.buttonElevation(default_elevation, pressed_elevation),
                modifier = Modifier
                    .height(home_buttons_height_landscape)
                    .width(home_buttons_width_landscape)
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

            //settings button
            Button(
                onClick = {
                    navController.navigate("settings")
                },
                shape = RoundedCornerShape(home_buttons_shape),
                elevation = ButtonDefaults.buttonElevation(default_elevation, pressed_elevation),
                modifier = Modifier
                    .height(home_buttons_height_landscape)
                    .width(home_buttons_width_landscape)
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


    } else {
        Column(
            verticalArrangement = Arrangement.spacedBy(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp)
        ) {
            //Titolo applicazione
            AppTitle(context)

            //play button
            Button(
                onClick = {
                    navController.navigate("game")
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
                        text = "Play",
                        style = buttonsTextStyle,
                        textAlign = TextAlign.Center
                    )
                }

            }

            //stats button
            Button(
                onClick = {
                    navController.navigate("stats")
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

            //settings button
            Button(
                onClick = {
                    navController.navigate("settings")
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

