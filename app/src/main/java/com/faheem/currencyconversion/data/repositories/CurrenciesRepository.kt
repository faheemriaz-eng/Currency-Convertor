package com.faheem.currencyconversion.data.repositories

import com.faheem.currencyconversion.data.models.Currency
import com.faheem.currencyconversion.data.models.ExchangeRate

interface CurrenciesRepository {
    suspend fun loadCurrencies(): Result<List<Currency>>
    suspend fun loadExchangeRates(): Result<ExchangeRate?>
}