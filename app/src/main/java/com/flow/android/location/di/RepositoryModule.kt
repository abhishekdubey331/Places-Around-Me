package com.flow.android.location.di

import com.flow.android.location.api.VenuesService
import com.flow.android.location.domain.repository.contract.VenueRepository
import com.flow.android.location.domain.repository.impl.VenuesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideVenueRepository(
        VenuesService: VenuesService
    ): VenueRepository = VenuesRepositoryImpl(VenuesService)
}
