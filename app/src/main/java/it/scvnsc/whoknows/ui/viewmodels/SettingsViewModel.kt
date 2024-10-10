package it.scvnsc.whoknows.ui.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.scvnsc.whoknows.utils.PreferencesManager

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val preferencesManager = PreferencesManager(application)

    private val _isDarkTheme = MutableLiveData(preferencesManager.isDarkTheme)
    val isDarkTheme: LiveData<Boolean> get() = _isDarkTheme

    private val _isSoundEnabled = MutableLiveData(preferencesManager.isSoundEnabled)
    val isSoundEnabled: LiveData<Boolean> get() = _isSoundEnabled

    fun toggleTheme() {
        val newTheme = !preferencesManager.isDarkTheme
        preferencesManager.isDarkTheme = newTheme
        _isDarkTheme.value = newTheme

        Toast.makeText(getApplication(), "Theme ${if(newTheme) "dark" else "light"}", Toast.LENGTH_SHORT).show()
    }

    fun toggleSound() {
        val newSoundSetting = !preferencesManager.isSoundEnabled
        preferencesManager.isSoundEnabled = newSoundSetting
        _isSoundEnabled.value = newSoundSetting

        Toast.makeText(getApplication(), "Sound ${if(newSoundSetting) "enabled" else "disabled"}", Toast.LENGTH_SHORT).show()
    }

}