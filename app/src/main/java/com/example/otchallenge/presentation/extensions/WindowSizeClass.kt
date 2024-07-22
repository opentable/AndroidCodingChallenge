package com.example.otchallenge.presentation.extensions

import android.app.Activity

enum class WindowSizeClass {
    Compact,
    Medium,
    Expanded
}

val Activity.getWidthSizeClass: WindowSizeClass
    get() {
        val width = windowManager.currentWindowMetrics.bounds.width() / resources.displayMetrics.density
        return when {
            width < 600 -> WindowSizeClass.Compact
            width < 840 -> WindowSizeClass.Medium
            else -> WindowSizeClass.Expanded
        }
    }

val Activity.getHeightSizeClass: WindowSizeClass
    get() {
        val height = windowManager.currentWindowMetrics.bounds.height() / resources.displayMetrics.density
        return when {
            height < 480 -> WindowSizeClass.Compact
            height < 900 -> WindowSizeClass.Medium
            else -> WindowSizeClass.Expanded
        }
    }
