package com.cryptotracker.data.remote.api

import com.cryptotracker.data.remote.dto.CoinDetailDto
import com.cryptotracker.data.remote.dto.CoinDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinGeckoApi {

    @GET("coins/markets")
    suspend fun getCoins(
        @Query("vs_currency") vsCurrency: String = "usd",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 100,
        @Query("page") page: Int = 1,
        @Query("sparkline") sparkline: Boolean = false
    ): List<CoinDto>

    @GET("coins/{id}")
    suspend fun getCoinDetail(
        @Path("id") coinId: String
    ): CoinDetailDto

}