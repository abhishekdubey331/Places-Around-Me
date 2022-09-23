package com.flow.android.location.domain.usecase.contract

import com.flow.android.location.api.model.LatLong
import com.flow.android.location.common.ResultState
import kotlinx.coroutines.flow.Flow

interface FetchLocationUseCase {
    operator fun invoke(): Flow<ResultState<LatLong>>
}