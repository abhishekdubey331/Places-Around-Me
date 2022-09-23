package com.flow.android.location.di

import com.flow.android.location.BuildConfig
import com.flow.android.location.api.VenuesService
import com.flow.android.location.common.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofitApi(okHttpClient: OkHttpClient): VenuesService = Retrofit.Builder()
        .baseUrl(BuildConfig.FOURSQUARE_BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(okHttpClient)
        .build()
        .create(VenuesService::class.java)

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    // TODO Add your FourSquare API key here
                    .addHeader(Constants.AUTHORIZATION_KEY, "fsq3wBr9LvgIens0vBGxgKzRqDqazjsKr7uAWQgOqEGOTJI=")
                    .build()
                chain.proceed(newRequest)
            }
            .build()
    }
}
