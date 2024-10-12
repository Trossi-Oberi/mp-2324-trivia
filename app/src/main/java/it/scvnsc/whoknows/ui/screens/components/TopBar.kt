package it.scvnsc.whoknows.ui.screens.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.scvnsc.whoknows.R
import it.scvnsc.whoknows.ui.theme.DarkYellow
import it.scvnsc.whoknows.ui.theme.topBarTextStyle
import it.scvnsc.whoknows.ui.theme.top_bar_height
import it.scvnsc.whoknows.ui.viewmodels.SettingsViewModel
import it.scvnsc.whoknows.utils.isLandscape

@Composable
fun TopBar(
    navController: NavController? = null, //parametri opzionali (il pulsante di chiusura o goBack viene mostrato solo se navController != null)
    onLeftBtnClick: () -> Unit = {}, //parametri opzionali
    leftBtnIcon: ImageVector? = null, //parametri opzionali (scelta dell'icona se presente navController
    showTitle: Boolean = true,
    title: String? = null,
    showLeftButton: Boolean = true,
    showRightButton: Boolean = false,
    rightBtnIcon: ImageVector? = null,
    onRightBtnClick: () -> Unit = {},
    showThemeChange: Boolean = true,
    settingsViewModel: SettingsViewModel? = null
) {
    val isLandscape = isLandscape()
    val context = LocalContext.current


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(top_bar_height),
        verticalAlignment = Alignment.Top
    ) {
        // Navigation icon
        if (navController != null && showLeftButton) {
            Box(
                modifier = Modifier
                    .weight(0.2F)
                    .fillMaxSize()
            ) {
                IconButton(
                    onClick = onLeftBtnClick,
                    colors = IconButtonDefaults.iconButtonColors(DarkYellow),
                    modifier = Modifier
                        .align(Alignment.Center)
                ) {
                    if (leftBtnIcon != null) {
                        Icon(
                            imageVector = leftBtnIcon,
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
        if (showTitle && title != null) {
            Box(
                modifier = Modifier
                    .weight(0.6F)
                    .fillMaxSize()
            ) {
                val text = context.getString(R.string.app_name)
                val infiniteTransition = rememberInfiniteTransition(label = "")

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp)
                ) {
                    text.forEachIndexed { index, char ->
                        // Creiamo un'animazione per la traslazione verticale e la scala per ogni lettera
                        val offsetY by infiniteTransition.animateFloat(
                            initialValue = 0f,
                            targetValue = -10f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(
                                    500,
                                    easing = FastOutSlowInEasing,
                                    delayMillis = index * 150
                                ),
                                repeatMode = RepeatMode.Reverse
                            ), label = ""
                        )

                        val scale by infiniteTransition.animateFloat(
                            initialValue = 1f,
                            targetValue = 1.2f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(
                                    500,
                                    easing = FastOutSlowInEasing,
                                    delayMillis = index * 150
                                ),
                                repeatMode = RepeatMode.Reverse
                            ), label = ""
                        )

                        Text(
                            text = char.toString(),
                            style = topBarTextStyle,
                            modifier = Modifier
                                .graphicsLayer {
                                    translationY = offsetY  // Salto verticale della lettera
                                    scaleX = scale  // Scala orizzontale della lettera
                                    scaleY = scale   // Scala verticale della lettera
                                }
                        )
                    }
                }

            }
        } else {
            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.6F)
            )
        }

        // Other button
        if (showRightButton) {
            Box(
                modifier = Modifier
                    .weight(0.2F)
                    .fillMaxSize()
            ) {
                if (showThemeChange) {
                    val isDarkTheme = settingsViewModel?.isDarkTheme?.observeAsState()?.value

                    with(settingsViewModel) {
                        IconButton(
                            onClick = { this?.toggleTheme() },
                            colors = IconButtonDefaults.iconButtonColors(DarkYellow),
                            modifier = Modifier
                                .align(Alignment.Center)
                        ) {
                            val iconRotation by animateFloatAsState(
                                targetValue = if (isDarkTheme == true) 360f else 0f,
                                animationSpec = tween(
                                    durationMillis = 500,
                                    easing = FastOutSlowInEasing
                                ),
                                label = ""
                            )

                            AnimatedContent(
                                targetState = isDarkTheme,
                                transitionSpec = {
                                    fadeIn(
                                        animationSpec = tween(
                                            300,
                                            delayMillis = 150
                                        )
                                    ) togetherWith fadeOut(animationSpec = tween(500))
                                },
                                label = "Theme Icon Animation"
                            ) { isDark ->
                                Icon(
                                    imageVector = if (isDark == true) Icons.Filled.DarkMode else Icons.Filled.WbSunny,
                                    contentDescription = if (isDark == true) "Switch to Light Mode" else "Switch to Dark Mode",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.rotate(iconRotation)
                                )
                            }
                        }
                    }
                } else {
                    //General right button

                    IconButton(
                        onClick = { onRightBtnClick() },
                        colors = IconButtonDefaults.iconButtonColors(DarkYellow),
                        modifier = Modifier
                            .align(Alignment.Center)

                    ) {
                        if (rightBtnIcon != null) {
                            Icon(
                                rightBtnIcon,
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
