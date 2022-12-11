package com.faheem.currencyconverstion.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faheem.currencyconverstion.domain.models.Currency
import com.faheem.currencyconverstion.domain.models.ExchangeRate
import com.faheem.currencyconverstion.domain.models.Rate
import com.faheem.currencyconverstion.domain.repository.CurrenciesRepository
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
    var uiState: MutableLiveData<CurrencyExchangeUIState> = MutableLiveData()

    private val _currencies: MutableLiveData<List<Currency>> = MutableLiveData()
    val currencies: LiveData<List<Currency>>
        get() = _currencies

    private val _rates: MutableLiveData<List<Rate>> = MutableLiveData()
    val rates: LiveData<List<Rate>>
        get() = _rates

    fun fetchCurrenciesAndRates() {
        loadData { currencies, rates ->
            if (currencies.isSuccess && rates.isSuccess)
                _uiState.value = CurrencyExchangeUIState.Success

            currencies.onSuccess {
                _currencies.value = it
            }.onFailure {
                _uiState.value = CurrencyExchangeUIState.Error(it.message ?: "Something went wrong")
            }
            rates.onSuccess {
                _rates.value = it?.rates
            }.onFailure {
                _uiState.value = CurrencyExchangeUIState.Error(it.message ?: "Something went wrong")
            }
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
}