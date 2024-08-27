package com.alphazit.nihomeadmin.utilities

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MediaPicker(modifier: Modifier = Modifier, onImagesSelected: (List<Uri>) -> Unit) {

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.isNotEmpty()) {
            onImagesSelected(uris)
        }
    }
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.TopStart) {
        ExtendedFloatingActionButton(
            text = {
                Text(text = "Choose Images")
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Pick Images"
                )
            },
            onClick = {
                launcher.launch("image/*")
            },
            modifier = modifier
        )
    }


}
