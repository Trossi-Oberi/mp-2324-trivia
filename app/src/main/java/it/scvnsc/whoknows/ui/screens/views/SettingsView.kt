package it.scvnsc.whoknows.ui.screens.views

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.scvnsc.whoknows.R
import it.scvnsc.whoknows.ui.screens.components.TopBar
import it.scvnsc.whoknows.ui.theme.WhoKnowsTheme
import it.scvnsc.whoknows.ui.theme.bottom_bar_padding
import it.scvnsc.whoknows.ui.theme.buttonsTextStyle
import it.scvnsc.whoknows.ui.theme.default_elevation
import it.scvnsc.whoknows.ui.theme.difficulty_buttons_height
import it.scvnsc.whoknows.ui.theme.difficulty_buttons_height_landscape
import it.scvnsc.whoknows.ui.theme.difficulty_buttons_width
import it.scvnsc.whoknows.ui.theme.difficulty_buttons_width_landscape
import it.scvnsc.whoknows.ui.theme.home_buttons_height
import it.scvnsc.whoknows.ui.theme.home_buttons_shape
import it.scvnsc.whoknows.ui.theme.home_buttons_width
import it.scvnsc.whoknows.ui.theme.icon_size_settings
import it.scvnsc.whoknows.ui.theme.icon_size_settings_landscape
import it.scvnsc.whoknows.ui.theme.pressed_elevation
import it.scvnsc.whoknows.ui.viewmodels.SettingsViewModel
import it.scvnsc.whoknows.utils.isLandscape

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsView(
    navController: NavHostController,
    settingsViewModel: SettingsViewModel
) {
    //determino orientamento schermo
    val context = LocalContext.current
    val isLandscape = isLandscape()

    val isDarkTheme by settingsViewModel.isDarkTheme.observeAsState(true)
    val isSoundEnabled by settingsViewModel.isSoundEnabled.observeAsState(true)

    WhoKnowsTheme(
        darkTheme = settingsViewModel.isDarkTheme.observeAsState().value == true,
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            content = {
                if (isLandscape) {
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
                        //Top app bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = bottom_bar_padding, end = bottom_bar_padding)
                        ) {
                            TopBar(
                                navController = navController,
                                onLeftBtnClick = { navController.navigate("home") },
                                leftBtnIcon = Icons.AutoMirrored.Filled.ArrowBack,
                                showTitle = true,
                                title = context.getString(R.string.app_name),
                                showThemeChange = false
                            )
                        }

                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(40.dp),
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            //Settings buttons
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = bottom_bar_padding, end = bottom_bar_padding)
                            ) {
                                SettingsButtons(
                                    isDarkTheme,
                                    isSoundEnabled,
                                    onToggleTheme = { settingsViewModel.toggleTheme() },
                                    onToggleSound = { settingsViewModel.toggleSound() }
                                )
                            }
                        }
                    }
                } else {
                    //Main column
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
                        //Top app bar
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
                                showThemeChange = false
                            )
                        }

                        //Settings buttons
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = bottom_bar_padding)
                        ) {
                            SettingsButtons(
                                isDarkTheme,
                                isSoundEnabled,
                                onToggleTheme = { settingsViewModel.toggleTheme() },
                                onToggleSound = { settingsViewModel.toggleSound() }
                            )
                        }

                    }
                }
            }
        )
    }
}

