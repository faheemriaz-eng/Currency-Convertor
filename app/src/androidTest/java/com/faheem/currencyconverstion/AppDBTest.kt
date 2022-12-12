package com.faheem.currencyconverstion

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.faheem.currencyconverstion.data.local.daos.CurrencyExchangeDao
import com.faheem.currencyconverstion.data.local.db.AppDB
import com.faheem.currencyconverstion.data.local.entities.CurrencyEntity
import com.faheem.currencyconverstion.data.local.entities.ExchangeRateEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDBTest {
    private lateinit var db: AppDB
    private lateinit var dao: CurrencyExchangeDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDB::class.java).build()
        dao = db.currencyExchangeDao()
    }

    @Test
    fun writeAndReadCurrency() {
        val mockCurrencies = CurrencyEntity(
            id = 1,
            currencies = mapOf(
                Pair("AED", "United Arab Emirates Dirham"),
                Pair("AFN", "Afghan Afghani"),
                Pair("ALL", "Albanian Lek")
            )
        )

        runBlocking {
            dao.insertCurrencies(mockCurrencies)
        }
        val currencies = dao.getCurrencies()

        Assert.assertEquals(mockCurrencies, currencies)
    }

    @Test
    fun writeAndReadExchangeRates() {
        val mockRates = ExchangeRateEntity(
            rates = mapOf(Pair("AED", 3.673), Pair("AFN", 88.500008)),
            baseCurrencyCode = "USD"
        )


        runBlocking {
            dao.insertRates(mockRates)
        }
        val currencies = dao.getRates()

        Assert.assertEquals(currencies, mockRates)
    }

    @After
    fun tearDown() {
        db.close()
    }
}