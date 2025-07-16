package com.cryptotracker.domain.model

data class CoinDetail(
    val id: String,
    val name: String,
    val symbol: String,
    val description: String,
    val imageUrl: String,
    val currentPriceUsd: Double
)