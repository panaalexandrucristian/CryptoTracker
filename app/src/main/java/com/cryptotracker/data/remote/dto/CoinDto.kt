package com.cryptotracker.data.remote.dto

import com.cryptotracker.domain.model.Coin
import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object for Coin API response
 */
data class CoinDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("current_price")
    val currentPrice: Double?,
)

fun CoinDto.toDomainModel(): Coin {
    return Coin(
        id = id,
        symbol = symbol.uppercase(),
        name = name,
        image = image,
        currentPrice = currentPrice ?: 0.0
    )
}

