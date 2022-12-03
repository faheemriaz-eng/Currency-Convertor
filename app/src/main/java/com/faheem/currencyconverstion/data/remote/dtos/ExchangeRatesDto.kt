package com.faheem.currencyconverstion.data.remote.dtos

data class ExchangeRatesDto(
    val base: String,
    val rates: Map<String, Double>
)
