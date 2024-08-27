package com.alphazit.nihomeadmin.utilities

import android.util.Log
import com.alphazit.nihomeadmin.models.LogEntry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

fun logUserAction(action: String, houseName: String) {
    val user = FirebaseAuth.getInstance().currentUser
    val firestore = FirebaseFirestore.getInstance()

    val logEntry = LogEntry(
        userName = user?.displayName ?: "Unknown User",
        action = action,
        houseName = houseName,
        timestamp = System.currentTimeMillis()
    )

    firestore.collection("logs")
        .add(logEntry)
        .addOnSuccessListener {
            // Log successfully written
        }
        .addOnFailureListener { e ->
            // Handle the error
            e.localizedMessage?.let { Log.e("Firebase error: ", it) }
        }
}
