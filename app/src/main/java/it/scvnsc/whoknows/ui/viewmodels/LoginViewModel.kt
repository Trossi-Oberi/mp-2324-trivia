package it.scvnsc.whoknows.ui.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.scvnsc.whoknows.ui.screens.LoginCredentials

class LoginViewModel : ViewModel(){
    private val _isLoggedIn = MutableLiveData(false) //inizialmente l'utente non è loggato
    val isLoggedIn: LiveData<Boolean> get() = _isLoggedIn


    fun checkLogin(loginCredentials: LoginCredentials, context: Context): Boolean{
        //Logica di autenticazione
        if (loginCredentials.isNotEmpty() && loginCredentials.login == "admin" && loginCredentials.password == "admin") {
            _isLoggedIn.value = true
            return true
        } else {
            Toast.makeText(context, "Wrong Credentials", Toast.LENGTH_SHORT).show()
        }
        return false
    }

}
