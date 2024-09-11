package org.scvnsc.whoknows

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.scvnsc.whoknows.ui.screens.HomeScreen
import org.scvnsc.whoknows.ui.screens.LoginScreen
import org.scvnsc.whoknows.ui.theme.WhoKnowsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WhoKnowsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    //NavControlHost()
                    navigateToLoginScreen()
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

    private fun navigateToLoginScreen() {
        val intent = Intent(this, LoginScreen::class.java)
        startActivity(intent)
    }

//    @Composable
//    private fun NavControlHost() {
//        val navController = rememberNavController()
//
//        NavHost(navController = navController, startDestination = "login" ) {
//            composable("login") {
//                LoginScreen()
//            }
//
//            composable("home") {
//                HomeScreen(navController)
//            }
//        }
//
//        Log.d("Debug", "Sto navigando verso login...")
//        navController.navigate("login")
//
//    }


//    @Preview(showBackground = true)
//    @Composable
//    fun GreetingPreview() {
//        WhoKnowsTheme {
//            Greeting("Android")
//        }
//    }

}

