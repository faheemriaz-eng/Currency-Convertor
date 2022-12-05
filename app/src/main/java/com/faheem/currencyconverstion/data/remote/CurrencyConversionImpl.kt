package com.faheem.currencyconverstion.data.remote

import com.faheem.currencyconverstion.data.remote.base.BaseApi
import com.faheem.currencyconverstion.data.remote.dtos.CurrenciesDto
import com.faheem.currencyconverstion.data.remote.dtos.ExchangeRatesDto

class CurrencyConversionImpl(private val service: CurrencyConversionService) : CurrencyConversion,
    BaseApi() {
    override suspend fun getCurrencies(): Result<CurrenciesDto> =
        executeApi { service.fetchCurrencies() }

    override suspend fun getExchangeRates(): Result<ExchangeRatesDto> =
        executeApi { service.fetchExchangeRates() }

}