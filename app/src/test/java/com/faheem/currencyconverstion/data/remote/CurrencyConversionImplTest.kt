package com.faheem.currencyconverstion.data.remote

import com.faheem.currencyconverstion.data.remote.dtos.CurrenciesDto
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
        coEvery { mockService.fetchCurrencies() } returns
                Response.success<CurrenciesDto>(200, mockk {
                    every { currencies } returns mapOf(
                        Pair("AED", "United Arab Emirates Dirham"),
                        Pair("AFN", "Afghan Afghani"),
                        Pair("ALL", "Albanian Lek")
                    )
                })

        // When
        val actualResult = sut.getCurrencies()

        // Then
        val expectedResult = mapOf(
            Pair("AED", "United Arab Emirates Dirham"),
            Pair("AFN", "Afghan Afghani"),
            Pair("ALL", "Albanian Lek")
        )
        Assert.assertEquals(
            expectedResult.entries.first(),
            actualResult.currencies.entries.first()
        )

        // Verify
        coVerify { mockService.fetchCurrencies() }
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }
}