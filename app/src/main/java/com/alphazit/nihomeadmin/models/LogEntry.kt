package com.alphazit.nihomeadmin.models

import androidx.compose.runtime.Immutable

@Immutable
data class LogEntry(
    val userName: String = "Unknown User",
    val action: String = "",
    val houseName: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

