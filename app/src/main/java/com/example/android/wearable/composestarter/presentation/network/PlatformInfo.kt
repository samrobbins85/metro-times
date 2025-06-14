package com.example.android.wearable.composestarter.presentation.network

import kotlinx.serialization.Serializable

@Serializable
data class Coordinates(
    val longitude: Double,
    val latitude: Double
)

@Serializable
data class PlatformInfo(
    val platformNumber: Int,
    val direction: String,
    val helperText: String,
    val coordinates: Coordinates
)
