package it.scvnsc.whoknows

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.scvnsc.whoknows.ui.screens.views.HomeView
import it.scvnsc.whoknows.ui.screens.views.GameView
import it.scvnsc.whoknows.ui.screens.views.SettingsView
import it.scvnsc.whoknows.ui.screens.views.StatsView
import it.scvnsc.whoknows.ui.theme.WhoKnowsTheme
import it.scvnsc.whoknows.ui.viewmodels.GameViewModel
import it.scvnsc.whoknows.ui.viewmodels.SettingsViewModel
import it.scvnsc.whoknows.ui.viewmodels.StatsViewModel

class MainActivity : ComponentActivity() {

    //TODO Complessivo:
    // -Schermata di quando si perde la partita con animazione che cade dall'alto e pulsanti New Game - Main Menu
    // -Schermata impostazioni con pulsanti credits (link github), theme (light/dark), sound (on/off)
    // -Schermata partite passate migliorare grafica
    // -Controlli landscape ed adattamento schermate
    // -Schermata record
    // -Controllo della connessione internet

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WhoKnowsTheme {
                Scaffold {
                    NavControlHost()
                }
            }
        }
    }


    @Composable
    private fun NavControlHost() {
        val navController = rememberNavController()
        val settingsViewModel: SettingsViewModel = viewModel<SettingsViewModel>()
        val gameViewModel: GameViewModel = viewModel<GameViewModel>()
        val statsViewModel: StatsViewModel = viewModel<StatsViewModel>()

        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                HomeView(navController, settingsViewModel)
            }

            composable("stats") {
                StatsView(navController, statsViewModel, settingsViewModel)
            }

            composable("settings") {
                SettingsView(navController, settingsViewModel)
            }

            composable("game") {
                GameView(navController, gameViewModel, settingsViewModel)
            }

        }

    }
}

