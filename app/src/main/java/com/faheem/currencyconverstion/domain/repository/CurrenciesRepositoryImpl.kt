package com.faheem.currencyconverstion.domain.repository

import com.faheem.currencyconverstion.data.local.CurrenciesLocalDataSource
import com.faheem.currencyconverstion.data.local.entities.toDomainList
import com.faheem.currencyconverstion.data.local.entities.toDomainModel
import com.faheem.currencyconverstion.data.remote.CurrenciesRemoteDataSource
import com.faheem.currencyconverstion.data.remote.dtos.asEntity
import com.faheem.currencyconverstion.domain.models.Currency
import com.faheem.currencyconverstion.domain.models.ExchangeRate
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