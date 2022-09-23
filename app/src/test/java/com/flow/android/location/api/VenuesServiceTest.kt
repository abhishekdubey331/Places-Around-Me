package com.flow.android.location.api

import com.flow.android.location.api.model.LatLong
import com.flow.android.location.base.ApiAbstract
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class VenuesServiceTest : ApiAbstract<VenuesService>() {

    companion object {
        private const val JSON_RES_FILE_NAME = "/venues_list_response.json"
    }

    private lateinit var apiService: VenuesService

    private val testLatLong = LatLong(0.0, 0.0)

    @Before
    fun setUp() {
        apiService = createService(VenuesService::class.java)
    }

    @Test
    fun `test get venue recommendations returns list of venues`() = runBlocking {
        val (lat, long) = testLatLong
        val query = VenueRecommendationsQueryBuilder()
            .setLatitudeLongitude(lat, long)
            .build()
        // Given
        enqueueResponse(JSON_RES_FILE_NAME)

        // Invoke
        val response = apiService.getVenueRecommendations(query)
        mockWebServer.takeRequest()

        // Then
        response.results.run {
            assertThat(size).isEqualTo(5)
            assertThat(first().name).isEqualTo("BREN Avalon")
            assertThat(first().categories.first().name).isEqualTo("Residential Building")
            assertThat(last().name).isEqualTo("Jaak Hydro Pneumatic Company")
        }
    }
}
