package com.cryptotracker.presentation.coinlist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.cryptotracker.core.RepositoryFactory
import com.cryptotracker.presentation.coindetail.CoinDetailDialog
import com.cryptotracker.presentation.coinlist.components.CoinItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinListScreen(
    viewModel: CoinListViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()
    var selectedCoinId by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Crypto Tracker", style = MaterialTheme.typography.headlineSmall)
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->

        PullToRefreshBox(
            isRefreshing = state.isLoading,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            onRefresh = { viewModel.retry() },
            content = {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    state.error != null -> {
                        Text(
                            text = state.error ?: "Unknown error",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(state.coins) { coin ->
                                CoinItem(
                                    coin = coin,
                                    onClick = { selectedCoinId = coin.id }
                                )
                                Divider()
                            }
                        }
                    }
                }
            }
        )

        selectedCoinId?.let { coinId ->
            CoinDetailDialog(
                coinId = coinId,
                repository = RepositoryFactory.getCoinRepository(LocalContext.current),
                onDismiss = { selectedCoinId = null }
            )
        }

    }
}