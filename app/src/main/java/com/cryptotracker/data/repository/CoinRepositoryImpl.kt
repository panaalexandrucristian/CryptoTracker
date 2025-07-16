package com.cryptotracker.data.repository

import com.cryptotracker.core.Resource
import com.cryptotracker.data.local.dao.CoinDao
import com.cryptotracker.data.local.entity.toCoin
import com.cryptotracker.data.local.entity.toCoinEntity
import com.cryptotracker.data.remote.api.CoinGeckoApi
import com.cryptotracker.data.remote.dto.toDomainModel
import com.cryptotracker.domain.model.Coin
import com.cryptotracker.domain.model.CoinDetail
import com.cryptotracker.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CoinRepositoryImpl(
    private val api: CoinGeckoApi,
    private val coinDao: CoinDao
) : CoinRepository {

    override suspend fun getCoins(): Flow<Resource<List<Coin>>> = flow {
        emit(Resource.Loading())

        try {
            // 1. Emit cached data immediately
            val cachedCoins = coinDao.getAllCoins().map { it.toCoin() }
            if (cachedCoins.isNotEmpty()) {
                emit(Resource.Success(cachedCoins))
            }

            // 2. Fetch fresh data from API
            val freshCoins = api.getCoins(
                vsCurrency = "usd",
                order = "market_cap_desc",
                perPage = 100,
                page = 1,
                sparkline = false
            ).map { it.toDomainModel() }

            coinDao.replaceAllCoins(freshCoins.map { it.toCoinEntity() })

            emit(Resource.Success(freshCoins))

        } catch (e: Exception) {
            val cachedCoins = coinDao.getAllCoins().map { it.toCoin() }
            if (cachedCoins.isNotEmpty()) {
                emit(Resource.Success(cachedCoins))
            } else {
                emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
            }
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
        try {
            val response = api.getCoinDetail(id)
            emit(Resource.Success(response.toDomainModel()))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
        }
    }

}
