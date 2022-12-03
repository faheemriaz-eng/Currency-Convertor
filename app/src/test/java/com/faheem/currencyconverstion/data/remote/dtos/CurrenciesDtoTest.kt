package com.faheem.currencyconverstion.data.remote.dtos

import com.faheem.currencyconverstion.testutils.ReadAssetFile
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.junit.Assert
import org.junit.Test

internal class CurrenciesDtoTest {

    @Test
    fun `test currencies json response maps to model class`() {
        val response = getCurrenciesJson()

        Assert.assertEquals(170, response.currencies.entries.size)

        Assert.assertEquals(
            "United Arab Emirates Dirham",
            response.currencies["AED"]
        )

        Assert.assertEquals(
            "Zimbabwean Dollar",
            response.currencies["ZWL"]
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