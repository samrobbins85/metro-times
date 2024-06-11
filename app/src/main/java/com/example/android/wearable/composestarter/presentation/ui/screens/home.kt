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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.android.wearable.composestarter.presentation.ui.StationState
import com.example.android.wearable.composestarter.presentation.ui.StationViewModel
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.layout.ScalingLazyColumnDefaults
import com.google.android.horologist.compose.layout.ScreenScaffold
import com.google.android.horologist.compose.material.Chip
import com.google.android.horologist.compose.material.ListHeaderDefaults
import com.google.android.horologist.compose.material.ResponsiveListHeader
import com.google.android.horologist.compose.rotaryinput.rotaryWithScroll

@OptIn(ExperimentalHorologistApi::class)
@Composable
fun GreetingScreen(onShowList: () -> Unit) {
    val scrollState = rememberScrollState()

    /* If you have enough items in your list, use [ScalingLazyColumn] which is an optimized
     * version of LazyColumn for wear devices with some added features. For more information,
     * see d.android.com/wear/compose.
     */
    ScreenScaffold(scrollState = scrollState) {
        val padding = ScalingLazyColumnDefaults.padding(
            first = ScalingLazyColumnDefaults.ItemType.Text,
            last = ScalingLazyColumnDefaults.ItemType.Chip
        )()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .rotaryWithScroll(scrollState)
                .padding(padding),
            verticalArrangement = Arrangement.Center
        ) {
            Greeting()
            Chip(label = "Show List", onClick = onShowList, colors = ChipDefaults.secondaryChipColors())
        }
    }
}

@Composable
fun Greeting() {
    val stationViewModel: StationViewModel = viewModel()
    val stationState = stationViewModel.stationState

    ResponsiveListHeader(contentPadding = ListHeaderDefaults.firstItemPadding()) {
        when (stationState) {
            is StationState.Loading -> Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary,
                text = "Loading"
            )

            is StationState.Success ->
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.primary,
                    text = "There are ${stationState.stations.size} stations"
                )

            is StationState.Error -> Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary,
                text = "Error"
            )

        }

    }
}
