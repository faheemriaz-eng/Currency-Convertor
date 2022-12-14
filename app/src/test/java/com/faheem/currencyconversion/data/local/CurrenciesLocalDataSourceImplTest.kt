package com.faheem.currencyconversion.data.local

import com.faheem.currencyconversion.data.local.daos.CurrencyExchangeDao
import com.faheem.currencyconversion.data.local.entities.CurrencyEntity
import com.faheem.currencyconversion.data.local.entities.ExchangeRateEntity
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CurrenciesLocalDataSourceImplTest {

    private lateinit var mockDao: CurrencyExchangeDao

    private lateinit var sut: CurrenciesLocalDataSource

    @Before
    fun init() {
        mockDao = mockk()
        sut = CurrenciesLocalDataSourceImpl(mockDao)
    }

    @Test
    fun `getCurrencies from local source should return currency entity`() = runTest {
        // Given
        val mockData = mapOf(
            Pair("AED", "United Arab Emirates Dirham"),
            Pair("AFN", "Afghan Afghani"),
            Pair("ALL", "Albanian Lek")
        )
        val mockEntity = mockk<CurrencyEntity> {
            every { currencies } returns mockData
        }

        coEvery { mockDao.getCurrencies() } returns mockEntity

        // When
        val actualResult = sut.getCurrencies()

        // Then
        Assert.assertEquals(mockEntity, actualResult)

        // Verify
        coVerify { mockDao.getCurrencies() }
    }

    @Test
    fun `getCurrencies from local source should return null if local source is empty`() = runTest {
        // Given
        coEvery { mockDao.getCurrencies() } returns null

        // When
        val actualResult = sut.getCurrencies()

        // Then
        Assert.assertEquals(null, actualResult)

        // Verify
        coVerify { mockDao.getCurrencies() }
    }

    @Test
    fun `getExchangeRates from local source should return exchange rate entity`() = runTest {
        // Given
        val mockData = mapOf(
            Pair("AED", 3.456),
            Pair("AFN", 1.231),
            Pair("ALL", 5.3234)
        )
        val mockEntity = mockk<ExchangeRateEntity> {
            every { rates } returns mockData
        }

        coEvery { mockDao.getRates() } returns mockEntity

        // When
        val actualResult = sut.getExchangeRates()

        // Then
        Assert.assertEquals(mockEntity, actualResult)

        // Verify
        coVerify { mockDao.getRates() }
    }

    @Test
    fun `getExchangeRates from local source should return null if local source is empty `() =
        runTest {
            // Given

            coEvery { mockDao.getRates() } returns null

            // When
            val actualResult = sut.getExchangeRates()

            // Then
            Assert.assertEquals(null, actualResult)

            // Verify
            coVerify { mockDao.getRates() }
        }

    @After
    fun tearDown() {
        clearAllMocks()
    }

}