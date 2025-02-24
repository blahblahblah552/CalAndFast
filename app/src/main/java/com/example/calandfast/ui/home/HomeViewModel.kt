package com.example.calandfast.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calandfast.database.Consumable
import com.example.calandfast.database.ConsumablesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel to retrieve all items in the Room database.
 */
class HomeViewModel(consumablesRepository: ConsumablesRepository) : ViewModel() {

    /**
     * Holds home ui state. The list of items are retrieved from [ConsumablesRepository] and mapped to
     * [HomeUiState]
     */
    val homeUiState: StateFlow<HomeUiState> =
        consumablesRepository.getAllConsumablesStream().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Ui State for HomeScreen
 */
data class HomeUiState(val itemList: List<Consumable> = listOf())