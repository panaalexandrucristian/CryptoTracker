package com.cryptotracker.domain.repository

import com.cryptotracker.core.Resource
import com.cryptotracker.domain.model.Coin
import com.cryptotracker.domain.model.CoinDetail
import kotlinx.coroutines.flow.Flow

interface CoinRepository {

    suspend fun getCoins(
    ): Flow<Resource<List<Coin>>>

    suspend fun getCoinDetail(
        id: String,
        localization: Boolean,
        tickers: Boolean,
        marketData: Boolean,
        communityData: Boolean,
        developerData: Boolean,
        sparkline: Boolean
    ): Flow<Resource<CoinDetail>>
}
