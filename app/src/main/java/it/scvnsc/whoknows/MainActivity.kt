package it.scvnsc.whoknows

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.SystemClock.sleep
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.scvnsc.whoknows.data.db.DatabaseWK
import it.scvnsc.whoknows.data.model.Question
import it.scvnsc.whoknows.ui.screens.views.HomeView
import it.scvnsc.whoknows.ui.screens.views.GameView
import it.scvnsc.whoknows.ui.screens.views.SettingsView
import it.scvnsc.whoknows.ui.screens.views.StatsView
import it.scvnsc.whoknows.ui.theme.WhoKnowsTheme
import it.scvnsc.whoknows.ui.viewmodels.GameViewModel
import it.scvnsc.whoknows.ui.viewmodels.SettingsViewModel

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WhoKnowsTheme() {
                Scaffold() {
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

        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                HomeView(navController, settingsViewModel)
            }

            composable("stats") {
                StatsView(navController)
            }

            composable("settings") {
                SettingsView(navController)
            }

            composable("game") {
                GameView(navController, gameViewModel, settingsViewModel)
            }

        }

    }
}

