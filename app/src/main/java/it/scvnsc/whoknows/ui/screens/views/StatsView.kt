package it.scvnsc.whoknows.ui.screens.views

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.scvnsc.whoknows.R
import it.scvnsc.whoknows.ui.theme.DarkYellow
import it.scvnsc.whoknows.ui.theme.WhoKnowsTheme
import it.scvnsc.whoknows.ui.theme.Yellow60
import it.scvnsc.whoknows.ui.theme.gradientBackgroundColors
import it.scvnsc.whoknows.ui.theme.topBarTextStyle
import it.scvnsc.whoknows.ui.viewmodels.SettingsViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsView(
    navController: NavHostController,
    settingsViewModel: SettingsViewModel
) {

    //TODO:: reset statistiche

    val context = LocalContext.current

    //Background gradient animation
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val targetOffset = with(LocalDensity.current) {
        1000.dp.toPx()
    }
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = targetOffset,
        animationSpec = infiniteRepeatable(
            tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset"
    )


    WhoKnowsTheme(darkTheme = settingsViewModel.isDarkTheme.observeAsState().value == true) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
//                        .drawWithCache {
//                            val brushSize = 400f
//                            val brush = Brush.linearGradient(
//                                colors = gradientBackgroundColors,
//                                start = Offset(offset, offset),
//                                end = Offset(offset + brushSize, offset + brushSize),
//                                tileMode = TileMode.Mirror
//                            )
//                            onDrawBehind {
//                                drawRect(brush)
//                            }
//                        }
                ) {
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


//                Column(
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(padding)
//                ) {
//
//                    CircularProgressIndicator(
//                        modifier = Modifier.size(120.dp),
//                        strokeWidth = 7.dp
//                    )
//
//                    Text(
//                        color = Color.Magenta,
//                        text = "Benvenuto nelle statistiche!"
//                    )
//                }
            }
        )
    }
}


@Preview
@Composable
fun StatsViewPreview() {
    StatsView(
        navController = NavHostController(LocalContext.current),
        SettingsViewModel(application = LocalContext.current.applicationContext as android.app.Application)
    )

}