@Composable
fun SettingsButtons(isDarkTheme: Boolean, isSoundEnabled: Boolean, onToggleTheme: () -> Unit, onToggleSound: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    val isLandscape = isLandscape()

    if (isLandscape) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            //Bottone Tema
            Button(
                onClick = onToggleTheme,
                elevation = ButtonDefaults.buttonElevation(default_elevation, pressed_elevation),
                shape = RoundedCornerShape(home_buttons_shape),
                modifier = Modifier
                    .height(difficulty_buttons_height_landscape)
                    .width(difficulty_buttons_width_landscape)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Icon(
                        if(isDarkTheme) Icons.Default.WbSunny else Icons.Default.DarkMode,
                        contentDescription = null,
                        modifier = Modifier.size(icon_size_settings_landscape)
                    )

                    Spacer(modifier = Modifier.size(10.dp))

                    Text(
                        color = MaterialTheme.colorScheme.onPrimary,
                        text = "Toggle Theme ",
                        style = buttonsTextStyle
                    )
                }

            }

            //Bottone Sound
            Button(
                onClick = onToggleSound,
                elevation = ButtonDefaults.buttonElevation(default_elevation, pressed_elevation),
                shape = RoundedCornerShape(home_buttons_shape),
                modifier = Modifier
                    .height(difficulty_buttons_height_landscape)
                    .width(difficulty_buttons_width_landscape)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Icon(
                        if(isSoundEnabled) Icons.AutoMirrored.Filled.VolumeOff else Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = null,
                        modifier = Modifier.size(icon_size_settings_landscape)
                    )

                    Spacer(modifier = Modifier.size(10.dp))

                    Text(
                        color = MaterialTheme.colorScheme.onPrimary,
                        text = "Toggle Sound ",
                        style = buttonsTextStyle
                    )
                }
            }

            //Bottone Crediti (GitHub)
            Button(
                onClick = { uriHandler.openUri("https://github.com/Trossi-Oberi/mp-2324-trivia") },
                elevation = ButtonDefaults.buttonElevation(default_elevation, pressed_elevation),
                shape = RoundedCornerShape(home_buttons_shape),
                modifier = Modifier
                    .height(difficulty_buttons_height_landscape)
                    .width(difficulty_buttons_width_landscape)
            ) {
                Text(
                    color = MaterialTheme.colorScheme.onPrimary,
                    text = "Credits",
                    style = buttonsTextStyle
                )
            }
        }

    } else {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Bottone Tema
            Button(
                onClick = onToggleTheme,
                elevation = ButtonDefaults.buttonElevation(default_elevation, pressed_elevation),
                shape = RoundedCornerShape(home_buttons_shape),
                modifier = Modifier
                    .height(difficulty_buttons_height)
                    .width(difficulty_buttons_width)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Text(
                        color = MaterialTheme.colorScheme.onPrimary,
                        text = "Toggle Theme ",
                        style = buttonsTextStyle
                    )

                    Icon(
                        if(isDarkTheme) Icons.Default.WbSunny else Icons.Default.DarkMode,
                        contentDescription = null,
                        modifier = Modifier.size(icon_size_settings)
                    )

                }

            }

            //Bottone Sound
            Button(
                onClick = onToggleSound,
                elevation = ButtonDefaults.buttonElevation(default_elevation, pressed_elevation),
                shape = RoundedCornerShape(home_buttons_shape),
                modifier = Modifier
                    .height(difficulty_buttons_height)
                    .width(difficulty_buttons_width)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Text(
                        color = MaterialTheme.colorScheme.onPrimary,
                        text = "Toggle Sound ",
                        style = buttonsTextStyle
                    )

                    Icon(
                        if(isSoundEnabled) Icons.AutoMirrored.Filled.VolumeOff else Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = null,
                        modifier = Modifier.size(icon_size_settings)
                    )

                }

            }

            //Bottone Crediti (GitHub)
            Button(
                onClick = { uriHandler.openUri("https://github.com/Trossi-Oberi/mp-2324-trivia") },
                elevation = ButtonDefaults.buttonElevation(default_elevation, pressed_elevation),
                shape = RoundedCornerShape(home_buttons_shape),
                modifier = Modifier
                    .height(difficulty_buttons_height)
                    .width(difficulty_buttons_width)
            ) {
                Text(
                    color = MaterialTheme.colorScheme.onPrimary,
                    text = "Credits",
                    style = buttonsTextStyle
                )
            }
        }
    }
}
