package com.flow.android.location.api

open class VenueRecommendationsQueryBuilder : VenueQueryBuilder() {
    private var latitudeLongitude: String? = null

    open fun setLatitudeLongitude(
        latitude: Double,
        longitude: Double
    ): VenueRecommendationsQueryBuilder {
        this.latitudeLongitude = "$latitude,$longitude"
        return this
    }

    override fun putQueryParams(queryParams: MutableMap<String, String>) {
        latitudeLongitude?.apply { queryParams["ll"] = this }
    }
}
