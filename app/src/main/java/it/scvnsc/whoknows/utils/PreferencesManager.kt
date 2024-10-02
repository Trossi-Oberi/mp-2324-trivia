package it.scvnsc.whoknows.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    //TODO: da sistemare il suono


    private val prefs: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    var isDarkTheme: Boolean
        get() = prefs.getBoolean("isDarkTheme", true)
        set(value) = prefs.edit().putBoolean("isDarkTheme", value).apply()

    var isSoundEnabled: Boolean
        get() = prefs.getBoolean("isSoundEnabled", true)
        set(value) = prefs.edit().putBoolean("isSoundEnabled", value).apply()
}