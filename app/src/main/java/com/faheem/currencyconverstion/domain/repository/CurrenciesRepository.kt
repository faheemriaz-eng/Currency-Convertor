package com.faheem.currencyconverstion.domain.repository

import com.faheem.currencyconverstion.domain.models.Currency
import com.faheem.currencyconverstion.domain.models.ExchangeRate

interface CurrenciesRepository {
    suspend fun loadCurrencies(): Result<List<Currency>>
    suspend fun loadExchangeRates(): Result<ExchangeRate?>
}