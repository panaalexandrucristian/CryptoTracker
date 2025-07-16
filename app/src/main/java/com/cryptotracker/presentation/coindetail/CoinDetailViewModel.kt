package com.cryptotracker.presentation.coindetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cryptotracker.core.Resource
import com.cryptotracker.domain.model.CoinDetail
import com.cryptotracker.domain.repository.CoinRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CoinDetailViewModel(
    private val repository: CoinRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<Resource<CoinDetail>>(Resource.Loading())
    val state: StateFlow<Resource<CoinDetail>> = _state.asStateFlow()

    fun loadCoin(coinId: String) {
        viewModelScope.launch {
            _state.value = Resource.Loading()
            try {
                repository.getCoinDetail(
                    id = coinId,
                    localization = false,
                    tickers = false,
                    marketData = true,
                    communityData = false,
                    developerData = false,
                    sparkline = false
                ).collect { resource ->
                    _state.value = resource
                }
            } catch (e: Exception) {
                _state.value = Resource.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}