package com.cryptotracker.data.remote.dto

import org.junit.Assert.assertEquals
import org.junit.Test

class CoinDetailDtoTest {

    @Test
    fun `toDomainModel should map DTO to domain model correctly`() {
        // Arrange
        val dto = CoinDetailDto(
            id = "btc",
            name = "Bitcoin",
            symbol = "btc",
            description = DescriptionDto(en = "The first crypto"),
            image = ImageDto(large = "https://image.url/btc.png"),
            marketData = MarketDataDto(
                currentPrice = mapOf("usd" to 12345.67)
            )
        )

        // Act
        val domainModel = dto.toDomainModel()

        // Assert
        assertEquals("btc", domainModel.id)
        assertEquals("Bitcoin", domainModel.name)
        assertEquals("BTC", domainModel.symbol)
        assertEquals("The first crypto", domainModel.description)
        assertEquals("https://image.url/btc.png", domainModel.imageUrl)
        assertEquals(12345.67, domainModel.currentPriceUsd, 0.001)
    }

    @Test
    fun `toDomainModel should use 0_0 when price is missing`() {
        // Arrange
        val dto = CoinDetailDto(
            id = "eth",
            name = "Ethereum",
            symbol = "eth",
            description = DescriptionDto(en = "Smart contract platform"),
            image = ImageDto(large = "https://image.url/eth.png"),
            marketData = MarketDataDto(
                currentPrice = emptyMap()
            )
        )

        // Act
        val domainModel = dto.toDomainModel()

        // Assert
        assertEquals(0.0, domainModel.currentPriceUsd, 0.001)
    }
}