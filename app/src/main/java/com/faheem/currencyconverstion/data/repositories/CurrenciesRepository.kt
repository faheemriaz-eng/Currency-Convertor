package com.faheem.currencyconverstion.data.repositories

import com.faheem.currencyconverstion.data.models.Currency
import com.faheem.currencyconverstion.data.models.ExchangeRate

interface CurrenciesRepository {
    suspend fun loadCurrencies(): Result<List<Currency>>
    suspend fun loadExchangeRates(): Result<ExchangeRate?>
}