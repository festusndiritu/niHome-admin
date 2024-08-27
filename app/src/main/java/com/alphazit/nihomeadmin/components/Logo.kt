package com.alphazit.nihomeadmin.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Logo(modifier: Modifier = Modifier) {
    Icon(
        imageVector = Icons.Default.Home,
        contentDescription = null,
        tint = Color.Gray,
        modifier = modifier.size(70.dp)
    )
}