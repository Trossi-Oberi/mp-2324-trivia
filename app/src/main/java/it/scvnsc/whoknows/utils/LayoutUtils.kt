package it.scvnsc.whoknows.utils

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.Surface
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

@Composable
fun getLayoutDirection(): LayoutDirection {
    return LocalLayoutDirection.current
}

@Composable
fun getSurfaceRotation(): Int? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        Log.d("LayoutUtils", "Display rotation: ${LocalContext.current.display?.rotation}")
        return LocalContext.current.display?.rotation
    } else {
        val windowManager = LocalContext.current.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        Log.d("LayoutUtils", "Display rotation OLD: ${display.rotation}")
        return when (display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 1
            Surface.ROTATION_180 -> 2
            Surface.ROTATION_270 -> 3
            else -> null
        }
    }
}
