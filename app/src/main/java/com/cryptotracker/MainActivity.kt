package com.cryptotracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cryptotracker.core.RepositoryFactory
import com.cryptotracker.presentation.coinlist.CoinListScreen
import com.cryptotracker.presentation.coinlist.CoinListViewModel
import com.cryptotracker.presentation.coinlist.CoinListViewModelFactory
import com.cryptotracker.ui.theme.CryptoTrackerTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CryptoTrackerTheme {
                val repository = RepositoryFactory.getCoinRepository(applicationContext)
                val viewModel: CoinListViewModel = viewModel(
                    factory = CoinListViewModelFactory(repository)
                )

                CoinListScreen(
                    viewModel = viewModel
                )
            }
        }
    }
}