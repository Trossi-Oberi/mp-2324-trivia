package it.scvnsc.whoknows

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.scvnsc.whoknows.services.NetworkMonitorService
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
    // -Aggiungere vite
    // -Fare che il punteggio dato dalla domanda varia a seconda della difficolta'

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //avvio il monitoraggio della rete
        //NetworkMonitorService.startMonitoring(this)
        //Log.d("NetworkState", "Network monitoring service started...")

        setContent {
            WhoKnowsTheme {
                Scaffold {
                    NavControlHost()
                }
            }
        }
    }

    /*

    //Viene chiamata durante la chiusura dell'applicazione
    override fun onDestroy() {
        super.onDestroy()

        //Ferma il monitoraggio della rete
        NetworkMonitorService.stopMonitoring(this)
        Log.d("NetworkState", "Network monitoring service stopped...")
    }

    //Viene chiamata durante la chiusura forzata del processo (es. rotazione schermo)
    override fun onStop() {
        super.onStop()

        //Ferma il monitoraggio della rete
        NetworkMonitorService.stopMonitoring(this)
        Log.d("NetworkState", "Network monitoring service stopped...")
    }

     */


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

