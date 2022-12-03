package com.faheem.currencyconverstion.data.remote

import com.faheem.currencyconverstion.data.remote.dtos.CurrenciesDto

class CurrencyConversionImpl(private val service: CurrencyConversionService) : CurrencyConversion {
    override suspend fun getCurrencies(): CurrenciesDto {
        val response = service.fetchCurrencies()

        return response.body()!!
    }
}