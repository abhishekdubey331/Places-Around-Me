package com.flow.android.location.domain.impl

import com.flow.android.location.api.model.LatLong
import com.flow.android.location.base.MainCoroutinesRule
import com.flow.android.location.common.ResultState
import com.flow.android.location.domain.repository.contract.VenueRepository
import com.flow.android.location.domain.usecase.contract.GetVenuesUseCase
import com.flow.android.location.domain.usecase.impl.GetVenuesUseCaseImpl
import com.flow.android.location.utils.MockData
import com.flow.android.location.utils.StringUtils
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import retrofit2.HttpException
import retrofit2.Response
import org.mockito.Mockito.`when` as whenever

@OptIn(ExperimentalCoroutinesApi::class)
class GetVenuesUseCaseImplTest {

    @get:Rule
    var coroutineRule = MainCoroutinesRule()

    @Mock
    lateinit var venueRepository: VenueRepository

    @Mock
    lateinit var stringUtils: StringUtils

    private lateinit var getVenuesUseCase: GetVenuesUseCase

    private val testLatLong = LatLong(0.0, 0.0)

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getVenuesUseCase = GetVenuesUseCaseImpl(
            venueRepository,
            stringUtils,
            coroutineRule.testDispatcher
        )
    }

    @Test
    fun `test fetch venues success`() = runTest {
        // Given
        val sampleVenueList = MockData.mockVenuesList

        // When
        val (lat, long) = testLatLong
        whenever(venueRepository.fetchVenues(lat, long)).thenReturn(sampleVenueList)
        val testResult = getVenuesUseCase.invoke(lat, long).toList()

        // Then
        assertThat(testResult.first()).isInstanceOf(ResultState.Loading::class.java)
        assertThat(testResult.last()).isInstanceOf(ResultState.Success::class.java)
        val venueList = (testResult.last() as ResultState.Success).data
        assertThat(venueList).isEqualTo(sampleVenueList)
        assertThat(venueList).hasSize(sampleVenueList.size)
        verify(venueRepository, times(1)).fetchVenues(lat, long)
    }

    @Test
    fun `test fetch venues failure`() = runTest {
        // Given
        val (lat, long) = testLatLong
        val sampleErrorResponse = "Something Went Wrong!"
        val body = "Test Error Message".toResponseBody("text/html".toMediaTypeOrNull())
        val httpException = HttpException(Response.error<ResponseBody>(500, body))

        // When
        whenever(venueRepository.fetchVenues(lat, long)).thenThrow(httpException)
        whenever(stringUtils.somethingWentWrong()).thenReturn(sampleErrorResponse)
        val testResult = getVenuesUseCase.invoke(lat, long).toList()

        // Then
        testResult.run {
            assertThat(first()).isInstanceOf(ResultState.Loading::class.java)
            assertThat(last()).isInstanceOf(ResultState.Failure::class.java)
            assertThat((last() as ResultState.Failure).errorMessage).isEqualTo(sampleErrorResponse)
        }
        verify(venueRepository, times(1)).fetchVenues(lat, long)
    }
}
