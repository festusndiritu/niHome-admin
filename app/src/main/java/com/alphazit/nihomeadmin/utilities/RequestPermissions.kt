package com.alphazit.nihomeadmin.utilities

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
fun requestPermissions(): Boolean {
    var permissionGranted by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            permissionGranted = permissions.values.all { it }
            if (!permissionGranted) {
                // Handle the case where permissions are not granted
                Toast.makeText(context, "Permissions are required to proceed", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    )

    LaunchedEffect(key1 = Unit) {
        permissionLauncher.launch(
            arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.INTERNET
            )
        )
    }

    return permissionGranted
}
