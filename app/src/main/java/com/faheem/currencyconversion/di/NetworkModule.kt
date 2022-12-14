package com.faheem.currencyconversion.di

import com.faheem.currencyconversion.BuildConfig
import com.faheem.currencyconversion.data.remote.CurrencyConversionService
import com.faheem.currencyconversion.data.remote.client.ApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideApiClient(): ApiClient = ApiClient()

    @Provides
    @Singleton
    fun provideRetrofit(apiClient: ApiClient): Retrofit {
        return apiClient.build(BuildConfig.SERVER_URL)
    }

    @Singleton
    @Provides
    fun providesArticlesService(retrofit: Retrofit): CurrencyConversionService =
        retrofit.create(CurrencyConversionService::class.java)
}