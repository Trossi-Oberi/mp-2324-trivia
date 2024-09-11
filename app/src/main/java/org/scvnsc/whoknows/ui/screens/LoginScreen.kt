package org.scvnsc.whoknows.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.scvnsc.whoknows.ui.screens.views.LoginForm
import org.scvnsc.whoknows.ui.theme.WhoKnowsTheme

class LoginScreen() : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WhoKnowsTheme {
                LoginForm()
            }
        }
    }

}