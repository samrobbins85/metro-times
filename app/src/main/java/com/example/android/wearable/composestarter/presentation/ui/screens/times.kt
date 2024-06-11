package com.example.android.wearable.composestarter.presentation.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.CardDefaults
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TitleCard
import com.example.android.wearable.composestarter.presentation.ui.TimeState
import com.example.android.wearable.composestarter.presentation.ui.TimeViewModel
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.layout.ScalingLazyColumn
import com.google.android.horologist.compose.layout.ScalingLazyColumnDefaults
import com.google.android.horologist.compose.layout.ScreenScaffold
import com.google.android.horologist.compose.layout.rememberResponsiveColumnState
import com.google.android.horologist.compose.material.ListHeaderDefaults
import com.google.android.horologist.compose.material.ResponsiveListHeader
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

private fun formatDate(date: String): String {
    val formatter = DateTimeFormatter.ofPattern("h:mm a")
    val parsed = OffsetDateTime.parse(date)
    return parsed.format(formatter)
}

data class LineColors(
    val bg: Long,
    val title: Long,
    val label: Long
)

val lineColors =
    mapOf(
        "GREEN" to LineColors(bg = 0xFF132D21, title = 0xFF3DD68C, label = 0xFFB1F1CB),
        "YELLOW" to LineColors(bg = 0xFF2D2305, title = 0xFFF5E147, label = 0xFFF6EEB4),
    )

@OptIn(ExperimentalHorologistApi::class)
@Composable
fun TimesScreen(station: String, platform: String) {
    val timeViewModel: TimeViewModel = viewModel(factory = TimeViewModel.factory(station, platform))
    val timeState = timeViewModel.timeState
    val columnState = rememberResponsiveColumnState(
        contentPadding = ScalingLazyColumnDefaults.padding(
            first = ScalingLazyColumnDefaults.ItemType.Text,
            last = ScalingLazyColumnDefaults.ItemType.SingleButton
        )
    )
    ScreenScaffold(scrollState = columnState) {
        when (timeState) {
            is TimeState.Loading -> CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                strokeWidth = 4.dp
            )

            is TimeState.Error -> Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary,
                text = "Error"
            )

            is TimeState.Success ->
                ScalingLazyColumn(
                    columnState = columnState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        ResponsiveListHeader(contentPadding = ListHeaderDefaults.firstItemPadding()) {
                            Text(text = "Times")
                        }
                    }
                    items(timeState.times.toList()) { time ->
                        TitleCard(
                            onClick = {},
                            title = {
                                Text(
                                    time.destination,
                                    color = Color(lineColors[time.line]?.title ?: 0xFF70B8FF)
                                )
                            },
                            backgroundPainter = CardDefaults.cardBackgroundPainter(
                                startBackgroundColor = Color(
                                    lineColors[time.line]?.bg ?: 0xFF0D2847
                                ),
                                endBackgroundColor = Color(
                                    lineColors[time.line]?.bg ?: 0xFF0D2847
                                )
                            ),
                            time = {
                                Text(
                                    text = "${time.dueIn}m",
                                    color = Color(lineColors[time.line]?.label ?: 0xFFC2E6FF)
                                )
                            }) {
                            Text(
                                text = "${
                                    time.lastEvent.lowercase()
                                        .replaceFirstChar { char -> char.uppercase() }
                                } ${time.lastEventLocation} at ${formatDate(time.lastEventTime)}",
                                fontSize = 12.sp,
                                color = Color(lineColors[time.line]?.label ?: 0xFFC2E6FF)
                            )
                        }
                    }
                }
        }
    }
}
