package com.faheem.currencyconversion.data.local

import com.faheem.currencyconversion.data.local.daos.CurrencyExchangeDao
import com.faheem.currencyconversion.data.local.entities.CurrencyEntity
import com.faheem.currencyconversion.data.local.entities.ExchangeRateEntity
import javax.inject.Inject

class CurrenciesLocalDataSourceImpl @Inject constructor(private val dao: CurrencyExchangeDao) :
    CurrenciesLocalDataSource {
    override suspend fun saveCurrencies(currencyEntity: CurrencyEntity) {
        dao.insertCurrencies(currencyEntity)
    }

    override fun getCurrencies(): CurrencyEntity? {
        return dao.getCurrencies()
    }

    override suspend fun saveExchangeRates(rate: ExchangeRateEntity) {
        dao.insertRates(rate)
    }

    override fun getExchangeRates(): ExchangeRateEntity? {
        return dao.getRates()
    }
}