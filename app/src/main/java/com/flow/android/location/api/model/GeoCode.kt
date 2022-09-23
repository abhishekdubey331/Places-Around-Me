package com.flow.android.location.api.model

data class GeoCode(
    val main: Main
)

data class Main(
    val latitude: Double,
    val longitude: Double,
)