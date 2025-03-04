package com.example.calandfast.ui.consumable

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calandfast.database.ConsumablesRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ConsumableEditViewModel(savedStateHandle: SavedStateHandle,
                              private val itemsRepository: ConsumablesRepository
) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var consumableUiState by mutableStateOf(ConsumableUiState())
        private set

    private val itemId: Int = checkNotNull(savedStateHandle[ConsumableEditDestination.itemIdArg])

    init {
        viewModelScope.launch {
            consumableUiState = itemsRepository.getConsumableStream(itemId)
                .filterNotNull()
                .first()
                .toConsumableUiState(true)
        }
    }

    /**
     * Update the item in the [ConsumablesRepository]'s data source
     */
    suspend fun updateItem() {
        if (validateInput(consumableUiState.consumableDetails)) {
            itemsRepository.updateConsumable(consumableUiState.consumableDetails.toConsumable())
        }
    }

    /**
     * Updates the [consumableUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(itemDetails: ConsumableDetails) {
        consumableUiState =
            ConsumableUiState(consumableDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }

    private fun validateInput(uiState: ConsumableDetails = consumableUiState.consumableDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && calories.isNotBlank() && lastUsed > 0
        }
    }
}