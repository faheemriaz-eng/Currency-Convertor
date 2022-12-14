package com.faheem.currencyconversion.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.faheem.currencyconversion.data.models.Currency
import com.faheem.currencyconversion.data.models.ExchangeRate
import com.faheem.currencyconversion.data.models.Rate
import com.faheem.currencyconversion.data.repositories.CurrenciesRepository
import com.faheem.currencyconversion.data.repositories.CurrenciesRepositoryImpl
import com.faheem.currencyconversion.testutils.getOrAwaitValue
import com.faheem.currencyconversion.ui.screens.currencies.CurrencyExchangeVM
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
import org.junit.Assert.assertNotEquals
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
        mockRepository = mockk<CurrenciesRepositoryImpl>()
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

        // Trigger an update, which starts a coroutine that updates the value
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
    fun `test fetchCurrenciesAndRates if currencies failed should return empty list of currencies`() =
        runTest {
            // Given
            coEvery { mockRepository.loadCurrencies() } returns Result.failure(Exception("Something went wrong"))
            coEvery { mockRepository.loadExchangeRates() } returns Result.success(
                ExchangeRate(baseCurrencyCode = "USD", rates = generateExchangeRates())
            )
            //When

            // Trigger an update, which starts a coroutine that updates the value
            sut.fetchCurrenciesAndRates()

            //Then
            Assert.assertEquals(true, sut.currencies.getOrAwaitValue().isEmpty())
            Assert.assertEquals(true, sut.rates.getOrAwaitValue().isNotEmpty())

            // Verify
            coVerify {
                mockRepository.loadCurrencies()
                mockRepository.loadExchangeRates()
            }
        }

    @Test
    fun `test fetchCurrenciesAndRates if rates failed should return empty list of rates`() =
        runTest {
            // Given

            coEvery { mockRepository.loadCurrencies() } returns Result.success(
                listOf(
                    Currency(
                        "PKR",
                        "Pakistani"
                    )
                )
            )
            coEvery { mockRepository.loadExchangeRates() } returns Result.failure(Exception("Something went wrong"))

            //When

            // Trigger an update, which starts a coroutine that updates the value
            sut.fetchCurrenciesAndRates()

            //Then
            Assert.assertEquals(true, sut.rates.getOrAwaitValue().isEmpty())
            Assert.assertEquals(true, sut.currencies.getOrAwaitValue().isNotEmpty())

            // Verify
            coVerify {
                mockRepository.loadCurrencies()
                mockRepository.loadExchangeRates()
            }
        }

    @Test
    fun `test fetchCurrenciesAndRates failed should return empty lists of rates and currencies`() =
        runTest {
            // Given
            coEvery { mockRepository.loadCurrencies() } returns Result.failure(Exception("Something went wrong"))
            coEvery { mockRepository.loadExchangeRates() } returns Result.failure(Exception("Something went wrong"))
            //When

            // Trigger an update, which starts a coroutine that updates the value
            sut.fetchCurrenciesAndRates()

            //Then
            Assert.assertEquals(true, sut.currencies.getOrAwaitValue().isEmpty())
            Assert.assertEquals(true, sut.rates.getOrAwaitValue().isEmpty())

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

        // Trigger an update, which starts a coroutine that updates the value
        sut.fetchCurrenciesAndRates()
        // Get the initial value that comes directly from Mock Data Source
        val initialValue = sut.rates.getOrAwaitValue()

        sut.convertExchangeRate(sourceCurrency, enteredAmount)

        // Then

        // Get the new value
        val valueAfterConversion = sut.rates.getOrAwaitValue()

        val expectedValue = listOf(Rate("AED", 55.095), Rate("PKR", 3372.9))

        assertNotEquals(initialValue, valueAfterConversion)
        Assert.assertEquals(expectedValue, valueAfterConversion)
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

        // Trigger an update, which starts a coroutine that updates the value
        sut.fetchCurrenciesAndRates()

        // Get the initial value that comes directly from Mock Data Source
        val initialValue = sut.rates.getOrAwaitValue()

        sut.convertExchangeRate(sourceCurrency, enteredAmount)

        // Then

        val expectedValueAfterConversion = listOf(Rate("AED", 0.245), Rate("PKR", 15.0))
        // Get the new value
        val valueAfterConversion = sut.rates.getOrAwaitValue()

        assertNotEquals(initialValue, valueAfterConversion)
        Assert.assertEquals(expectedValueAfterConversion, valueAfterConversion)

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