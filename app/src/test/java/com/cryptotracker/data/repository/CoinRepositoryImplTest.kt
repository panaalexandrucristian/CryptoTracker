package com.cryptotracker.data.repository

import com.cryptotracker.core.Resource
import com.cryptotracker.data.local.dao.CoinDao
import com.cryptotracker.data.local.entity.toCoinEntity
import com.cryptotracker.data.remote.api.CoinGeckoApi
import com.cryptotracker.data.remote.dto.CoinDetailDto
import com.cryptotracker.data.remote.dto.CoinDto
import com.cryptotracker.data.remote.dto.DescriptionDto
import com.cryptotracker.data.remote.dto.ImageDto
import com.cryptotracker.data.remote.dto.MarketDataDto
import com.cryptotracker.data.remote.dto.toDomainModel
import com.cryptotracker.domain.model.Coin
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CoinRepositoryImplTest {

    private lateinit var api: CoinGeckoApi
    private lateinit var dao: CoinDao
    private lateinit var repository: CoinRepositoryImpl

    @Before
    fun setup() {
        api = mockk()
        dao = mockk(relaxed = true)
        repository = CoinRepositoryImpl(api, dao)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getCoinDetail emits loading then success`() = runTest {
        // Arrange
        val fakeDto = CoinDetailDto(
            id = "btc",
            name = "Bitcoin",
            symbol = "btc",
            description = DescriptionDto(en = "The first crypto"),
            image = ImageDto(large = "https://image.url/btc.png"),
            marketData = MarketDataDto(
                currentPrice = mapOf("usd" to 12345.67)
            )
        )

        coEvery { api.getCoinDetail("btc") } returns fakeDto
        // Act
        val result = repository.getCoinDetail(
            id = "btc",
            localization = false,
            tickers = false,
            marketData = false,
            communityData = false,
            developerData = false,
            sparkline = false
        ).toList()

        // Assert
        assertTrue(result.first() is Resource.Loading)
        assertTrue(result[1] is Resource.Success)
        assertEquals(fakeDto.toDomainModel(), (result[1] as Resource.Success).data)

        coVerify(exactly = 1) { api.getCoinDetail("btc") }
    }

    @Test
    fun `getCoins emits cached data and then remote data`() = runTest {
        // Given
        val localCoins = listOf(
            Coin(id = "btc", name = "Bitcoin", symbol = "BTC", image = "", currentPrice = 20000.0)
        )
        val remoteCoins = listOf(
            CoinDto(id = "eth", symbol = "eth", name = "Ethereum", image = "img", currentPrice = 3000.0)
        )

        // Mock local data (from Room)
        coEvery { dao.getAllCoins() } returns localCoins.map { it.toCoinEntity() }

        // Mock remote data (from API)
        coEvery {
            api.getCoins(
                vsCurrency = any(),
                order = any(),
                perPage = any(),
                page = any(),
                sparkline = any(),
            )
        } returns remoteCoins

        // Act
        val result = repository.getCoins().toList()
        println("Result: $result")
        assertTrue(result.first() is Resource.Loading)
        assertTrue(result[1] is Resource.Success)
        assertEquals(localCoins, (result[1] as Resource.Success).data)
        assertTrue(result[2] is Resource.Success)
        assertEquals(remoteCoins.map { it.toDomainModel() }, (result[2] as Resource.Success).data)

    }
}