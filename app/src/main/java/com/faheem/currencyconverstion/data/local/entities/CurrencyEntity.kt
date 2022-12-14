package com.faheem.currencyconverstion.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.faheem.currencyconverstion.data.models.Currency

@Entity(tableName = "currency")
data class CurrencyEntity(
    val currencies: Map<String, String>,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0

)

fun CurrencyEntity?.toDomainList(): List<Currency> {
    return this?.currencies?.map {
        Currency(it.key, it.value)
    } ?: listOf()
}
