package com.flow.android.location.ui.venue.state

import com.flow.android.location.api.model.Result

data class VenueScreenState(
    val loading: Boolean = false,
    val allVenueList: List<Result> = emptyList(),
    val filteredList: List<Result> = emptyList(),
    val errorMessage: String? = null
)
