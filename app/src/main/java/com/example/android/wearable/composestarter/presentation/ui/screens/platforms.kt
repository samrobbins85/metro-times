package com.example.android.wearable.composestarter.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.CardDefaults
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TitleCard
import com.example.android.wearable.composestarter.presentation.ui.PlatformState
import com.example.android.wearable.composestarter.presentation.ui.StationState
import com.example.android.wearable.composestarter.presentation.ui.StationViewModel
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.layout.ScalingLazyColumn
import com.google.android.horologist.compose.layout.ScalingLazyColumnDefaults
import com.google.android.horologist.compose.layout.ScreenScaffold
import com.google.android.horologist.compose.layout.rememberResponsiveColumnState
import com.google.android.horologist.compose.material.Chip
import com.google.android.horologist.compose.material.ListHeaderDefaults
import com.google.android.horologist.compose.material.ResponsiveListHeader
import com.google.android.horologist.compose.rotaryinput.rotaryWithScroll

@OptIn(ExperimentalHorologistApi::class)
@Composable
fun PlatformScreen(station: String?, selectPlatform: (String)->Unit){
    val scrollState = rememberScrollState()
    val stationViewModel: StationViewModel = viewModel()
    val platformState = stationViewModel.platformState

    val columnState = rememberResponsiveColumnState(
        contentPadding = ScalingLazyColumnDefaults.padding(
            first = ScalingLazyColumnDefaults.ItemType.Text,
            last = ScalingLazyColumnDefaults.ItemType.SingleButton
        )
    )

    ScreenScaffold(scrollState = columnState) {
        /*
         * The Horologist [ScalingLazyColumn] takes care of the horizontal and vertical
         * padding for the list, so there is no need to specify it, as in the [GreetingScreen]
         * composable.
         */
        when (platformState) {
            is PlatformState.Loading -> CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                strokeWidth = 4.dp
            )

            is PlatformState.Error -> Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary,
                text = "Error"
            )

            is PlatformState.Success ->
                ScalingLazyColumn(
                    columnState = columnState,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    println(platformState.platforms)
                    val platforms = platformState.platforms[station]
                    println(platforms)
                    if(platforms?.isEmpty() == false) {
                        item {
                            ResponsiveListHeader(contentPadding = ListHeaderDefaults.firstItemPadding()) {
                                Text(text = "Platforms")
                            }
                        }
                        items(platforms) { platform ->
                            TitleCard(onClick = { selectPlatform(platform.platformNumber.toString())}, title = { Text("Platform ${platform.platformNumber}") }) {
                                Text(text = platform.helperText)
                            }
                        }
                    }

                }
        }

    }

}
