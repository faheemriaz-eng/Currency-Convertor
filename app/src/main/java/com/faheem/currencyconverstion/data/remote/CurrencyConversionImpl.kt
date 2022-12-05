package com.faheem.currencyconverstion.data.remote

import com.faheem.currencyconverstion.data.remote.dtos.CurrenciesDto
import com.faheem.currencyconverstion.data.remote.dtos.ExchangeRatesDto

class CurrencyConversionImpl(private val service: CurrencyConversionService) : CurrencyConversion {
    override suspend fun getCurrencies(): Result<CurrenciesDto> {
        val response = service.fetchCurrencies()
        if (response.isSuccessful)
            return Result.success(response.body()!!)

        return Result.failure(Throwable(message = response.errorBody()?.string()))
    }

    override suspend fun getExchangeRates(): Result<ExchangeRatesDto> {
        val response = service.fetchExchangeRates()
        if (response.isSuccessful)
            return Result.success(response.body()!!)

        return Result.failure(Throwable(message = response.errorBody()?.string()))
    }
}