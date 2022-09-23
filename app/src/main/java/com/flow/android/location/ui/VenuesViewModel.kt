package com.flow.android.location.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flow.android.location.api.model.Category
import com.flow.android.location.api.model.Result
import com.flow.android.location.common.ResultState
import com.flow.android.location.domain.usecase.contract.FetchLocationUseCase
import com.flow.android.location.domain.usecase.contract.GetVenuesUseCase
import com.flow.android.location.ui.categories.state.CategoryScreenState
import com.flow.android.location.ui.venue.state.VenueScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VenuesViewModel @Inject constructor(
    private val fetchLocationUseCase: FetchLocationUseCase,
    private val getVenuesUseCase: GetVenuesUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val LATITUDE_KEY = "LATITUDE"
        private const val LONGITUDE_KEY = "LONGITUDE"
    }

    private val _venueScreenState = MutableStateFlow(VenueScreenState())
    val venueScreenState = _venueScreenState.asStateFlow()

    private val _categoryScreenState = MutableStateFlow(CategoryScreenState())
    val categoryScreenState = _categoryScreenState.asStateFlow()

    init {
        if (hasSavedState(savedStateHandle)) {
            val latitude = savedStateHandle.get<Double>(LATITUDE_KEY) ?: 0.0
            val longitude = savedStateHandle.get<Double>(LONGITUDE_KEY) ?: 0.0
            fetchNearByVenues(latitude, longitude)
        } else {
            fetchLocationTriggerVenueRequest()
        }
    }

    /**
     *   Fetch nearby venues by latitude and longitude and update ui states
     *   @param latitude Latitude of user's current location
     *   @param longitude Longitude of user's current location
     */
    fun fetchLocationTriggerVenueRequest() {
        viewModelScope.launch {
            fetchLocationUseCase().collect { result ->
                when (result) {

                    is ResultState.Loading -> _venueScreenState.update {
                        it.copy(
                            loading = true,
                            errorMessage = null
                        )
                    }

                    is ResultState.Success -> {
                        val latitude = result.data.latitude
                        val longitude = result.data.longitude
                        saveLatLongToSavedStateBundle(latitude, longitude)
                        fetchNearByVenues(latitude, longitude)
                    }

                    is ResultState.Failure -> _venueScreenState.update {
                        it.copy(
                            errorMessage = result.errorMessage,
                            loading = false
                        )
                    }
                }
            }
        }
    }


    /**
     *   Fetch nearby venues by latitude and longitude and update ui states
     *   @param latitude Latitude of user's current location
     *   @param longitude Longitude of user's current location
     */
    private fun fetchNearByVenues(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            getVenuesUseCase(latitude, longitude).collect { result ->
                when (result) {
                    is ResultState.Loading -> _venueScreenState.update {
                        it.copy(
                            loading = true,
                            errorMessage = null
                        )
                    }

                    is ResultState.Success -> {
                        _venueScreenState.update {
                            it.copy(
                                allVenueList = result.data,  // on first fetch main list and filtered list are same
                                filteredList = result.data,
                                loading = false
                            )
                        }
                        _categoryScreenState.update {
                            it.copy(categories = getCategoryList(result.data))
                        }
                    }

                    is ResultState.Failure -> _venueScreenState.update {
                        it.copy(
                            errorMessage = result.errorMessage,
                            loading = false
                        )
                    }
                }
            }
        }
    }

    /**
     *   Update venueScreenState with filtered list and set active category
     *   @param result complete venue list
     *   @return List<Category> Distinct list of categories
     */
    private fun getCategoryList(result: List<Result>) = result
        .asSequence()
        .map { it.categories }
        .flatten()
        .distinct()
        .toList()


    /**
     *   Update venueScreenState with filtered list and set active category
     *   @param category to filter
     */
    fun updateFilteredListByCategory(category: Category) {
        _venueScreenState.update {
            it.copy(filteredList = getFilteredListByCategory(category.id))
        }
        _categoryScreenState.update { it.copy(activeCategory = category) }
    }

    /**
     *   Return a filtered list based on category id
     *   @param categoryId to filter
     *  @return List<Result> Filtered list by category
     */
    private fun getFilteredListByCategory(categoryId: String) = venueScreenState.value
        .allVenueList.filter { venue ->
            venue.categories.any {
                it.id == categoryId
            }
        }

    /**
     *  Remove active category and update filtered list with all contents of venue list
     */
    fun clearFilters() {
        _venueScreenState.update { it.copy(filteredList = it.allVenueList) }
        _categoryScreenState.update { it.copy(activeCategory = null) }
    }

    /**
     *   Set latitude and longitude to savedStateHandle to reuse save coordinates
     *   in case of process death
     *   @param latitude Latitude of user's current location
     *   @param longitude Longitude of user's current location
     *   @return List<Category> Distinct list of categories
     */
    private fun saveLatLongToSavedStateBundle(latitude: Double, longitude: Double) {
        savedStateHandle[LATITUDE_KEY] = latitude
        savedStateHandle[LONGITUDE_KEY] = longitude
    }

    /**
     *   Check if coordinates are present in case of restart after process death
     *   in case of process death
     *   @param savedStateHandle Latitude of user's current location
     *   @return Boolean if state was saved for Lat and Lng
     */
    private fun hasSavedState(savedStateHandle: SavedStateHandle): Boolean {
        return savedStateHandle.contains(LATITUDE_KEY)
                && savedStateHandle.contains(LONGITUDE_KEY)
    }
}