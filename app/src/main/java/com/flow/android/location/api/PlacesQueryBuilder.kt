package com.flow.android.location.api

import java.text.SimpleDateFormat
import java.util.*

abstract class VenueQueryBuilder {

    fun build(): Map<String, String> {
        val queryParams = hashMapOf("v" to dateFormat.format(Date()))
        putQueryParams(queryParams)
        return queryParams
    }

    abstract fun putQueryParams(queryParams: MutableMap<String, String>)

    companion object {
        private val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.ROOT)
    }
}
