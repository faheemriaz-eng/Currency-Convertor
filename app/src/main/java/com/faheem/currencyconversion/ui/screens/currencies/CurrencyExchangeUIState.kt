package com.faheem.currencyconversion.ui.screens.currencies

sealed class CurrencyExchangeUIState {
    object Loading : CurrencyExchangeUIState()
    object Success : CurrencyExchangeUIState()
    data class Error(val message: String) : CurrencyExchangeUIState()
}