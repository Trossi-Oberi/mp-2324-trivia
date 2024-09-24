package it.scvnsc.whoknows.ui.screens.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.scvnsc.whoknows.R
import it.scvnsc.whoknows.ui.screens.components.TopBar
import it.scvnsc.whoknows.ui.theme.WhoKnowsTheme
import it.scvnsc.whoknows.ui.theme.bottom_bar_padding
import it.scvnsc.whoknows.ui.theme.buttonsTextStyle
import it.scvnsc.whoknows.ui.theme.default_elevation
import it.scvnsc.whoknows.ui.theme.home_buttons_height
import it.scvnsc.whoknows.ui.theme.home_buttons_shape
import it.scvnsc.whoknows.ui.theme.home_buttons_width
import it.scvnsc.whoknows.ui.theme.pressed_elevation
import it.scvnsc.whoknows.ui.viewmodels.SettingsViewModel
import it.scvnsc.whoknows.utils.isLandscape

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
//TODO : credits, sounds, theme
fun SettingsView(
    navController: NavHostController,
    settingsViewModel: SettingsViewModel
) {
    //determino orientamento schermo
    val context = LocalContext.current
    val isLandscape = isLandscape()

    WhoKnowsTheme(
        darkTheme = settingsViewModel.isDarkTheme.observeAsState().value == true,
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            content = {
                if (isLandscape) {
                    //TODO:: da sistemare
                } else {
                    //Main column
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
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
                            SettingsButtons()
                        }

                    }
                }
            }
        )
    }
}

@Composable
fun SettingsButtons() {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Bottone Tema
        Button(
            onClick = {},
            elevation = ButtonDefaults.buttonElevation(default_elevation, pressed_elevation),
            shape = RoundedCornerShape(home_buttons_shape),
            modifier = Modifier
                .height(home_buttons_height)
                .width(home_buttons_width)
        ) {
            Text(
                color = MaterialTheme.colorScheme.onPrimary,
                text = "Toggle Theme",
                style = buttonsTextStyle
            )
        }

        //Bottone Sound
        Button(
            onClick = {},
            elevation = ButtonDefaults.buttonElevation(default_elevation, pressed_elevation),
            shape = RoundedCornerShape(home_buttons_shape),
            modifier = Modifier
                .height(home_buttons_height)
                .width(home_buttons_width)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Text(
                    color = MaterialTheme.colorScheme.onPrimary,
                    text = "Toggle Sound",
                    style = buttonsTextStyle
                )

                Icon(
                    Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )

            }

        }

        //Bottone Crediti (GitHub)
        Button(
            onClick = { uriHandler.openUri("https://github.com/Trossi-Oberi/mp-2324-trivia") },
            elevation = ButtonDefaults.buttonElevation(default_elevation, pressed_elevation),
            shape = RoundedCornerShape(home_buttons_shape),
            modifier = Modifier
                .height(home_buttons_height)
                .width(home_buttons_width)
        ) {
            Text(
                color = MaterialTheme.colorScheme.onPrimary,
                text = "Credits",
                style = buttonsTextStyle
            )
        }
    }

}
