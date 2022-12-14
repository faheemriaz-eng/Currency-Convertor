package com.faheem.currencyconversion.data.remote

import com.faheem.currencyconversion.data.remote.dtos.ExchangeRatesDto
import retrofit2.Response
import retrofit2.http.GET

interface CurrencyConversionService {
    @GET("currencies.json")
    suspend fun fetchCurrencies(): Response<Map<String, String>>
    @GET("latest.json")
    suspend fun fetchExchangeRates(): Response<ExchangeRatesDto>
}