package com.alphazit.nihomeadmin.models

import androidx.compose.runtime.Immutable

@Immutable
data class HouseData(
    val id: String = "",
    val name: String = "",
    val location: String = "",
    val price: String = "",
    val size: String = "",
    val phoneNumber: String = "",
    val status: String = "Vacant",
    val type: String = "One Bedroom",
    val amenities: List<String> = emptyList(),
    val imageUrls: List<String> = emptyList()
)