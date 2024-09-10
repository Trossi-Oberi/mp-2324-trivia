package org.scvnsc.whoknows

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.scvnsc.whoknows.data.model.Question
import org.scvnsc.whoknows.data.network.TriviaApiService
import org.scvnsc.whoknows.ui.theme.WhoKnowsTheme

class MainActivity : ComponentActivity() {
    private lateinit var retrieveResult: List<Question>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WhoKnowsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        //TODO: da sistemare
        val triviaApiService = TriviaApiService(applicationContext)
        lifecycleScope.launch {
            val questions = triviaApiService.getQuestions(15)
            retrieveResult = questions
        }

    }

//    private fun retrieveQuestions() {
//        val scope = CoroutineScope(Dispatchers.Main)
//
//        scope.launch {
//
//            retrieveResult = questions
//        }
//    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WhoKnowsTheme {
        Greeting("Android")
    }
}