package com.faheem.currencyconverstion.ui.screens.currencies

sealed class CurrencyExchangeUIState {
    object Loading : CurrencyExchangeUIState()
    object Success : CurrencyExchangeUIState()
    data class Error(val message: String) : CurrencyExchangeUIState()
}