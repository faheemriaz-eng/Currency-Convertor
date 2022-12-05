package com.faheem.currencyconverstion.data.remote

import com.faheem.currencyconverstion.data.remote.dtos.CurrenciesDto
import com.faheem.currencyconverstion.data.remote.dtos.ExchangeRatesDto
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
internal class CurrencyConversionImplTest {

    private lateinit var mockService: CurrencyConversionService

    private lateinit var sut: CurrencyConversion

    @Before
    fun init() {
        mockService = mockk()
        sut = CurrencyConversionImpl(mockService)
    }

    @Test
    fun `getCurrencies on success return currencies`() = runTest {
        // Given
        val mockData = mockk<CurrenciesDto> {
            every { currencies } returns mapOf(
                Pair("AED", "United Arab Emirates Dirham"),
                Pair("AFN", "Afghan Afghani"),
                Pair("ALL", "Albanian Lek")
            )
        }

        coEvery { mockService.fetchCurrencies() } returns
                Response.success(200, mockData)

        // When
        val actualResult = sut.getCurrencies()

        // Then
        val expectedResult = Result.success(mockData)
        Assert.assertEquals(expectedResult, actualResult)

        // Verify
        coVerify { mockService.fetchCurrencies() }
    }

    @Test
    fun `getCurrencies on failure return exception`() = runTest {
        // Given
        val errorBody =
            "{\"description\": \"Invalid App ID provided\"}".toResponseBody("application/json".toMediaTypeOrNull())
        val mockErrorResponse = Response.error<CurrenciesDto>(401, errorBody)

        coEvery { mockService.fetchCurrencies() } returns mockErrorResponse

        // When
        val actualResult = sut.getCurrencies()

        // Then
        Assert.assertEquals(actualResult.exceptionOrNull()?.message, "Invalid App ID provided")

        // Verify
        coVerify { mockService.fetchCurrencies() }
    }

    @Test
    fun `getExchangeRates on success return rates`() = runTest {
        // Given
        val mockData = mockk<ExchangeRatesDto> {
            every { base } returns "USD"
            every { rates } returns mapOf(Pair("AED", 3.673), Pair("AFN", 88.500008))
        }

        coEvery { mockService.fetchExchangeRates() } returns
                Response.success(200, mockData)

        // When
        val actualResult = sut.getExchangeRates()

        // Then
        val expectedResult = Result.success(mockData)
        Assert.assertEquals(expectedResult, actualResult)

        // Verify
        coVerify { mockService.fetchExchangeRates() }
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }
}