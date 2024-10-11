package it.scvnsc.whoknows.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

@Composable
fun getLayoutDirection(): LayoutDirection {
    return LocalLayoutDirection.current
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun getSurfaceRotation(): Int? {
    return LocalContext.current.display?.rotation
}
