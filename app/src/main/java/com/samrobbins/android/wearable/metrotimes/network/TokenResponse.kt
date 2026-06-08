package com.samrobbins.android.wearable.metrotimes.network

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val token: String
)
