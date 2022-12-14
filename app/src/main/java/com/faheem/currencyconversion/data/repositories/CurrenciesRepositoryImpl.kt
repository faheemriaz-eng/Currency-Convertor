package com.faheem.currencyconversion.data.repositories

import com.faheem.currencyconversion.data.local.CurrenciesLocalDataSource
import com.faheem.currencyconversion.data.local.entities.toDomainList
import com.faheem.currencyconversion.data.local.entities.toDomainModel
import com.faheem.currencyconversion.data.models.Currency
import com.faheem.currencyconversion.data.models.ExchangeRate
import com.faheem.currencyconversion.data.remote.CurrenciesRemoteDataSource
import com.faheem.currencyconversion.data.remote.dtos.asEntity
import javax.inject.Inject

class CurrenciesRepositoryImpl @Inject constructor(
    private val currenciesRemoteDataSource: CurrenciesRemoteDataSource,
    private val currenciesLocalDataSource: CurrenciesLocalDataSource
) : CurrenciesRepository {
    override suspend fun loadCurrencies(): Result<List<Currency>> {
        val localCurrencies = currenciesLocalDataSource.getCurrencies()
        return localCurrencies?.let {
            Result.success(it.toDomainList())
        } ?: getCurrencies()
    }

    override suspend fun loadExchangeRates(): Result<ExchangeRate?> {
        val localExchangeRate = currenciesLocalDataSource.getExchangeRates()
        return localExchangeRate?.let {
            Result.success(it.toDomainModel())
        } ?: return getExchangeRate()
    }

    private suspend fun getCurrencies(): Result<List<Currency>> {
        val networkResult = currenciesRemoteDataSource.getCurrencies()
        return if (networkResult.isSuccess) {
            currenciesLocalDataSource.saveCurrencies(networkResult.getOrNull()!!.asEntity())
            Result.success(currenciesLocalDataSource.getCurrencies().toDomainList())
        } else {
            Result.failure(networkResult.exceptionOrNull()!!)
        }
    }

    private suspend fun getExchangeRate(): Result<ExchangeRate?> {
        val networkResult = currenciesRemoteDataSource.getExchangeRates()
        return if (networkResult.isSuccess) {
            currenciesLocalDataSource.saveExchangeRates(networkResult.getOrNull()!!.asEntity())
            Result.success(currenciesLocalDataSource.getExchangeRates().toDomainModel())
        } else {
            Result.failure(networkResult.exceptionOrNull()!!)
        }
    }
}