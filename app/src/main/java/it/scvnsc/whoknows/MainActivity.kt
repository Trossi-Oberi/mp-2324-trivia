package it.scvnsc.whoknows

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.scvnsc.whoknows.ui.screens.views.HomeView
import it.scvnsc.whoknows.ui.screens.LoginForm
import it.scvnsc.whoknows.ui.screens.RegistrationForm
import it.scvnsc.whoknows.ui.screens.views.GameView
import it.scvnsc.whoknows.ui.screens.views.SettingsView
import it.scvnsc.whoknows.ui.screens.views.StatsView
import it.scvnsc.whoknows.ui.theme.WhoKnowsTheme
import it.scvnsc.whoknows.ui.viewmodels.LoginViewModel
import it.scvnsc.whoknows.ui.viewmodels.RegistrationViewModel

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WhoKnowsTheme {
                Scaffold() {
                    NavControlHost()
                }
            }
        }

        //TODO: da sistemare
//        val triviaApiService = TriviaApiService(applicationContext)
//        lifecycleScope.launch {
//            val questions = triviaApiService.getQuestions(15)
//            questions.forEach { question ->
//                Log.d("Debug", "Question: ${question.question}")
//            }
//        }


    }

    @Composable
    private fun NavControlHost() {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "login" ) {
            composable("login") {
                val loginViewModel = viewModel<LoginViewModel>()

                LoginForm(loginViewModel, navController)
            }

            composable("registration") {
                val registrationViewModel = viewModel<RegistrationViewModel>()

                RegistrationForm(registrationViewModel, navController)
            }

            composable("home") {
                HomeView(navController)
            }

            composable("stats") {
                StatsView(navController)
            }

            composable("settings") {
                SettingsView(navController)
            }

            composable("game") {
                GameView(navController)
            }
        }

    }

    //TODO: NICOLAS
    /*
     - creazione model Game, UserGame
     - creazione db usando SQLiteStudio
     - inizio integrazione DAO, logica di recupero informazioni dal db e scrittura sul db
     */

    //TODO: MATTEO
    /*
     - risolvere problema GET da API Trivia
     - creazione interfacce Registrazione, Stats e Setting (se faccio in tempo)
     - inizio logica di registrazione
     - scheda profilo (avatar)
     - LoginSession per ridurre i tempi di accesso dovuti alle query al db
     */

}

