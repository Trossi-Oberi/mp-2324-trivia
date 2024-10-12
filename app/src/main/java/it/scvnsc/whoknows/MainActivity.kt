package it.scvnsc.whoknows

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.scvnsc.whoknows.services.NetworkMonitorService
import it.scvnsc.whoknows.ui.screens.views.ExitConfirmationDialog
import it.scvnsc.whoknows.ui.screens.views.HomeView
import it.scvnsc.whoknows.ui.screens.views.GameView
import it.scvnsc.whoknows.ui.screens.views.SettingsView
import it.scvnsc.whoknows.ui.screens.views.StatsView
import it.scvnsc.whoknows.ui.theme.WhoKnowsTheme
import it.scvnsc.whoknows.ui.viewmodels.GameViewModel
import it.scvnsc.whoknows.ui.viewmodels.SettingsViewModel
import it.scvnsc.whoknows.ui.viewmodels.StatsViewModel

class MainActivity : ComponentActivity() {
    private lateinit var gameViewModel: GameViewModel
    private lateinit var navController: NavHostController
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var statsViewModel: StatsViewModel

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //avvio il monitoraggio della rete
        NetworkMonitorService.startMonitoring(this)

        Log.d("MainActivity", "Network monitoring service started...")

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
        Log.d("MainActivity", "Network monitoring service started...")
    }


    //Viene chiamata durante la chiusura dell'applicazione
    override fun onDestroy() {
        super.onDestroy()

        //Ferma il monitoraggio della rete
        NetworkMonitorService.stopMonitoring(this)
        Log.d("MainActivity", "Network monitoring service stopped...")
    }

    //Viene chiamata durante la chiusura forzata del processo (es. rotazione schermo)
    override fun onStop() {
        super.onStop()

        //Ferma il monitoraggio della rete
        NetworkMonitorService.stopMonitoring(this)
        Log.d("MainActivity", "Network monitoring service stopped...")
    }


    @Composable
    private fun NavControlHost() {
        navController = rememberNavController()
        settingsViewModel = viewModel<SettingsViewModel>()
        gameViewModel = viewModel<GameViewModel>()
        statsViewModel = viewModel<StatsViewModel>()

        NavHost(
            navController = navController,
            startDestination = "home",
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            composable(
                "home",
                enterTransition = {
                    slideIntoContainer(
                        animationSpec = tween(400, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        animationSpec = tween(400, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                }
            ) {
                HomeView(navController, settingsViewModel)
            }

            composable(
                "stats",
                enterTransition = {
                    slideIntoContainer(
                        animationSpec = tween(400, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        animationSpec = tween(400, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                }
            ) {
                BackHandler {
                    if (statsViewModel.showGameDetails.value == true) {
                        statsViewModel.setShowGameDetails(false)
                        statsViewModel.setGameQuestionsReady(false)
                    } else {
                        navController.popBackStack()
                    }
                }

                StatsView(navController, statsViewModel, settingsViewModel)
            }

            composable(
                "settings",
                enterTransition = {
                    slideIntoContainer(
                        animationSpec = tween(400, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        animationSpec = tween(400, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                }
            ) {
                SettingsView(navController, settingsViewModel)
            }

            composable(
                "game",
                enterTransition = {
                    slideIntoContainer(
                        animationSpec = tween(400, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        animationSpec = tween(400, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                }
            ) {
                val showExitConfirmationDialog = rememberSaveable { mutableStateOf(false) }
                val showLoading = gameViewModel.isGameTimerInterrupted.observeAsState().value

                BackHandler {
                    if(!showLoading!!) {
                        if (gameViewModel.isPlaying.value == true && gameViewModel.isGameOver.value == false && NetworkMonitorService.isOffline.value == false) {
                            // Aggiungi qui il comportamento specifico per la schermata di gioco
                            showExitConfirmationDialog.value = true

                        } else if (gameViewModel.isPlaying.value == true && gameViewModel.isGameOver.value == false && NetworkMonitorService.isOffline.value == true) {
                            //chiudo la partita e torno alla home
                            gameViewModel.onQuitGameClicked()
                            navController.popBackStack()
                            gameViewModel.setIsPlaying(false)

                        } else if (gameViewModel.isGameOver.value == true) {
                            /*setta isPlaying off e rimane su gameView*/
                            gameViewModel.setIsPlaying(false)
                            gameViewModel.clearUserAnswer()
                            gameViewModel.setGameOver(false)
                        } else {
                            navController.popBackStack()
                        }
                    }
                }

                GameView(navController, gameViewModel, settingsViewModel)

                val isDark = settingsViewModel.isDarkTheme.observeAsState().value == true

                if (showExitConfirmationDialog.value) {
                    WhoKnowsTheme(darkTheme = isDark) {
                        ExitConfirmationDialog(
                            applicationContext,
                            gameViewModel,
                            isDark,
                            showExitConfirmationDialog
                        )
                    }
                }
            }
        }
    }
}

