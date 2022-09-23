package com.flow.android.location.domain.repository.contract

import com.flow.android.location.api.model.Result

interface VenueRepository {

    suspend fun fetchVenues(latitude: Double, longitude: Double): List<Result>

}
