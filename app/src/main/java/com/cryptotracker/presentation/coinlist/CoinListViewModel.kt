package com.cryptotracker.presentation.coinlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cryptotracker.domain.model.Coin
import com.cryptotracker.domain.repository.CoinRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CoinListViewModel(
    private val coinRepository: CoinRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CoinListState())
    val state: StateFlow<CoinListState> = _state.asStateFlow()

    init {
        loadCoins()
    }

    private fun loadCoins() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val result  = coinRepository.getCoins()
                result.collect { resource ->
                    when (resource) {
                        is com.cryptotracker.core.Resource.Loading -> {
                            _state.value = _state.value.copy(isLoading = true, error = null)
                        }
                        is com.cryptotracker.core.Resource.Success -> {
                            _state.value = _state.value.copy(
                                coins = resource.data ?: emptyList(),
                                isLoading = false,
                                error = null
                            )
                        }
                        is com.cryptotracker.core.Resource.Error -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                error = resource.message ?: "Unknown error occurred"
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun retry() {
        loadCoins()
    }
}

data class CoinListState(
    val coins: List<Coin> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

// ViewModelFactory pentru dependency injection
class CoinListViewModelFactory(
    private val coinRepository: CoinRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoinListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CoinListViewModel(coinRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
