package com.faheem.currencyconverstion.data.remote.dtos

import com.faheem.currencyconverstion.testutils.ReadAssetFile

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.junit.Assert
import org.junit.Test

internal class ExchangeRatesDtoTest {

    @Test
    fun `test exchange rates json response maps to model class`() {
        val response = getExchangeRatesJson()

        Assert.assertEquals(169, response.rates.entries.size)
        Assert.assertEquals("USD", response.base)

        Assert.assertEquals(
            3.673,
            response.rates["AED"]
        )

        Assert.assertEquals(
            322.0,
            response.rates["ZWL"]
        )
    }

    private fun getExchangeRatesJson(): ExchangeRatesDto {
        val gson = GsonBuilder().create()
        val itemType = object : TypeToken<ExchangeRatesDto>() {}.type
        return gson.fromJson(
            ReadAssetFile.readFileFromTestResources("exchangeRates.json"),
            itemType
        )
    }
}
