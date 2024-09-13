package it.scvnsc.whoknows.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    //TODO: Mantenere difficolta' quando ruota lo schermo

    private val _selectedDifficulty = MutableLiveData<String>()
    val selectedDifficulty: LiveData<String> = _selectedDifficulty

    fun setDifficulty(difficulty: String) {
        _selectedDifficulty.value = difficulty
    }

    fun getDifficulty(): String? {
        return _selectedDifficulty.value
    }
}