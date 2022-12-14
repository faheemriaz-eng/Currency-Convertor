package com.faheem.currencyconversion.ui.utils

import java.text.DecimalFormat

fun Double.roundUpTo3Decimal(): Double {
    return DecimalFormat("########0.000").format(this).toDouble()
}