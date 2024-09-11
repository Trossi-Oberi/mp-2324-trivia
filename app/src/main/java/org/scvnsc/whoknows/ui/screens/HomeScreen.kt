package org.scvnsc.whoknows.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.scvnsc.whoknows.ui.theme.WhoKnowsTheme


class HomeScreen() : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WhoKnowsTheme {
                Scaffold (
                    modifier = Modifier
                        .fillMaxSize(),
                    contentColor = Color.Blue,
                    topBar = {
                        TopAppBar(title = {Text("WhoKnows")})
                    },
                    bottomBar = {
                        NavigationBar (
                            containerColor = Color.LightGray
                        ){
                            NavigationBarItem(
                                selected = true,
                                icon = { Icon(Icons.Default.Home, "Home") },
                                label = { Text(text = "Home") },
                                onClick = { /* Handle home click */ }
                            )
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.BarChart, "Stats") },
                                label = { Text(text = "Stats") },
                                selected = false,
                                onClick = { /* Handle stats click */ }
                            )
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.Settings, "Settings") },
                                label = { Text(text = "Settings") },
                                selected = false,
                                onClick = { /* Handle settings click */ }
                            )
                        }
                    },
                    content = {
                        Column (
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize()
                        ){
                            Text("Benvenuto nella home!")
                        }
                    }
                )
            }
        }
    }

}
