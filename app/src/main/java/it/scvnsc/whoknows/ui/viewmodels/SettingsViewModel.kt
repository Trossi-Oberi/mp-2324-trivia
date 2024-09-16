package it.scvnsc.whoknows.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val _isDarkTheme = MutableLiveData<Boolean>(false)
    val isDarkTheme: LiveData<Boolean> get() = _isDarkTheme

    fun toggleDarkTheme() {
        _isDarkTheme.value = !_isDarkTheme.value!!
    }

}