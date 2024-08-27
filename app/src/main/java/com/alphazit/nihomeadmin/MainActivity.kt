package com.alphazit.nihomeadmin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.alphazit.nihomeadmin.ui.theme.NiHomeAdminTheme
import com.alphazit.nihomeadmin.utilities.AuthGate
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    // Initialize Firebase Auth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            NiHomeAdminTheme {
                AuthGate(auth)
            }
        }
    }
}