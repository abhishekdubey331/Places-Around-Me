package com.flow.android.location.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.flow.android.location.api.model.LatLong
import com.flow.android.location.base.MainCoroutinesRule
import com.flow.android.location.common.ResultState
import com.flow.android.location.domain.usecase.contract.FetchLocationUseCase
import com.flow.android.location.domain.usecase.contract.GetVenuesUseCase
import com.flow.android.location.utils.MockData
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.Mockito.`when` as whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class VenuesViewModelTest {

    private lateinit var venuesViewModel: VenuesViewModel

    @Mock
    lateinit var fetchLocationUseCase: FetchLocationUseCase

    @Mock
    lateinit var getVenuesUseCase: GetVenuesUseCase

    @Mock
    lateinit var savedStateHandle: SavedStateHandle

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val dispatcherRule = MainCoroutinesRule()

    private val testLatLong = LatLong(0.0, 0.0)

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `test fetch venue success`() = runTest {
        // Given
        val venuesFlow = flow {
            emit(ResultState.Success(MockData.mockVenuesList))
        }

        val latLongFlow = flow {
            emit(ResultState.Success(testLatLong))
        }

        // When
        whenever(fetchLocationUseCase.invoke()).thenReturn(latLongFlow)
        whenever(getVenuesUseCase.invoke(testLatLong.latitude, testLatLong.longitude)).thenReturn(
            venuesFlow
        )
        // Invoke
        venuesViewModel = VenuesViewModel(getVenuesUseCase, fetchLocationUseCase, savedStateHandle)

        // Then
        venuesViewModel.run {
            assertThat(venueScreenState.value.allVenueList.size).isEqualTo(MockData.mockVenuesList.size)
            assertThat(venueScreenState.value.filteredList.size).isEqualTo(MockData.mockVenuesList.size)
            assertThat(categoryScreenState.value.categories.size).isGreaterThan(0)
            assertThat(venueScreenState.value.errorMessage).isNull()
        }
    }

    @Test
    fun `test update filter list by category`() = runTest {
        // Given
        val venuesFlow = flow {
            emit(ResultState.Success(MockData.mockVenuesList))
        }

        val latLongFlow = flow {
            emit(ResultState.Success(testLatLong))
        }

        // When
        whenever(fetchLocationUseCase.invoke()).thenReturn(latLongFlow)
        whenever(getVenuesUseCase.invoke(testLatLong.latitude, testLatLong.longitude)).thenReturn(
            venuesFlow
        )
        // Invoke
        val category = MockData.mockVenuesList.first().categories.first()
        venuesViewModel = VenuesViewModel(getVenuesUseCase, fetchLocationUseCase, savedStateHandle)
        venuesViewModel.updateFilteredListByCategory(category)

        // Then
        venuesViewModel.run {
            assertThat(venueScreenState.value.allVenueList.size).isEqualTo(MockData.mockVenuesList.size)
            assertThat(venueScreenState.value.filteredList.size).isEqualTo(1)
            assertThat(categoryScreenState.value.categories.first()).isEqualTo(category)
            assertThat(categoryScreenState.value.activeCategory).isNotNull()
            assertThat(categoryScreenState.value.activeCategory).isEqualTo(category)
            assertThat(venueScreenState.value.errorMessage).isNull()
        }
    }

    @Test
    fun `test clear filter list`() = runTest {
        // Given
        val venuesFlow = flow {
            emit(ResultState.Success(MockData.mockVenuesList))
        }

        val latLongFlow = flow {
            emit(ResultState.Success(testLatLong))
        }

        // When
        whenever(fetchLocationUseCase.invoke()).thenReturn(latLongFlow)
        whenever(getVenuesUseCase.invoke(testLatLong.latitude, testLatLong.longitude)).thenReturn(
            venuesFlow
        )
        // Invoke
        venuesViewModel = VenuesViewModel(getVenuesUseCase, fetchLocationUseCase, savedStateHandle)
        venuesViewModel.updateFilteredListByCategory(MockData.mockVenuesList.first().categories.first())

        val allVenueList = venuesViewModel.venueScreenState.value.allVenueList
        val categoriesList = venuesViewModel.categoryScreenState.value.categories
        var filteredList = venuesViewModel.venueScreenState.value.filteredList

        // Then
        assertThat(allVenueList.size).isEqualTo(MockData.mockVenuesList.size)
        assertThat(filteredList.size).isEqualTo(1)
        assertThat(categoriesList.size).isGreaterThan(0)
        assertThat(venuesViewModel.venueScreenState.value.errorMessage).isNull()

        // Invoke
        venuesViewModel.clearFilters()
        filteredList = venuesViewModel.venueScreenState.value.filteredList

        // Then
        assertThat(filteredList.size).isEqualTo(allVenueList.size)
        assertThat(venuesViewModel.categoryScreenState.value.activeCategory).isNull()
    }

    @Test
    fun `test fetch location failure`() = runTest {
        // Given
        val sampleErrorResponse = "Fetch Location Failure!"
        val latLongFlow = flow {
            emit(ResultState.Failure(sampleErrorResponse))
        }

        // When
        whenever(fetchLocationUseCase.invoke()).thenReturn(latLongFlow)
        // Invoke
        venuesViewModel = VenuesViewModel(getVenuesUseCase, fetchLocationUseCase, savedStateHandle)

        // Then
        venuesViewModel.run {
            assertThat(venueScreenState.value.allVenueList.size).isEqualTo(0)
            assertThat(venueScreenState.value.filteredList.size).isEqualTo(0)
            assertThat(categoryScreenState.value.categories.size).isEqualTo(0)
            assertThat(venueScreenState.value.errorMessage).isEqualTo(sampleErrorResponse)
        }
    }

    @Test
    fun `test fetch location success but venues api failure`() = runTest {
        // Given
        val sampleErrorResponse = "Something Went Wrong!"
        val latLongFlow = flow {
            emit(ResultState.Success(testLatLong))
        }

        val venuesFlow = flow {
            emit(ResultState.Failure(sampleErrorResponse))
        }

        // When
        whenever(fetchLocationUseCase.invoke()).thenReturn(latLongFlow)
        val (lat, long) = testLatLong
        whenever(getVenuesUseCase.invoke(lat, long)).thenReturn(venuesFlow)
        // Invoke
        venuesViewModel = VenuesViewModel(getVenuesUseCase, fetchLocationUseCase, savedStateHandle)

        // Then
        venuesViewModel.run {
            assertThat(venueScreenState.value.allVenueList.size).isEqualTo(0)
            assertThat(venueScreenState.value.filteredList.size).isEqualTo(0)
            assertThat(categoryScreenState.value.categories.size).isEqualTo(0)
            assertThat(venueScreenState.value.errorMessage).isEqualTo(sampleErrorResponse)
        }
    }
}
