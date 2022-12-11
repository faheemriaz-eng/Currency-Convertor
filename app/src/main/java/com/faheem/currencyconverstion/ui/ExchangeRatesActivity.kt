package com.faheem.currencyconverstion.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.faheem.currencyconverstion.databinding.ActivityExchangeRatesBinding
import com.faheem.currencyconverstion.domain.models.Currency
import com.faheem.currencyconverstion.domain.models.Rate
import com.faheem.currencyconverstion.ui.adapter.RatesAdapter
import com.faheem.currencyconverstion.ui.utils.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExchangeRatesActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityExchangeRatesBinding

    private val viewModel: CurrencyExchangeVM by viewModels()

    lateinit var adapter: RatesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityExchangeRatesBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewModelObservers()
        initRecyclerView()
        initCurrenciesSpinner()
    }

    private fun initRecyclerView() {
        adapter = RatesAdapter()
        viewBinding.rvRates.adapter = adapter
    }

    private fun initCurrenciesSpinner() {
        viewBinding.etAmount.doAfterTextChanged {
            viewModel.onAmountChange(it.toString())
        }
        viewBinding.currencySpinner.onItemSelectedListener = currenciesSpinnerItemListener
    }

    private fun handleCurrencies(currencies: List<Currency>) {
        val dataAdapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            currencies.map { currency -> currency.currencyCode + " : " + currency.currencyName } as ArrayList<String>
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        viewBinding.currencySpinner.adapter = dataAdapter
    }

    private fun handleRates(rates: List<Rate>) {
        adapter.setList(rates)
    }

    private val currenciesSpinnerItemListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            viewModel.onSelectedCurrency(
                position = position,
                amount = viewBinding.etAmount.text.toString().toDoubleOrNull() ?: 0.0
            )
        }

        override fun onNothingSelected(parent: AdapterView<*>?) = Unit

    }

    private fun viewModelObservers() {
        observe(viewModel.currencies, ::handleCurrencies)
        observe(viewModel.rates, ::handleRates)
    }
}