package com.faheem.currencyconversion.ui.screens.currencies

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.faheem.currencyconversion.data.models.Currency
import com.faheem.currencyconversion.data.models.Rate
import com.faheem.currencyconversion.databinding.ActivityExchangeRatesBinding
import com.faheem.currencyconversion.ui.screens.currencies.adapter.RatesAdapter
import com.faheem.currencyconversion.ui.utils.observe
import com.faheem.currencyconversion.ui.workmanager.ExchangeRatesWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class ExchangeRatesActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityExchangeRatesBinding

    private val viewModel: CurrencyExchangeVM by viewModels()

    @Inject
    lateinit var adapter: RatesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityExchangeRatesBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewModel.fetchCurrenciesAndRates()
        initPeriodicWorkRequest()
        viewModelObservers()
        initViews()
    }

    private fun initViews() {
        initRecyclerView()
        initCurrenciesSpinner()
        initAmountInputField()
    }

    private fun initAmountInputField() {
        viewBinding.etAmount.doAfterTextChanged {
            viewModel.onAmountChange(it.toString())
        }
    }

    private fun initRecyclerView() {
        viewBinding.rvRates.adapter = adapter
    }

    private fun initCurrenciesSpinner() {
        viewBinding.currencySpinner.onItemSelectedListener = currenciesSpinnerItemListener
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

    private fun handleCurrencies(currencies: List<Currency>) {
        val dataAdapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            viewModel.formattedCurrencies(currencies)
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        viewBinding.currencySpinner.adapter = dataAdapter
    }

    private fun handleRates(rates: List<Rate>) {
        adapter.setList(rates)
    }

    private fun handleUIState(uiState: CurrencyExchangeUIState) {
        when (uiState) {
            is CurrencyExchangeUIState.Loading -> viewBinding.progressBar.visibility = View.VISIBLE
            is CurrencyExchangeUIState.Success -> viewBinding.progressBar.visibility = View.GONE
            is CurrencyExchangeUIState.Error -> {
                viewBinding.progressBar.visibility = View.GONE
                Toast.makeText(this, uiState.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun viewModelObservers() {
        observe(viewModel.uiState, ::handleUIState)
        observe(viewModel.currencies, ::handleCurrencies)
        observe(viewModel.rates, ::handleRates)
    }


    private fun initPeriodicWorkRequest() {
        val workManager = WorkManager.getInstance(this)

        val syncRatesWorker = PeriodicWorkRequestBuilder<ExchangeRatesWorker>(
            WORKER_INTERVAL, TimeUnit.MINUTES
        ).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        )
            .addTag(WORKER_TAG)
            .build()

        workManager.enqueueUniquePeriodicWork(
            WORKER_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            syncRatesWorker
        )
    }

    companion object {
        const val WORKER_INTERVAL = 30L
        const val WORKER_TAG = "syncRatesWorker"
        const val WORKER_NAME = "periodicSyncRates"
    }
}
