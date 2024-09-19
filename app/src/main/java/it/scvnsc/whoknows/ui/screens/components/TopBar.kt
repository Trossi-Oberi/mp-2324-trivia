package it.scvnsc.whoknows.ui.screens.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.scvnsc.whoknows.ui.theme.DarkYellow
import it.scvnsc.whoknows.ui.theme.topBarTextStyle
import it.scvnsc.whoknows.ui.theme.top_bar_height
import it.scvnsc.whoknows.ui.theme.top_bar_padding
import it.scvnsc.whoknows.ui.viewmodels.GameViewModel
import it.scvnsc.whoknows.ui.viewmodels.SettingsViewModel

@Composable
fun TopBar(
    navController: NavController? = null, //parametri opzionali (il pulsante di chiusura o goBack viene mostrato solo se navController != null)
    onClick: () -> Unit = {}, //parametri opzionali
    navIcon: ImageVector? = null, //parametri opzionali (scelta dell'icona se presente navController
    showTitle: Boolean = true,
    title: String? = null,
    showThemeChange: Boolean = true,
    settingsViewModel: SettingsViewModel,
    gameViewModel: GameViewModel?
) {
    Row(
        modifier = Modifier
            .padding(top = top_bar_padding)
            .fillMaxWidth()
            .height(top_bar_height),
        verticalAlignment = Alignment.Top
    ) {
        // Navigation icon
        if (navController != null) {
            Box(
                modifier = Modifier
                    .weight(0.2F)
                    .fillMaxSize()
            ) {
                IconButton(
                    onClick = onClick,
                    colors = IconButtonDefaults.iconButtonColors(DarkYellow),
                    modifier = Modifier
                        .align(Alignment.Center)
                ) {
                    if (navIcon != null) {
                        Icon(
                            imageVector = navIcon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        } else {
            Spacer(
                modifier = Modifier
                    .weight(0.2F)
                    .fillMaxSize()
            )
        }

        // App title
        if(showTitle && title != null) {
            Box(
                modifier = Modifier
                    .weight(0.6F)
                    .fillMaxSize()
            ) {
                Text(
                    text = title,
                    style = topBarTextStyle, // Sostituisci con lo stile desiderato
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        } else {
            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.6F)
            )
        }

        // Theme (dark/light switch button)
        if(showThemeChange) {
            Box(
                modifier = Modifier
                    .weight(0.2F)
                    .fillMaxSize()
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
        } else {
            Spacer(
                modifier = Modifier
                    .weight(0.2F)
                    .fillMaxSize()
            )
        }

    }
}

/*
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