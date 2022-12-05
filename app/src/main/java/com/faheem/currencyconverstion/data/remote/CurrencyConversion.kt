package com.faheem.currencyconverstion.data.remote

import com.faheem.currencyconverstion.data.remote.dtos.CurrenciesDto
import com.faheem.currencyconverstion.data.remote.dtos.ExchangeRatesDto

interface CurrencyConversion {
    suspend fun getCurrencies(): Result<CurrenciesDto>
    suspend fun getExchangeRates(): Result<ExchangeRatesDto>
}