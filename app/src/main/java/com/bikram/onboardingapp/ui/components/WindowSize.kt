package com.bikram.onboardingapp.ui.components

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.window.layout.WindowMetricsCalculator

sealed class WindowSize(val size: DpSize) {
    class Compact(windowDpSize: DpSize) : WindowSize(windowDpSize)
    class Medium(windowDpSize: DpSize) : WindowSize(windowDpSize)
    class Expanded(windowDpSize: DpSize) : WindowSize(windowDpSize)
}

@Composable
fun Activity.rememberWindowSizeClass(): WindowSize {
    // Get the size (in pixels) of the window
    val windowSize = rememberWindowSize()

    // Convert the window size to [Dp]
    val windowDpSize = with(LocalDensity.current) {
        windowSize.toDpSize()
    }

    // Calculate the window size class
    return getWindowSizeClass(windowDpSize)
}

@Composable
private fun Activity.rememberWindowSize(): Size {
    val configuration = LocalConfiguration.current
    // WindowMetricsCalculator implicitly depends on the configuration through the activity,
    // so re-calculate it upon changes.
    val windowMetrics = remember(configuration) {
        WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this)
    }
    return windowMetrics.bounds.toComposeRect().size
}

private fun getWindowSizeClass(windowDpSize: DpSize): WindowSize = when {
    windowDpSize.width < 0.dp -> throw IllegalArgumentException("Dp value cannot be negative")
    windowDpSize.width < 600.dp -> WindowSize.Compact(windowDpSize)
    windowDpSize.width < 840.dp -> WindowSize.Medium(windowDpSize)
    else -> WindowSize.Expanded(windowDpSize)
}