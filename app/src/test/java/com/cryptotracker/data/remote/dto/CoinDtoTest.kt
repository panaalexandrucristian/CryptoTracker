package com.cryptotracker.data.remote.dto

import org.junit.Assert.assertEquals
import org.junit.Test

class CoinDtoTest {

    @Test
    fun `toDomainModel should map DTO to domain model correctly`() {
        val dto = CoinDto(
            id = "btc",
            symbol = "btc",
            name = "Bitcoin",
            image = "https://image.url/btc.png",
            currentPrice = 45678.9
        )

        // Act
        val domainModel = dto.toDomainModel()

        // Assert
        assertEquals("btc", domainModel.id)
        assertEquals("BTC", domainModel.symbol)
        assertEquals("Bitcoin", domainModel.name)
        assertEquals("https://image.url/btc.png", domainModel.image)
        assertEquals(45678.9, domainModel.currentPrice, 0.001)
    }

    @Test
    fun `toDomainModel should set currentPrice to 0_0 if null`() {
        // Arrange
        val dto = CoinDto(
            id = "eth",
            symbol = "eth",
            name = "Ethereum",
            image = "https://image.url/eth.png",
            currentPrice = null
        )

        // Act
        val domainModel = dto.toDomainModel()

        // Assert
        assertEquals(0.0, domainModel.currentPrice, 0.001)
    }
}