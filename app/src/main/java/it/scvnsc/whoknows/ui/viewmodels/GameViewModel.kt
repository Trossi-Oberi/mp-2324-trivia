package it.scvnsc.whoknows.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel (application: Application) : AndroidViewModel(application) {

    private val _selectedDifficulty = MutableLiveData("NOT SET")
    private val _showDifficultySelection = MutableLiveData (false)
    val selectedDifficulty: LiveData<String> get() = _selectedDifficulty
    val showDifficultySelection: LiveData<Boolean> get() = _showDifficultySelection

    fun setDifficulty(difficulty: String) {
        _selectedDifficulty.value = difficulty
    }

    fun setShowDifficultySelection(show: Boolean) {
        _showDifficultySelection.value = show
    }
}