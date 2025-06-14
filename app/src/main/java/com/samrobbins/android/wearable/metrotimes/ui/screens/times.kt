package com.samrobbins.android.wearable.metrotimes.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
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
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.layout.ScalingLazyColumn
import com.google.android.horologist.compose.layout.ScalingLazyColumnDefaults
import com.google.android.horologist.compose.layout.ScreenScaffold
import com.google.android.horologist.compose.layout.rememberResponsiveColumnState
import com.google.android.horologist.compose.material.Button
import com.google.android.horologist.compose.material.ButtonSize
import com.samrobbins.android.wearable.metrotimes.ui.TimeState
import com.samrobbins.android.wearable.metrotimes.ui.TimeViewModel
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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
            first = ScalingLazyColumnDefaults.ItemType.Card,
            last = ScalingLazyColumnDefaults.ItemType.SingleButton
        )
    )
    val infiniteTransition = rememberInfiniteTransition(label = "InfiniteTransition")

    val angle by infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = 360F,
        animationSpec = infiniteRepeatable(animation = tween(1000, easing = LinearEasing)),
        label = "FloatInfiniteTransition"
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
                                    text = if (time.dueIn > 0) "${time.dueIn}m" else "Now",
                                    color = Color(lineColors[time.line]?.label ?: 0xFFC2E6FF)
                                )
                            }) {
                            Text(
                                text = if (time.lastEvent != "READY_TO_START") "${
                                    time.lastEvent.lowercase()
                                        .replaceFirstChar { char -> char.uppercase() }
                                } ${time.lastEventLocation} at ${formatDate(time.lastEventTime)}" else "",
                                fontSize = 12.sp,
                                color = Color(lineColors[time.line]?.label ?: 0xFFC2E6FF)
                            )
                        }
                    }
                    item {
                        Button(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            onClick = {
                                timeViewModel.getTimes()
                                runBlocking {
                                    launch {
                                        columnState.scrollBy(-10000f)
                                    }
                                }
                            },
                            buttonSize = ButtonSize.Small,
                            modifier = Modifier.rotate(if (timeState.loading) angle else 0f)
                        )
                    }
                }
        }
    }
}
