package com.faheem.currencyconverstion.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.faheem.currencyconverstion.domain.models.Currency
import com.faheem.currencyconverstion.domain.models.ExchangeRate
import com.faheem.currencyconverstion.domain.models.Rate
import com.faheem.currencyconverstion.domain.repository.CurrenciesRepository
import com.faheem.currencyconverstion.testutils.getOrAwaitValue
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyExchangeVMTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")


    private lateinit var mockRepository: CurrenciesRepository

    private lateinit var sut: CurrencyExchangeVM

    @Before
    fun init() {
        mockRepository = mockk()
        sut = CurrencyExchangeVM(mockRepository)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @Test
    fun `test fetchCurrenciesAndRates should return currencies and rates`() = runTest {
        // Given
        val currencies = listOf(Currency("AED", "United Arab Emirates Dirham"))
        coEvery { mockRepository.loadCurrencies() } returns Result.success(currencies)
        coEvery { mockRepository.loadExchangeRates() } returns Result.success(
            ExchangeRate(baseCurrencyCode = "USD", rates = generateExchangeRates())
        )
        //When
        sut.fetchCurrenciesAndRates()
        val expectedResult = generateExchangeRates()

        //Then
        Assert.assertEquals(currencies, sut.currencies.getOrAwaitValue())
        Assert.assertEquals(expectedResult, sut.rates.getOrAwaitValue())

        // Verify
        coVerify {
            mockRepository.loadCurrencies()
            mockRepository.loadExchangeRates()
        }
    }

    @Test
    fun `test convert exchangeRate if selected currency is USD`() = runTest {
        // Given
        val baseCurrency = "USD"
        val sourceCurrency = "USD"
        val enteredAmount = 15.00
        coEvery { mockRepository.loadCurrencies() } returns Result.success(mockk())
        coEvery { mockRepository.loadExchangeRates() } returns Result.success(
            ExchangeRate(baseCurrencyCode = baseCurrency, rates = generateExchangeRates())
        )

        // When
        sut.fetchCurrenciesAndRates()
        sut.rates.getOrAwaitValue()
        sut.convertExchangeRate(sourceCurrency, enteredAmount)

        val expectedRates = listOf(Rate("AED", 55.095), Rate("PKR", 3372.9))

        // Then
        Assert.assertEquals(expectedRates, sut.rates.getOrAwaitValue())
    }

    @Test
    fun `test convert exchangeRate if selected currency is PKR`() = runTest {
        // Given
        val baseCurrency = "USD"
        val sourceCurrency = "PKR"
        val enteredAmount = 15.00
        coEvery { mockRepository.loadCurrencies() } returns Result.success(mockk())
        coEvery { mockRepository.loadExchangeRates() } returns Result.success(
            ExchangeRate(baseCurrencyCode = baseCurrency, rates = generateExchangeRates())
        )

        // When
        sut.fetchCurrenciesAndRates()
        sut.rates.getOrAwaitValue()
        sut.convertExchangeRate(sourceCurrency, enteredAmount)

        val expectedRates = listOf(Rate("AED", 0.245), Rate("PKR", 15.0))

        // Then
        Assert.assertEquals(expectedRates, sut.rates.getOrAwaitValue())
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
        clearAllMocks()
    }

    companion object {
        fun generateExchangeRates() = listOf(Rate("AED", 3.673), Rate("PKR", 224.86))
    }

}