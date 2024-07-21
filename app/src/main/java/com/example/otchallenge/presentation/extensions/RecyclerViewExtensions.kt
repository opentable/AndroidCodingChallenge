package com.example.otchallenge.presentation.extensions

import androidx.recyclerview.widget.RecyclerView

val RecyclerView.Adapter<*>.isEmpty: Boolean
    get() { return itemCount == 0 }
