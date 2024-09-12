package it.scvnsc.whoknows.ui.screens.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsView(navController: NavHostController) {

    Scaffold (
        modifier = Modifier
            .fillMaxSize(),

        contentColor = Color.Blue,
        topBar = {
            TopAppBar(title = { Text("WhoKnows") })
        },
        bottomBar = {
            NavigationBar (
                containerColor = Color.LightGray
            ){
                NavigationBarItem(
                    selected = false,
                    icon = { Icon(Icons.Default.Home, "Home") },
                    label = { Text(text = "Home") },
                    enabled = true,
                    onClick = {
                        navController.navigate("home")
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.BarChart, "Stats") },
                    label = { Text(text = "Stats") },
                    selected = false,
                    enabled = true,
                    onClick = {
                        navController.navigate("stats")
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, "Settings") },
                    label = { Text(text = "Settings") },
                    selected = true,
                    enabled = false,
                    onClick = { /* non deve succedere nulla */}
                )
            }
        },
        content = { padding ->
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            ){
                Text(
                    color = Color.Green,
                    text = "Benvenuto nelle impostazioni!"
                )
            }
        }
    )
}