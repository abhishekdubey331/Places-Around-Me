package com.flow.android.location.di

import android.content.Context
import com.flow.android.location.utils.StringUtils
import com.flow.android.location.utils.StringUtilsImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideStringUtils(@ApplicationContext context: Context): StringUtils =
        StringUtilsImpl(context)
}
