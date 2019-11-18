package com.quan.lam.nytimesmovies.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MultimediaDAO(val type: String = "",
                         val src: String = "",
                         val width: Int = 0,
                         val height: Int = 0): Parcelable