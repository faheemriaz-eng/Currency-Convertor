package com.faheem.currencyconversion.data.local

import com.faheem.currencyconversion.data.local.entities.CurrencyEntity
import com.faheem.currencyconversion.data.local.entities.ExchangeRateEntity

interface CurrenciesLocalDataSource {
    suspend fun saveCurrencies(currencyEntity: CurrencyEntity)
    fun getCurrencies(): CurrencyEntity?
    suspend fun saveExchangeRates(rate: ExchangeRateEntity)
    fun getExchangeRates(): ExchangeRateEntity?
}