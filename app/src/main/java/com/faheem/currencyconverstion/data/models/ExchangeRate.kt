package com.faheem.currencyconverstion.data.models

data class ExchangeRate(
    val baseCurrencyCode: String,
    val rates: List<Rate>
)

data class Rate(val currencyCode: String, val rate: Double)
