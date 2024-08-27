package com.alphazit.nihomeadmin.utilities

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

suspend fun fetchHouseImageUrls(houseId: String): List<String>? {
    val firestore = FirebaseFirestore.getInstance()
    return try {
        val document = firestore.collection("houses")
            .whereEqualTo("id", houseId)
            .get()
            .await()
            .documents
            .firstOrNull()

        // Extract image URLs from the document
        document?.get("imageUrls") as? List<String>
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}