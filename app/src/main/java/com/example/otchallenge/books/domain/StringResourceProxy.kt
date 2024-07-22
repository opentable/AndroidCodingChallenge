package com.care.sdk.utils

import android.content.Context
import androidx.annotation.StringRes
import javax.inject.Inject

class StringResourceProxy @Inject constructor(val context: Context): StringResources {

    override fun getString(@StringRes string: Int, vararg args: String): String {
        return context.getString(string, *args)
    }
}