package com.faheem.currencyconverstion.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency")
data class CurrencyEntity(
    val currencies: Map<String, String>,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0

)