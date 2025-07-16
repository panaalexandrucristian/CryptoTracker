package com.cryptotracker.presentation.coindetail

import app.cash.turbine.test
import com.cryptotracker.core.Resource
import com.cryptotracker.domain.model.CoinDetail
import com.cryptotracker.domain.repository.CoinRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CoinDetailViewModelTest {

    private val repository: CoinRepository = mockk()
    private lateinit var viewModel: CoinDetailViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CoinDetailViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadCoin emits loading then success`() = runTest {
        // Given
        val coinDetail = CoinDetail(
            id = "btc",
            name = "Bitcoin",
            symbol = "BTC",
            description = "A crypto",
            imageUrl = "https://image.url",
            currentPriceUsd = 20000.0
        )

        coEvery { repository.getCoinDetail(any(), any(), any(), any(), any(), any(), any()) } returns flow {
            emit(Resource.Loading())
            emit(Resource.Success(coinDetail))
        }

        // When
        viewModel.loadCoin("btc")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.state.test {
            val success = awaitItem()
            assertTrue(success is Resource.Success)
            assertEquals(coinDetail, (success as Resource.Success).data)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadCoin emits error on exception`() = runTest {
        // Given
        val errorMessage = "Something went wrong"
        coEvery { repository.getCoinDetail(any(), any(), any(), any(), any(), any(), any()) } throws RuntimeException(errorMessage)

        // When
        viewModel.loadCoin("btc")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.state.test {
            val first = awaitItem()
            assertTrue(first is Resource.Error)
            assertEquals(errorMessage, (first as Resource.Error).message)
            cancelAndIgnoreRemainingEvents()
        }
    }
}