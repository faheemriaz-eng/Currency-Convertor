package com.faheem.currencyconverstion.di


import android.content.Context
import androidx.room.Room
import com.faheem.currencyconverstion.data.local.daos.CurrencyExchangeDao
import com.faheem.currencyconverstion.data.local.db.AppDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun provideCurrencyDao(appDatabase: AppDB): CurrencyExchangeDao {
        return appDatabase.currencyExchangeDao()
    }

    @Provides
    @Singleton
    fun provideAppDB(@ApplicationContext appContext: Context): AppDB {
        return Room.databaseBuilder(
            appContext,
            AppDB::class.java,
            "CurrencyConversion"
        ).build()
    }

}