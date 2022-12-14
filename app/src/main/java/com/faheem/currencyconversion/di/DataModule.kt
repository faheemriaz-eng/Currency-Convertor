package com.faheem.currencyconversion.di

import com.faheem.currencyconversion.data.local.CurrenciesLocalDataSource
import com.faheem.currencyconversion.data.local.CurrenciesLocalDataSourceImpl
import com.faheem.currencyconversion.data.remote.CurrenciesRemoteDataSource
import com.faheem.currencyconversion.data.remote.CurrencyConversionImpl
import com.faheem.currencyconversion.data.repositories.CurrenciesRepository
import com.faheem.currencyconversion.data.repositories.CurrenciesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun provideCurrenciesRemoteDataSource(repository: CurrencyConversionImpl): CurrenciesRemoteDataSource

    @Binds
    @Singleton
    abstract fun provideCurrenciesLocalDataSource(localSource: CurrenciesLocalDataSourceImpl): CurrenciesLocalDataSource

    @Binds
    @Singleton
    abstract fun provideCurrenciesRepository(repository: CurrenciesRepositoryImpl): CurrenciesRepository


}