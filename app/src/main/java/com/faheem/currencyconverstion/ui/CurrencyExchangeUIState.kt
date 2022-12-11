package com.faheem.currencyconverstion.ui

sealed class CurrencyExchangeUIState {
    object Loading : CurrencyExchangeUIState()
    object Success : CurrencyExchangeUIState()
    data class Error(val message: String) : CurrencyExchangeUIState()
}