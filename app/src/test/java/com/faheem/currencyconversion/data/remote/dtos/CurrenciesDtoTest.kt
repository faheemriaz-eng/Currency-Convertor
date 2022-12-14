package com.faheem.currencyconversion.data.remote.dtos

import com.faheem.currencyconversion.testutils.ReadAssetFile
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.junit.Assert
import org.junit.Test

internal class CurrenciesDtoTest {

    @Test
    fun `test currencies json response maps to model class`() {
        val response = getCurrenciesJson()

        Assert.assertEquals(170, response.currencies?.entries?.size)

        Assert.assertEquals(
            "United Arab Emirates Dirham",
            response.currencies?.get("AED")
        )

        Assert.assertEquals(
            "Zimbabwean Dollar",
            response.currencies?.get("ZWL")
        )
    }

    private fun getCurrenciesJson(): CurrenciesDto {
        val gson = GsonBuilder().create()
        val itemType = object : TypeToken<CurrenciesDto>() {}.type
        return gson.fromJson(
            ReadAssetFile.readFileFromTestResources("currencies.json"),
            itemType
        )
    }
}