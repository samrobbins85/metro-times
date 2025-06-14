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

package com.example.android.wearable.composestarter.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.material.Text
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import androidx.wear.compose.ui.tooling.preview.WearPreviewFontScales
import com.example.android.wearable.composestarter.presentation.theme.WearAppTheme
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.layout.AppScaffold
import com.example.android.wearable.composestarter.presentation.ui.screens.GreetingScreen
import com.example.android.wearable.composestarter.presentation.ui.screens.ListScreen
import com.example.android.wearable.composestarter.presentation.ui.screens.PlatformScreen
import com.example.android.wearable.composestarter.presentation.ui.screens.TimesScreen

/**
 * Simple "Hello, World" app meant as a starting point for a new project using Compose for Wear OS.
 *
 * Displays a centered [Text] composable and a list built with [Horologist]
 * (https://github.com/google/horologist).
 *
 * Use the Wear version of Compose Navigation. You can carry
 * over your knowledge from mobile and it supports the swipe-to-dismiss gesture (Wear OS's
 * back action). For more information, go here:
 * https://developer.android.com/reference/kotlin/androidx/wear/compose/navigation/package-summary
 */
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
        SwipeDismissableNavHost(navController = navController, startDestination = "menu") {
//            composable("menu") {
//                GreetingScreen(
//                    onShowList = { navController.navigate("list") }
//                )
//            }
            composable("list") {
                ListScreen(selectStation = { station: String -> navController.navigate("platforms/${station}") })
            }
            composable("platforms/{station}") { backStackEntry ->
                PlatformScreen(
                    station = backStackEntry.arguments?.getString("station"),
                    selectPlatform = { platform: String -> navController.navigate("times/${backStackEntry.arguments?.getString("station")}/${platform}")}
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
