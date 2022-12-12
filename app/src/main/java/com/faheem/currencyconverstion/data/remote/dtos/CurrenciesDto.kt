package com.faheem.currencyconverstion.data.remote.dtos

import com.faheem.currencyconverstion.data.local.entities.CurrencyEntity

data class CurrenciesDto(

    val currencies: Map<String, String>? = null
)

/**
 * Converts the network model to the local model for persisting
 * by the local data source
 */
fun CurrenciesDto.asEntity() = CurrencyEntity(currencies = this.currencies?: mapOf())
