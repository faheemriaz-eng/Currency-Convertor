package com.faheem.currencyconverstion.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.faheem.currencyconverstion.data.local.entities.CurrencyEntity
import com.faheem.currencyconverstion.data.local.entities.ExchangeRateEntity

@Dao
interface CurrencyExchangeDao {
    @Query("SELECT * FROM currency")
    fun getCurrencies(): CurrencyEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencies(currencies: CurrencyEntity)

    @Query("SELECT * FROM exchange_rate")
    fun getRates(): ExchangeRateEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rates: ExchangeRateEntity)

}