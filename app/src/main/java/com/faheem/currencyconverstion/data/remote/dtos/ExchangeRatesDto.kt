package com.faheem.currencyconverstion.data.remote.dtos

import com.faheem.currencyconverstion.data.local.entities.ExchangeRateEntity

data class ExchangeRatesDto(
    val base: String,
    val rates: Map<String, Double>
)

/**
 * Converts the network model to the local model for persisting
 * by the local data source
 */
fun ExchangeRatesDto.asEntity() =
    ExchangeRateEntity(this.base, this.rates)
