package com.example.android.wearable.composestarter.presentation.network

interface NexusRepository {
    suspend fun getStations(): Map<String, String>
    suspend fun getPlatforms(): Map<String, List<PlatformInfo>>
}

class NetworkNexusRepository(): NexusRepository{
    override suspend fun getStations(): Map<String, String> {
        return NexusApi.retrofitService.getStations()
    }

    override suspend fun getPlatforms(): Map<String, List<PlatformInfo>> {
        return NexusApi.retrofitService.getPlatforms()
    }
}
