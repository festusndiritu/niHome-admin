package com.alphazit.nihomeadmin.utilities

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.alphazit.nihomeadmin.models.LogEntry
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch

@Composable
fun fetchLogs(): List<LogEntry> {
    var logs by remember { mutableStateOf<List<LogEntry>>(emptyList()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("logs")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { documents ->
                    val fetchedLogs = documents.map { document ->
                        document.toObject(LogEntry::class.java)
                    }
                    logs = fetchedLogs
                }
                .addOnFailureListener {
                    // Handle error
                }
        }
    }

    return logs
}
