package com.cryptotracker.data.remote.dto

import com.cryptotracker.domain.model.CoinDetail
import com.google.gson.annotations.SerializedName

data class CoinDetailDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: DescriptionDto,
    @SerializedName("image")
    val image: ImageDto,
    @SerializedName("market_data")
    val marketData: MarketDataDto
)

data class DescriptionDto(
    @SerializedName("en")
    val en: String
)

data class ImageDto(
    @SerializedName("large")
    val large: String
)

data class MarketDataDto(
    @SerializedName("current_price")
    val currentPrice: Map<String, Double>
)

fun CoinDetailDto.toDomainModel(): CoinDetail {
    return CoinDetail(
        id = id,
        name = name,
        symbol = symbol.uppercase(),
        description = description.en,
        imageUrl = image.large,
        currentPriceUsd = marketData.currentPrice["usd"] ?: 0.0
    )
}