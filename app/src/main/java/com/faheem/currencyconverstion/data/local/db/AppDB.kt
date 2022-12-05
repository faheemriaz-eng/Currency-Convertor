package com.faheem.currencyconverstion.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.faheem.currencyconverstion.data.local.daos.CurrencyExchangeDao
import com.faheem.currencyconverstion.data.local.entities.CurrencyEntity
import com.faheem.currencyconverstion.data.local.entities.ExchangeRateEntity
import com.faheem.currencyconverstion.data.local.utils.Converter

@Database(
    entities = [CurrencyEntity::class, ExchangeRateEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class AppDB : RoomDatabase() {
    abstract fun currencyExchangeDao(): CurrencyExchangeDao
}