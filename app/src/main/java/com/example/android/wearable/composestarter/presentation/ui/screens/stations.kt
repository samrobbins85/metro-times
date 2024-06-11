package com.example.android.wearable.composestarter.presentation.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TitleCard
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

@OptIn(ExperimentalHorologistApi::class)
@Composable
fun ListScreen(selectStation: (String) -> Unit) {
    val stationViewModel: StationViewModel = viewModel()
    val stationState = stationViewModel.stationState
    /*
     * Specifying the types of items that appear at the start and end of the list ensures that the
     * appropriate padding is used.
     */
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
        when (stationState) {
            is StationState.Loading -> CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                strokeWidth = 4.dp
            )

            is StationState.Error -> Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary,
                text = "Error"
            )

            is StationState.Success ->
                ScalingLazyColumn(
                    columnState = columnState,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    item {
                        ResponsiveListHeader(contentPadding = ListHeaderDefaults.firstItemPadding()) {
                            Text(text = "Stations")
                        }
                    }
                    items(stationState.stations.entries.toList()) { station ->
                        Chip(
                            label = station.value,
                            onClick = { selectStation(station.key) },
                            colors = ChipDefaults.secondaryChipColors()
                        )
                    }


                }
        }

    }
}
