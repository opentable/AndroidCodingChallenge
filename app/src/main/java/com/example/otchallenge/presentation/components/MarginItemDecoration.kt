package com.example.otchallenge.presentation.components

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class MarginItemDecoration(
    private val horizontal: Int = 0,
    private val vertical: Int = 0
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with (outRect) {
            top = vertical.dp(parent.context)
            bottom = 0
            left = horizontal.dp(parent.context)
            right = 0
        }

        when (val layoutManager = parent.layoutManager) {
            is GridLayoutManager -> {
                layoutManager.handleGridLayoutMargins(outRect, view, parent)
            }
            is LinearLayoutManager -> {
                layoutManager.handleLinearLayoutMargins(outRect, view, parent)
            }
        }
    }

    private fun LinearLayoutManager.handleLinearLayoutMargins(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
    ) {
        val childPosition = parent.getChildLayoutPosition(view)

        when (orientation) {
            LinearLayoutManager.HORIZONTAL -> {
                outRect.top = 0
                if (childPosition == 0) {
                    outRect.left = 0
                }
            }
            LinearLayoutManager.VERTICAL -> {
                outRect.left = 0
                if (childPosition == 0) {
                    outRect.top = 0
                }
            }
        }
    }

    private fun GridLayoutManager.handleGridLayoutMargins(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
    ) {
        val childPosition = parent.getChildAdapterPosition(view)
        with (outRect) {
            if (childPosition < spanCount) {
                top = 0
            }
            if (childPosition % spanCount == 0) {
                left = 0
            }
        }
    }

    private fun Int.dp(context: Context): Int {
        return this * context.resources.displayMetrics.density.roundToInt()
    }
}