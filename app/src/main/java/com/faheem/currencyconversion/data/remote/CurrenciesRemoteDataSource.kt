package com.faheem.currencyconversion.data.remote

import com.faheem.currencyconversion.data.remote.dtos.CurrenciesDto
import com.faheem.currencyconversion.data.remote.dtos.ExchangeRatesDto

interface CurrenciesRemoteDataSource {
    suspend fun getCurrencies(): Result<CurrenciesDto>
    suspend fun getExchangeRates(): Result<ExchangeRatesDto>
}