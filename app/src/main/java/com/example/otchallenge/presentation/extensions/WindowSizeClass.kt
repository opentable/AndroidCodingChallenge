package com.example.otchallenge.presentation.extensions

import android.app.Activity
import androidx.window.layout.WindowMetricsCalculator

enum class WindowSizeClass {
    Compact,
    Medium,
    Expanded
}

val Activity.getWidthSizeClass: WindowSizeClass
    get() {
        return WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this)
            .let { metrics ->
                metrics.bounds.width() / resources.displayMetrics.density
            }
            .let { width ->
                when {
                    width < 600 -> WindowSizeClass.Compact
                    width < 840 -> WindowSizeClass.Medium
                    else -> WindowSizeClass.Expanded
                }
            }
    }

val Activity.getHeightSizeClass: WindowSizeClass
    get() {
        return WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this)
            .let { metrics ->
                metrics.bounds.height() / resources.displayMetrics.density
            }
            .let { height ->
                when {
                    height < 480 -> WindowSizeClass.Compact
                    height < 900 -> WindowSizeClass.Medium
                    else -> WindowSizeClass.Expanded
                }
            }
    }
