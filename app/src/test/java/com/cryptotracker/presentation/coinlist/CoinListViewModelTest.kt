package com.cryptotracker.presentation.coinlist

import app.cash.turbine.test
import com.cryptotracker.core.Resource
import com.cryptotracker.domain.model.Coin
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
class CoinListViewModelTest {

    private val repository = mockk<CoinRepository>()
    private lateinit var viewModel: CoinListViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadCoins emits loading then success`() = runTest {
        // Given
        val coins = listOf(
            Coin(id = "btc", name = "Bitcoin", symbol = "BTC", image = "image", currentPrice = 12345.0),
            Coin(id = "eth", name = "Ethereum", symbol = "ETH", image = "image", currentPrice = 5678.0)
        )

        coEvery { repository.getCoins() } returns flow {
            emit(Resource.Loading())
            emit(Resource.Success(coins))
        }

        // When
        viewModel = CoinListViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.state.test {
            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertEquals(coins, successState.coins)
            assertNull(successState.error)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadCoins emits error when repository fails`() = runTest {
        val errorMsg = "Network failure"

        coEvery { repository.getCoins() } returns flow {
            emit(Resource.Loading())
            emit(Resource.Error(errorMsg))
        }

        viewModel = CoinListViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.state.test {
            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertTrue(errorState.error?.contains(errorMsg) == true)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadCoins emits error when exception is thrown`() = runTest {
        coEvery { repository.getCoins() } throws RuntimeException("Unexpected")

        viewModel = CoinListViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.state.test {
            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertTrue(errorState.error?.contains("Unexpected") == true)

            cancelAndIgnoreRemainingEvents()
        }
    }
}