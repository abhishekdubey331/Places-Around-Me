package com.flow.android.location.domain.usecase.contract

import com.flow.android.location.api.model.Result
import com.flow.android.location.common.ResultState
import kotlinx.coroutines.flow.Flow

interface GetVenuesUseCase {

    operator fun invoke(latitude: Double, longitude: Double): Flow<ResultState<List<Result>>>

}
