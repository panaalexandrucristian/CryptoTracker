package com.cryptotracker.presentation.coindetail

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cryptotracker.domain.model.CoinDetail
import com.cryptotracker.fake.FakeCoinRepository
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CoinDetailDialogTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun showsCoinDetailSuccessfully() {
        val fakeDetail = CoinDetail(
            id = "btc",
            name = "Bitcoin",
            symbol = "BTC",
            description = "Most popular cryptocurrency",
            imageUrl = "",
            currentPriceUsd = 50000.0
        )
        val fakeRepo = FakeCoinRepository(coinDetail = fakeDetail)

        composeTestRule.setContent {
            CoinDetailDialog(
                coinId = "btc",
                repository = fakeRepo,
                onDismiss = {}
            )
        }

        composeTestRule.onNodeWithText("Bitcoin").assertIsDisplayed()
        composeTestRule.onNodeWithText("BTC").assertIsDisplayed()
        composeTestRule.onNodeWithText("$50000.00").assertIsDisplayed()
        composeTestRule.onNodeWithText("Most popular cryptocurrency").assertIsDisplayed()
    }

    @Test
    fun showsErrorWhenRepositoryFails() {
        val fakeRepo = FakeCoinRepository(simulateError = true)

        composeTestRule.setContent {
            CoinDetailDialog(
                coinId = "btc",
                repository = fakeRepo,
                onDismiss = {}
            )
        }

        composeTestRule.onNodeWithText("Failed: Fake detail error").assertIsDisplayed()
    }
}