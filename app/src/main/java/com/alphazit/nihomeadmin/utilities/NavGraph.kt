package com.alphazit.nihomeadmin.utilities

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alphazit.nihomeadmin.screens.HomeScreen
import com.alphazit.nihomeadmin.screens.HouseDetailsScreen
import com.alphazit.nihomeadmin.screens.ImageViewer
import com.alphazit.nihomeadmin.screens.LoginScreen
import com.alphazit.nihomeadmin.screens.LogsScreen
import com.alphazit.nihomeadmin.screens.RegisterScreen
import com.alphazit.nihomeadmin.screens.SettingsScreen
import com.alphazit.nihomeadmin.screens.UploadScreen
import com.google.firebase.auth.FirebaseUser

@Composable
fun NavGraph(user: FirebaseUser?, navController: NavHostController) {
    val startRoute = if (user != null) "home" else "login"
    NavHost(navController = navController, startDestination = startRoute) {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("upload") { UploadScreen(navController) }
        composable("logs") { LogsScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
        composable("houseDetails/{houseId}") { backStackEntry ->
            HouseDetailsScreen(
                houseId = backStackEntry.arguments?.getString("houseId") ?: "",
                navController
            )
        }
        composable("pager/{houseId}/{startIndex}") { backStackEntry ->
            val houseId = backStackEntry.arguments?.getString("houseId") ?: ""
            val startIndex = backStackEntry.arguments?.getString("startIndex")?.toInt() ?: 0
            ImageViewer(houseId = houseId, startIndex = startIndex, navController = navController)
        }
    }
}