package com.faheem.currencyconverstion.domain.repository

import com.faheem.currencyconverstion.data.local.CurrenciesLocalDataSource
import com.faheem.currencyconverstion.data.local.entities.CurrencyEntity
import com.faheem.currencyconverstion.data.local.entities.ExchangeRateEntity
import com.faheem.currencyconverstion.data.remote.CurrenciesRemoteDataSource
import com.faheem.currencyconverstion.data.remote.dtos.CurrenciesDto
import com.faheem.currencyconverstion.data.remote.dtos.ExchangeRatesDto
import com.faheem.currencyconverstion.data.remote.dtos.asEntity
import com.faheem.currencyconverstion.domain.models.Currency
import com.faheem.currencyconverstion.domain.models.ExchangeRate
import com.faheem.currencyconverstion.domain.models.Rate
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CurrenciesRepositoryTest {
    private lateinit var mockLocalDataSource: CurrenciesLocalDataSource
    private lateinit var mockRemoteDataSource: CurrenciesRemoteDataSource

    private lateinit var sut: CurrenciesRepository

    @Before
    fun init() {
        mockLocalDataSource = mockk()
        mockRemoteDataSource = mockk()
        sut = CurrenciesRepository(mockRemoteDataSource, mockLocalDataSource)
    }


    @Test
    fun `test load currencies from local source on second call`() = runTest {
        // Given
        val mockData = mapOf(Pair("AED", "United Arab Emirates Dirham"))
        val mockResponse = mockk<CurrenciesDto> {
            every { currencies } returns mockData
        }

        // When
        coEvery { mockLocalDataSource.saveCurrencies(CurrencyEntity(mockData)) } returns Unit
        coEvery { mockRemoteDataSource.getCurrencies() } returns Result.success(mockResponse)
        every { mockLocalDataSource.getCurrencies() } returns null andThen mockResponse.asEntity()


        // initiate first time call
        val actualResultOnFirstCall = sut.loadCurrencies()
        //initiate second time call
        val actualResultOnSecondCall = sut.loadCurrencies()

        // Then
        val expectedResult = Result.success(listOf(Currency("AED", "United Arab Emirates Dirham")))
        Assert.assertEquals(
            expectedResult,
            actualResultOnFirstCall
        )

        Assert.assertEquals(
            expectedResult,
            actualResultOnSecondCall
        )

        coVerify(exactly = 3) { mockLocalDataSource.getCurrencies() }
        coVerify {
            mockLocalDataSource.saveCurrencies(CurrencyEntity(mockData))
            mockRemoteDataSource.getCurrencies()
        }
    }

    @Test
    fun `test load exchange rates from local source on second call`() = runTest {
        // Given
        val baseCurrency = "USD"
        val mockData = mapOf(Pair("AED", 3.4132))
        val mockResponse = mockk<ExchangeRatesDto> {
            every { base } returns baseCurrency
            every { rates } returns mockData
        }

        // When
        coEvery {
            mockLocalDataSource.saveExchangeRates(
                ExchangeRateEntity(baseCurrency, mockData)
            )
        } returns Unit
        coEvery { mockRemoteDataSource.getExchangeRates() } returns Result.success(mockResponse)
        every { mockLocalDataSource.getExchangeRates() } returns null andThen mockResponse.asEntity()


        // initiate first time call
        val actualResultOnFirstCall = sut.loadExchangeRates()
        //initiate second time call
        val actualResultOnSecondCall = sut.loadExchangeRates()

        // Then
        val expectedResult =
            Result.success(
                ExchangeRate(
                    baseCurrencyCode = baseCurrency,
                    rates = listOf(Rate("AED", 3.4132))
                )
            )

        Assert.assertEquals(
            expectedResult,
            actualResultOnFirstCall
        )

        Assert.assertEquals(
            expectedResult,
            actualResultOnSecondCall
        )

        coVerify(exactly = 3) { mockLocalDataSource.getExchangeRates() }
        coVerify {
            mockLocalDataSource.saveExchangeRates(ExchangeRateEntity(baseCurrency, mockData))
            mockRemoteDataSource.getExchangeRates()
        }
    }

    @Before
    fun tearDown() {
        clearAllMocks()
    }

}