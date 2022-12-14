package com.faheem.currencyconverstion.ui.screens.currencies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faheem.currencyconverstion.data.models.Currency
import com.faheem.currencyconverstion.data.models.ExchangeRate
import com.faheem.currencyconverstion.data.models.Rate
import com.faheem.currencyconverstion.data.repositories.CurrenciesRepository
import com.faheem.currencyconverstion.ui.utils.roundUpTo3Decimal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CurrencyExchangeVM @Inject constructor(private val repository: CurrenciesRepository) :
    ViewModel() {
    private val _uiState: MutableLiveData<CurrencyExchangeUIState> = MutableLiveData()
    val uiState: LiveData<CurrencyExchangeUIState> = _uiState

    private var originalExchangeRate: ExchangeRate? = null
    private var selectedCurrencyCode: String? = null

    private val _currencies: MutableLiveData<List<Currency>> = MutableLiveData()
    val currencies: LiveData<List<Currency>> = _currencies

    private val _rates: MutableLiveData<List<Rate>> = MutableLiveData()
    val rates: LiveData<List<Rate>> = _rates

    fun fetchCurrenciesAndRates() {
        loadData { currencies, rates ->
            if (currencies.isSuccess && rates.isSuccess)
                _uiState.value = CurrencyExchangeUIState.Success
            handleCurrenciesResult(currencies)
            handleRatesResult(rates)
        }
    }

    private fun handleCurrenciesResult(currencies: Result<List<Currency>>) {
        currencies.onSuccess {
            _currencies.value = it
        }.onFailure {
            _currencies.value = listOf()
            _uiState.value = CurrencyExchangeUIState.Error(it.message ?: "Something went wrong")
        }
    }

    private fun handleRatesResult(rates: Result<ExchangeRate?>) {
        rates.onSuccess {
            originalExchangeRate = it
            _rates.value = it?.rates
        }.onFailure {
            _rates.value = listOf()
            _uiState.value = CurrencyExchangeUIState.Error(it.message ?: "Something went wrong")
        }
    }

    private fun loadData(result: (Result<List<Currency>>, Result<ExchangeRate?>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.postValue(CurrencyExchangeUIState.Loading)
            val defCurrencies = async { repository.loadCurrencies() }
            val defRates = async { repository.loadExchangeRates() }

            withContext(Dispatchers.Main) {
                result(defCurrencies.await(), defRates.await())
            }
        }
    }

    fun onAmountChange(amount: String) {
        selectedCurrencyCode?.let {
            convertExchangeRate(it, amount.toDoubleOrNull() ?: 0.0)
        }
    }

    fun onSelectedCurrency(position: Int, amount: Double) {
        selectedCurrencyCode = _currencies.value?.get(position)?.currencyCode
        selectedCurrencyCode?.let {
            convertExchangeRate(it, amount)
        }
    }

    fun formattedCurrencies(currencies: List<Currency>): List<String> =
        currencies.map { currency -> currency.currencyCode + " : " + currency.currencyName }

    fun convertExchangeRate(selectedCurrencyCode: String, amount: Double) {
        if (selectedCurrencyCode == originalExchangeRate?.baseCurrencyCode)
            convertIfSourceIsUSD(amount)
        else
            convertIfSourceIsNonUSD(amount, selectedCurrencyCode)
    }

    private fun convertIfSourceIsUSD(amount: Double) {
        _rates.value = originalExchangeRate?.rates?.map {
            Rate(it.currencyCode, (amount * it.rate).roundUpTo3Decimal())
        }
    }

    private fun convertIfSourceIsNonUSD(amount: Double, selectedCurrencyCode: String) {
        val sourceRate =
            originalExchangeRate?.rates?.find { it.currencyCode == selectedCurrencyCode }

        _rates.value = originalExchangeRate?.rates?.map {
            val rate = it.rate.div(sourceRate?.rate ?: 0.0)
            Rate(it.currencyCode, (amount * rate).roundUpTo3Decimal())
        }
    }
}