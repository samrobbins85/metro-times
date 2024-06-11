package com.example.android.wearable.composestarter.presentation.network

import kotlinx.serialization.Serializable

@Serializable
data class PlatformInfo(
    val platformNumber: Int,
    val direction: String,
    val helperText: String,
)
