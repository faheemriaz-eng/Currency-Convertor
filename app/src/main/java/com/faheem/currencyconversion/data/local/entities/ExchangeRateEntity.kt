package com.faheem.currencyconversion.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.faheem.currencyconversion.data.models.ExchangeRate
import com.faheem.currencyconversion.data.models.Rate

@Entity(tableName = "exchange_rate")
data class ExchangeRateEntity(
    val baseCurrencyCode: String,
    val rates: Map<String, Double>,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)

fun ExchangeRateEntity?.toDomainModel(): ExchangeRate {
    val rates = this?.rates?.map {
        Rate(
            currencyCode = it.key,
            rate = it.value
        )
    } ?: listOf()
    return ExchangeRate(baseCurrencyCode = this?.baseCurrencyCode ?: "", rates = rates)
}