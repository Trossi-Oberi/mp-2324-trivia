package it.scvnsc.whoknows.ui.screens.views

import android.provider.Settings.Global.getString
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import androidx.navigation.NavHostController
import it.scvnsc.whoknows.R
import it.scvnsc.whoknows.ui.theme.WhoKnowsTheme
import it.scvnsc.whoknows.ui.theme.buttonsTextStyle
import it.scvnsc.whoknows.ui.theme.home_buttons_height
import it.scvnsc.whoknows.ui.theme.home_buttons_padding
import it.scvnsc.whoknows.ui.theme.home_buttons_shape
import it.scvnsc.whoknows.ui.theme.home_buttons_width
import it.scvnsc.whoknows.ui.theme.medium_padding
import it.scvnsc.whoknows.ui.theme.small_padding
import it.scvnsc.whoknows.ui.theme.titleTextStyle
import it.scvnsc.whoknows.ui.viewmodels.SettingsViewModel
import it.scvnsc.whoknows.utils.isLandscape
import kotlin.coroutines.coroutineContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    navController: NavHostController,
    settingsViewModel: SettingsViewModel
) {

    //determino orientamento schermo
    val isLandscape = isLandscape()
    val context = LocalContext.current

    WhoKnowsTheme(
        darkTheme = settingsViewModel.isDarkTheme.observeAsState().value == true
    ) {
        Surface {
            Scaffold(
                //modificatori di stile
                modifier = Modifier
                    .fillMaxSize(),

                //contenuto topBar
                topBar = {
                    TopAppBar(
                        title = { //empty
                        },
                        actions = {
                            //pulsante per cambiare tema
                            with(settingsViewModel) {
                                IconButton(onClick = { toggleDarkTheme() }) {
                                    if (isDarkTheme.value == true) {
                                        Icon(Icons.Filled.DarkMode, contentDescription = null)
                                    } else {
                                        Icon(Icons.Filled.WbSunny, contentDescription = null)
                                    }
                                }
                            }
                        }
                    )
                },

                //contenuto principale
                content = { padding ->

                    //se orientamento orizzontale dispone i bottoni su una fila orizzontale
                    if (isLandscape) {
                        //TODO:: da sistemare

                        Text(
                            text = context.getString(R.string.app_name),
                            color = Color.Yellow,
                            style = titleTextStyle,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxHeight()
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(home_buttons_padding),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            //TODO::
                            //tre bottoni principali dell'applicazione

                            Button(
                                onClick = {
                                    navController.navigate("game")
                                },
                                shape = ButtonDefaults.outlinedShape,

                                ) {
                                Text(
                                    text = "Play"
                                )
                            }

                            Button(
                                onClick = {
                                    navController.navigate("stats")
                                },
                                shape = ButtonDefaults.outlinedShape
                            ) {
                                Text(
                                    text = "Your games"
                                )
                            }

                            Button(
                                onClick = {
                                    navController.navigate("settings")
                                },
                                shape = ButtonDefaults.outlinedShape
                            ) {
                                Text(
                                    text = "Settings"
                                )
                            }
                        }
                    } else {

                        Column(
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                        ) {
                            Text(
                                text = "¿WhoKnows?",
                                style = titleTextStyle,
                                textAlign = TextAlign.Center
                            )


                            //tre bottoni principali dell'applicazione
                            //TODO: separare funzione in diverse composable
                            //TODO:

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
                }
            )
        }
    }
}