package com.faheem.currencyconverstion.data.remote

import com.faheem.currencyconverstion.data.remote.dtos.CurrenciesDto

interface CurrencyConversion {
    suspend fun getCurrencies(): CurrenciesDto
}