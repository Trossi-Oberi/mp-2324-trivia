package com.example.whoknows

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import com.example.whoknows.data.model.Question
import com.example.whoknows.data.network.TriviaApiService
import com.example.whoknows.ui.viewmodels.TriviaViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private lateinit var retrieveResult: List<Question>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Text("Hello, World!")
        }

        retrieveQuestions()

    }

    private fun retrieveQuestions() {
        val scope = CoroutineScope(Dispatchers.Main)
        val triviaApiService = TriviaApiService(applicationContext)
        scope.launch {
            val questions = triviaApiService.getQuestions(15)
            retrieveResult = questions
        }
    }

}