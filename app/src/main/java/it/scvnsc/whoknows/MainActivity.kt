package it.scvnsc.whoknows

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.scvnsc.whoknows.services.NetworkMonitorService
import it.scvnsc.whoknows.ui.screens.views.HomeView
import it.scvnsc.whoknows.ui.screens.views.GameView
import it.scvnsc.whoknows.ui.screens.views.SettingsView
import it.scvnsc.whoknows.ui.screens.views.StatsView
import it.scvnsc.whoknows.ui.screens.views.showExitConfirmationDialog
import it.scvnsc.whoknows.ui.theme.WhoKnowsTheme
import it.scvnsc.whoknows.ui.viewmodels.GameViewModel
import it.scvnsc.whoknows.ui.viewmodels.SettingsViewModel
import it.scvnsc.whoknows.ui.viewmodels.StatsViewModel

class MainActivity : ComponentActivity() {
    private lateinit var gameViewModel: GameViewModel
    private lateinit var navController: NavHostController
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var statsViewModel: StatsViewModel

    //TODO Complessivo:
    // sistemare salvataggio impostazioni (shared preferences) suono che non si disattiva
    // sistemare bug retrieve categorie in game view se manca internet
    // sistemare comportamento back button in game view (uscire da partita)
    // sistemare bug scelta multipla della risposta alla domanda
    // aggiungere musica di sottofondo partita
    // migliorare schermata game over
    // finire grafica schermata partite passate
    // sistemare landscape
    // aggiungere vite


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        //TODO: da sistemare
        // Register the back press callback
        /*
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d("WhoKnows", "Back pressed")
                if (navController.currentDestination?.route == "game" &&
                    gameViewModel.isPlaying.value == true &&
                    gameViewModel.isGameOver.value == false) {
                    showExitConfirmationDialog(context = this@MainActivity, gameViewModel)
                } else {
                    finish()
                }
            }
        })
        */

        //avvio il monitoraggio della rete
        NetworkMonitorService.startMonitoring(this)

        Log.d("WhoKnows", "Network monitoring service started...")

        // Inizializzo l'app normalmente
        setContent {
            WhoKnowsTheme {
                Scaffold {
                    NavControlHost()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        //avvio il monitoraggio della rete
        NetworkMonitorService.startMonitoring(this)
        Log.d("WhoKnows", "Network monitoring service started...")
    }


    //Viene chiamata durante la chiusura dell'applicazione
    override fun onDestroy() {
        super.onDestroy()

        //Ferma il monitoraggio della rete
        NetworkMonitorService.stopMonitoring(this)
        Log.d("WhoKnows", "Network monitoring service stopped...")
    }

    //Viene chiamata durante la chiusura forzata del processo (es. rotazione schermo)
    override fun onStop() {
        super.onStop()

        //Ferma il monitoraggio della rete
        NetworkMonitorService.stopMonitoring(this)
        Log.d("WhoKnows", "Network monitoring service stopped...")
    }


    @Composable
    private fun NavControlHost() {
        /*
        val navController = rememberNavController()
        val settingsViewModel: SettingsViewModel = viewModel<SettingsViewModel>()
        val gameViewModel: GameViewModel = viewModel<GameViewModel>()
        val statsViewModel: StatsViewModel = viewModel<StatsViewModel>()
         */
        navController = rememberNavController()
        settingsViewModel = viewModel<SettingsViewModel>()
        gameViewModel = viewModel<GameViewModel>()
        statsViewModel = viewModel<StatsViewModel>()

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

