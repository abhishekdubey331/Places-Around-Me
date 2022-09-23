package com.flow.android.location.utils

import android.content.Context
import com.flow.android.location.R

interface StringUtils {
    fun noNetworkErrorMessage(): String
    fun somethingWentWrong(): String
}

class StringUtilsImpl(private val appContext: Context) : StringUtils {
    override fun noNetworkErrorMessage() =
        appContext.getString(R.string.message_no_network_connected_str)

    override fun somethingWentWrong() =
        appContext.getString(R.string.message_something_went_wrong_str)
}
