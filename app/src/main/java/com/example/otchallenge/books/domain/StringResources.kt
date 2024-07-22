package com.care.sdk.utils

import androidx.annotation.StringRes

interface StringResources {
    fun getString(@StringRes string: Int, vararg args: String): String
}