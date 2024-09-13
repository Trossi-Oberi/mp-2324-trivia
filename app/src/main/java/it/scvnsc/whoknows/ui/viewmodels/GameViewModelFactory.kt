package it.scvnsc.whoknows.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

//Factory per la creazione di GameViewModel - gestione lifecycle automatica di ViewModelProvider
class GameViewModelFactory(private val application: Application) : ViewModelProvider.AndroidViewModelFactory(application) {

    // Sovrascrive il metodo create per fornire l'istanza di GameViewModel
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Verifica se la classe del ViewModel richiesto è GameViewModel
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(application) as T // Ritorna un'istanza di GameViewModel
        }
        // Se il ViewModel richiesto non è GameViewModel, lancia un'eccezione
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
