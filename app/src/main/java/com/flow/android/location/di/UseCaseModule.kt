package com.flow.android.location.di

import android.content.Context
import com.flow.android.location.domain.repository.contract.VenueRepository
import com.flow.android.location.domain.usecase.contract.FetchLocationUseCase
import com.flow.android.location.domain.usecase.contract.GetVenuesUseCase
import com.flow.android.location.domain.usecase.impl.FetchLocationUseCaseImpl
import com.flow.android.location.domain.usecase.impl.GetVenuesUseCaseImpl
import com.flow.android.location.utils.StringUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher

@InstallIn(ViewModelComponent::class)
@Module
object UseCaseModule {

    @Provides
    fun getVenuesUseCase(
        venueRepository: VenueRepository,
        stringUtils: StringUtils,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher
    ): GetVenuesUseCase = GetVenuesUseCaseImpl(
        venueRepository, stringUtils, coroutineDispatcher
    )

    @Provides
    fun getFetchLocationUseCase(
        @ApplicationContext context: Context,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher
    ): FetchLocationUseCase = FetchLocationUseCaseImpl(
        context,
        coroutineDispatcher
    )
}
