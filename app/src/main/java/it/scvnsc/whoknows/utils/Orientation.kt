package it.scvnsc.whoknows.utils

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration


//funzione per capire se siamo in modalità landscape: returns true if is landscape, else false
@Composable
fun isLandscape(): Boolean {
    //recupera l'orientamento dello schermo
    val appConfiguration = LocalConfiguration.current
    return appConfiguration.orientation == Configuration.ORIENTATION_LANDSCAPE
}