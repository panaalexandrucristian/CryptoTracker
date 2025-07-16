package com.cryptotracker.presentation.coinlist

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cryptotracker.domain.model.Coin
import com.cryptotracker.fake.FakeCoinRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CoinListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var viewModel: CoinListViewModel

    @Before
    fun setup() {
        val fakeRepository = FakeCoinRepository(
            coins = listOf(
                Coin("btc", "BTC", "Bitcoin", "", 50000.0),
                Coin("eth", "ETH", "Ethereum", "", 3500.0)
            )
        )
        viewModel = CoinListViewModel(fakeRepository)
    }

    @Test
    fun showsCoinListWhenLoaded() {
        composeTestRule.setContent {
            CoinListScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Bitcoin").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ethereum").assertIsDisplayed()
    }
}