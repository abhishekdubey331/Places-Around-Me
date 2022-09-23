package com.flow.android.location.api.model

data class Result(
    val categories: List<Category>,
    val distance: Int,
    val geocode: GeoCode,
    val location: Location,
    val name: String,
    val timezone: String
)