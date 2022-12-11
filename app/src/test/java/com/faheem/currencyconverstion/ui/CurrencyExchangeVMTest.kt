package com.faheem.currencyconverstion.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.faheem.currencyconverstion.domain.models.Currency
import com.faheem.currencyconverstion.domain.models.ExchangeRate
import com.faheem.currencyconverstion.domain.models.Rate
import com.faheem.currencyconverstion.domain.repository.CurrenciesRepository
import com.faheem.currencyconverstion.testutils.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
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
        val rates = listOf(Rate("AED", 3.673))
        coEvery { mockRepository.loadCurrencies() } returns Result.success(currencies)
        coEvery { mockRepository.loadExchangeRates() } returns Result.success(
            ExchangeRate(baseCurrencyCode = "USD", rates = rates)
        )
        //When
        sut.fetchCurrenciesAndRates()

        //Then
        Assert.assertEquals(currencies, sut.currencies.getOrAwaitValue())
        Assert.assertEquals(rates, sut.rates.getOrAwaitValue())

        // Verify
        coVerify {
            mockRepository.loadCurrencies()
            mockRepository.loadExchangeRates()
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

}