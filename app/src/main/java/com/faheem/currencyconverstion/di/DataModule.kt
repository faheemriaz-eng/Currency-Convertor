package com.faheem.currencyconverstion.di

import com.faheem.currencyconverstion.data.local.CurrenciesLocalDataSource
import com.faheem.currencyconverstion.data.local.CurrenciesLocalDataSourceImpl
import com.faheem.currencyconverstion.data.remote.CurrenciesRemoteDataSource
import com.faheem.currencyconverstion.data.remote.CurrencyConversionImpl
import com.faheem.currencyconverstion.domain.repository.CurrenciesRepository
import com.faheem.currencyconverstion.domain.repository.CurrenciesRepositoryImpl
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