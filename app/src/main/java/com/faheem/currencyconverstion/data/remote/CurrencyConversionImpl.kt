package com.faheem.currencyconverstion.data.remote

import com.faheem.currencyconverstion.data.remote.dtos.CurrenciesDto

class CurrencyConversionImpl(private val service: CurrencyConversionService) : CurrencyConversion {
    override suspend fun getCurrencies(): Result<CurrenciesDto> {
        val response = service.fetchCurrencies()
        if (response.isSuccessful)
            return Result.success(response.body()!!)

        return Result.failure(Throwable(message = response.errorBody()?.string()))
    }
}