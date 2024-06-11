package com.example.android.wearable.composestarter.presentation.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.android.wearable.composestarter.presentation.network.NexusApi
import com.example.android.wearable.composestarter.presentation.network.TimeInfo
import java.io.IOException
import kotlinx.coroutines.launch

sealed interface TimeState {
    data class Success(val times: List<TimeInfo>, val loading: Boolean) : TimeState
    data object Loading : TimeState
    data object Error : TimeState
}

class TimeViewModel(private val station: String, private val platform: String) : ViewModel() {
    var timeState: TimeState by mutableStateOf(TimeState.Loading)

    init {
        getTimes()
    }

    fun getTimes() {

        viewModelScope.launch {
            timeState = try {
                if (timeState is TimeState.Success) {
                    timeState = TimeState.Success((timeState as TimeState.Success).times, true)
                }
                val listResult = NexusApi.retrofitService.getTimes(station, platform)
                TimeState.Success(listResult, false)
            } catch (e: IOException) {
                TimeState.Error
            }
        }
    }


    companion object {
        fun factory(station: String, platform: String): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                ): T {
                    if (modelClass.isAssignableFrom(TimeViewModel::class.java)) {
                        return TimeViewModel(station, platform) as T
                    }
                    throw IllegalArgumentException("Unknown Class")
                }
            }
        }
    }
}
