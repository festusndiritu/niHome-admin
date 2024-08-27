package com.alphazit.nihomeadmin.utilities

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AuthGate(auth: FirebaseAuth) {
    var user by remember { mutableStateOf(auth.currentUser) }
    val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        user = firebaseAuth.currentUser
    }
    val navController = rememberNavController()

    DisposableEffect(Unit) {
        auth.addAuthStateListener(authStateListener)
        onDispose {
            auth.removeAuthStateListener(authStateListener)
        }
    }
    NavGraph(user, navController)
}