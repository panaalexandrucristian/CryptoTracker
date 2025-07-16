package com.cryptotracker.fake

import com.cryptotracker.core.Resource
import com.cryptotracker.domain.model.Coin
import com.cryptotracker.domain.model.CoinDetail
import com.cryptotracker.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeCoinRepository(
    private val coins: List<Coin> = emptyList(),
    private val coinDetail: CoinDetail? = null,
    private val simulateError: Boolean = false,
) : CoinRepository {

    override suspend fun getCoins(): Flow<Resource<List<Coin>>> = flow {
        emit(Resource.Loading())
        if (simulateError) {
            emit(Resource.Error("Fake error"))
        } else {
            emit(Resource.Success(coins))
        }
    }

    override suspend fun getCoinDetail(
        id: String,
        localization: Boolean,
        tickers: Boolean,
        marketData: Boolean,
        communityData: Boolean,
        developerData: Boolean,
        sparkline: Boolean
    ): Flow<Resource<CoinDetail>> = flow {
        emit(Resource.Loading())
        if (simulateError || coinDetail == null) {
            emit(Resource.Error("Fake detail error"))
        } else {
            emit(Resource.Success(coinDetail))
        }
    }
}