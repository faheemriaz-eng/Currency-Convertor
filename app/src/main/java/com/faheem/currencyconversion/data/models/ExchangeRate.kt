package com.faheem.currencyconversion.data.models

data class ExchangeRate(
    val baseCurrencyCode: String,
    val rates: List<Rate>
)

data class Rate(val currencyCode: String, val rate: Double)
