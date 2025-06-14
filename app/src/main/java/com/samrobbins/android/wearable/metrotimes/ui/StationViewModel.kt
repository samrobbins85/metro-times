package com.samrobbins.android.wearable.metrotimes.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samrobbins.android.wearable.metrotimes.network.NetworkNexusRepository
import com.samrobbins.android.wearable.metrotimes.network.PlatformInfo
import java.io.IOException
import kotlinx.coroutines.launch

sealed interface StationState {
    data class Success(val stations: Map<String, String>) : StationState
    data object Loading : StationState
    data object Error : StationState
}

sealed interface PlatformState {
    data class Success(val platforms: Map<String, List<PlatformInfo>>) : PlatformState
    data object Loading : PlatformState
    data object Error : PlatformState
}


class StationViewModel : ViewModel() {
    var stationState: StationState by mutableStateOf(StationState.Loading)
        private set
    var platformState: PlatformState by mutableStateOf(PlatformState.Loading)
        private set
    private val nexusRepository = NetworkNexusRepository()

    init {
        getStations()
        getPlatforms()
    }

    private fun getStations() {
        viewModelScope.launch {
            stationState = try {
                val listResult = nexusRepository.getStations()
                StationState.Success(listResult)
            } catch (e: IOException) {
                StationState.Error
            }
        }
    }

    private fun getPlatforms() {
        viewModelScope.launch {
            platformState = try {
                val listResult = nexusRepository.getPlatforms()
                PlatformState.Success(listResult)
            } catch (e: IOException) {
                PlatformState.Error
            }
        }
    }

}

