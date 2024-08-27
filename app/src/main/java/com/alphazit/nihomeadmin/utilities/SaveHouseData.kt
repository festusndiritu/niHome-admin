package com.alphazit.nihomeadmin.utilities

import com.alphazit.nihomeadmin.models.HouseData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

suspend fun saveHouseData(houseData: HouseData) {
    val firestore = Firebase.firestore

    val houseRef = firestore.collection("houses").document()
    val houseWithId = houseData.copy(id = houseRef.id)

    houseRef.set(houseWithId).await()
}
