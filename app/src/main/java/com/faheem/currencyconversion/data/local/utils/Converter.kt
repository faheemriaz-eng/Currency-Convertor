package com.faheem.currencyconversion.data.local.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converter {

    @TypeConverter
    fun toCurrencyMap(value: String): Map<String, String> =
        Gson().fromJson(value, object : TypeToken<Map<String, String>>() {}.type)

    @TypeConverter
    fun fromCurrencyMap(value: Map<String, String>): String =
        Gson().toJson(value)

    @TypeConverter
    fun toRatesMap(value: String): Map<String, Double> =
        Gson().fromJson(value, object : TypeToken<Map<String, Double>>() {}.type)

    @TypeConverter
    fun fromRatesMap(value: Map<String, Double>): String =
        Gson().toJson(value)

}