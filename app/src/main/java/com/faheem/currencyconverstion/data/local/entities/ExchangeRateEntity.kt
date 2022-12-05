package com.faheem.currencyconverstion.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_rate")
data class ExchangeRateEntity(
    val baseCurrencyCode: String,
    val rates: Map<String, Double>,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)