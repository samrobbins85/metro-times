package com.samrobbins.android.wearable.metrotimes.network

import kotlinx.serialization.Serializable

@Serializable
data class TimeInfo(
    val trn: String,
    val lastEvent: String,
    val lastEventLocation: String,
    val lastEventTime: String,
    val destination: String,
    val dueIn: Int,
    val line: String,
    val actualScheduledTime: String? = null,
    val actualPredictedTime: String? = null
)
