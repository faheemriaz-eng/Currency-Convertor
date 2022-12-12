package com.faheem.currencyconverstion.data.remote

import com.faheem.currencyconverstion.data.remote.base.BaseApi
import com.faheem.currencyconverstion.data.remote.dtos.CurrenciesDto
import com.faheem.currencyconverstion.data.remote.dtos.ExchangeRatesDto
import javax.inject.Inject

class CurrencyConversionImpl @Inject constructor(private val service: CurrencyConversionService) :
    CurrenciesRemoteDataSource,
    BaseApi() {
    override suspend fun getCurrencies(): Result<CurrenciesDto> {
        val result = executeApi { service.fetchCurrencies() }
        return if (result.isSuccess) {
            Result.success(CurrenciesDto(result.getOrNull()))
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Something went wrong"))
        }
    }

    override suspend fun getExchangeRates(): Result<ExchangeRatesDto> =
        executeApi { service.fetchExchangeRates() }

}