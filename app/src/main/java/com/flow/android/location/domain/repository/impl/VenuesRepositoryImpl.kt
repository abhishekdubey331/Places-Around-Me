package com.flow.android.location.domain.repository.impl

import com.flow.android.location.api.VenueRecommendationsQueryBuilder
import com.flow.android.location.api.VenuesService
import com.flow.android.location.api.model.Result
import com.flow.android.location.domain.repository.contract.VenueRepository
import javax.inject.Inject

class VenuesRepositoryImpl @Inject constructor(
    private val venuesService: VenuesService
) : VenueRepository {

    /**
     * This functions returns the list of venues from Api Result
     *   @param latitude Latitude of user's current location
     *   @param longitude Longitude of user's current location
     * @return List<Result> return list of venues
     */
    override suspend fun fetchVenues(latitude: Double, longitude: Double): List<Result> {
        val query = VenueRecommendationsQueryBuilder()
            .setLatitudeLongitude(latitude, longitude)
            .build()
        return venuesService.getVenueRecommendations(query).results
    }
}
