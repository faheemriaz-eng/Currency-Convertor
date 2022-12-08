package com.faheem.currencyconverstion.data.remote

import com.faheem.currencyconverstion.data.remote.dtos.CurrenciesDto
import com.faheem.currencyconverstion.data.remote.dtos.ExchangeRatesDto

interface CurrenciesRemoteDataSource {
    suspend fun getCurrencies(): Result<CurrenciesDto>
    suspend fun getExchangeRates(): Result<ExchangeRatesDto>
}