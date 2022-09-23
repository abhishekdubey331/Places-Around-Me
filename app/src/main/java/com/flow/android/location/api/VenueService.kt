package com.flow.android.location.api

import com.flow.android.location.api.model.ResponseWrapper
import com.flow.android.location.common.Constants
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface VenuesService {
    /**
     * Get venue recommendations.
     *
     * See [the docs](https://developer.foursquare.com/reference/places-nearby)
     */
    @GET("places/nearby?limit=${Constants.RESULT_LIMIT}")
    suspend fun getVenueRecommendations(@QueryMap query: Map<String, String>): ResponseWrapper
}
