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
import it.scvnsc.whoknows.ui.theme.home_buttons_shape
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
                Column(
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
                            .padding(
                                start = if (isLandscape) bottom_bar_padding else 0.dp,
                                end = if (isLandscape) bottom_bar_padding else 0.dp
                            )
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
        )
    }
}

@Composable
fun SettingsButtons(
    isDarkTheme: Boolean,
    isSoundEnabled: Boolean,
    onToggleTheme: () -> Unit,
    onToggleSound: () -> Unit
) {
    val uriHandler = LocalUriHandler.current
    val isLandscape = isLandscape()

    val buttonModifier = Modifier
        .height(if (isLandscape) difficulty_buttons_height_landscape else difficulty_buttons_height)
        .width(if (isLandscape) difficulty_buttons_width_landscape else difficulty_buttons_width)

    val iconSize = if (isLandscape) icon_size_settings_landscape else icon_size_settings

    val buttonContent: @Composable (String, @Composable () -> Unit) -> Unit = { text, icon ->
        if (isLandscape) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                icon()
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    color = MaterialTheme.colorScheme.onPrimary,
                    text = text,
                    style = buttonsTextStyle
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    color = MaterialTheme.colorScheme.onPrimary,
                    text = text,
                    style = buttonsTextStyle
                )
                icon()
            }
        }
    }

    val buttonLayout: @Composable (@Composable () -> Unit) -> Unit = { content ->
        if (isLandscape) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                content()
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
            }
        }
    }

    buttonLayout {
        Button(
            onClick = onToggleTheme,
            elevation = ButtonDefaults.buttonElevation(default_elevation, pressed_elevation),
            shape = RoundedCornerShape(home_buttons_shape),
            modifier = buttonModifier
        ) {
            buttonContent("Toggle Theme") {
                Icon(
                    if (isDarkTheme) Icons.Default.WbSunny else Icons.Default.DarkMode,
                    contentDescription = null,
                    modifier = Modifier.size(iconSize)
                )
            }
        }

        Button(
            onClick = onToggleSound,
            elevation = ButtonDefaults.buttonElevation(default_elevation, pressed_elevation),
            shape = RoundedCornerShape(home_buttons_shape),
            modifier = buttonModifier
        ) {
            buttonContent("Toggle Sound") {
                Icon(
                    if (isSoundEnabled) Icons.AutoMirrored.Filled.VolumeOff else Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = null,
                    modifier = Modifier.size(iconSize)
                )
            }
        }

        Button(
            onClick = { uriHandler.openUri("https://github.com/Trossi-Oberi/mp-2324-trivia") },
            elevation = ButtonDefaults.buttonElevation(default_elevation, pressed_elevation),
            shape = RoundedCornerShape(home_buttons_shape),
            modifier = buttonModifier
        ) {
            Text(
                color = MaterialTheme.colorScheme.onPrimary,
                text = "Credits",
                style = buttonsTextStyle
            )
        }
    }
}