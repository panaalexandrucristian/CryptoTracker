package com.cryptotracker.core

import android.content.Context
import com.cryptotracker.data.local.database.CoinDatabase
import com.cryptotracker.data.remote.api.ApiClient
import com.cryptotracker.data.repository.CoinRepositoryImpl
import com.cryptotracker.domain.repository.CoinRepository

object RepositoryFactory {

    @Volatile
    private var coinRepository: CoinRepository? = null

    fun getCoinRepository(context: Context): CoinRepository {
        return coinRepository ?: synchronized(this) {
            coinRepository ?: createCoinRepository(context).also {
                coinRepository = it
            }
        }
    }

    private fun createCoinRepository(context: Context): CoinRepository {
        val database = CoinDatabase.getDatabase(context)
        val api = ApiClient.coinGeckoApi

        return CoinRepositoryImpl(
            api = api,
            coinDao = database.coinDao()
        )
    }

}
