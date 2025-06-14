/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:OptIn(ExperimentalHorologistApi::class, ExperimentalWearFoundationApi::class)

package com.samrobbins.android.wearable.metrotimes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import androidx.wear.compose.ui.tooling.preview.WearPreviewFontScales
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.layout.AppScaffold
import com.samrobbins.android.wearable.metrotimes.theme.WearAppTheme
import com.samrobbins.android.wearable.metrotimes.ui.screens.GreetingScreen
import com.samrobbins.android.wearable.metrotimes.ui.screens.ListScreen
import com.samrobbins.android.wearable.metrotimes.ui.screens.PlatformScreen
import com.samrobbins.android.wearable.metrotimes.ui.screens.TimesScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearAppTheme {
                WearApp()

            }
        }
    }
}

@Composable
fun WearApp() {
    val navController = rememberSwipeDismissableNavController()
    AppScaffold {
        SwipeDismissableNavHost(navController = navController, startDestination = "list") {
            composable("list") {
                ListScreen(selectStation = { station: String -> navController.navigate("platforms/${station}") })
            }
            composable("platforms/{station}") { backStackEntry ->
                PlatformScreen(
                    station = backStackEntry.arguments?.getString("station"),
                    selectPlatform = { platform: String ->
                        navController.navigate(
                            "times/${
                                backStackEntry.arguments?.getString(
                                    "station"
                                )
                            }/${platform}"
                        )
                    }
                )
            }
            composable("times/{station}/{platform}") { backStackEntry ->
                TimesScreen(
                    station = backStackEntry.arguments?.getString("station").toString(),
                    platform = backStackEntry.arguments?.getString("platform")
                        .toString()
                )
            }
        }
    }

}

@WearPreviewDevices
@WearPreviewFontScales
@Composable
fun GreetingScreenPreview() {
    GreetingScreen(onShowList = {})
}

@WearPreviewDevices
@WearPreviewFontScales
@Composable
fun ListScreenPreview() {
    ListScreen(selectStation = {})
}
