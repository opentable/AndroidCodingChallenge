package com.quan.lam.nytimesmovies.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LinkDAO(val type: String = "",
                   val url: String = "",
                   val suggested_link_text: String = "") : Parcelable