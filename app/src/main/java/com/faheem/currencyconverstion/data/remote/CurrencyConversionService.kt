package com.faheem.currencyconverstion.data.remote

import com.faheem.currencyconverstion.data.remote.dtos.CurrenciesDto
import com.faheem.currencyconverstion.data.remote.dtos.ExchangeRatesDto
import retrofit2.Response
import retrofit2.http.GET

interface CurrencyConversionService {
    @GET("currencies.json")
    suspend fun fetchCurrencies(): Response<CurrenciesDto>
    suspend fun fetchExchangeRates(): Response<ExchangeRatesDto>
}