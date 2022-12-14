package com.faheem.currencyconversion.ui.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.faheem.currencyconversion.data.local.CurrenciesLocalDataSource
import com.faheem.currencyconversion.data.remote.CurrenciesRemoteDataSource
import com.faheem.currencyconversion.data.remote.dtos.asEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ExchangeRatesWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val remoteSource: CurrenciesRemoteDataSource,
    private val localSource: CurrenciesLocalDataSource,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val networkResult = remoteSource.getExchangeRates()
        return if (networkResult.isSuccess) {
            networkResult.getOrNull()?.let {
                localSource.saveExchangeRates(it.asEntity())
                Result.success()
            } ?: Result.failure()
        } else {
            Result.failure()
        }
    }
}