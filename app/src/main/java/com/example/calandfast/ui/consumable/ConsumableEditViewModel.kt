package com.example.calandfast.ui.consumable

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calandfast.database.ConsumablesRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ConsumableEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ConsumablesRepository
) : ViewModel() {

    var consumableUiState by mutableStateOf(ConsumableUiState())
        private set

    var selectedDate by mutableLongStateOf(System.currentTimeMillis())
    var showModal by mutableStateOf(false)

    private val itemId: Int = checkNotNull(savedStateHandle[ConsumableEditDestination.itemIdArg])

    private val _navigateBack = MutableSharedFlow<Boolean>()
    val navigateBack: SharedFlow<Boolean> = _navigateBack

    init {
        viewModelScope.launch {
            consumableUiState = itemsRepository.getConsumableStream(itemId)
                .filterNotNull()
                .first()
                .toConsumableUiState(true)
            selectedDate = consumableUiState.consumableDetails.lastUsed
        }
    }

    fun updateName(name: String) {
        updateUiState(consumableUiState.consumableDetails.copy(name = name))
    }

    fun updateCalories(calories: String) {
        updateUiState(consumableUiState.consumableDetails.copy(calories = calories))
    }

    fun updateLastUsed(lastUsed: Long) {
        selectedDate = lastUsed
        updateUiState(consumableUiState.consumableDetails.copy(lastUsed = lastUsed))
    }

    fun updateShowModal(show: Boolean) {
        showModal = show
    }

    suspend fun updateItem() {
        if (validateInput(consumableUiState.consumableDetails)) {
            itemsRepository.updateConsumable(consumableUiState.consumableDetails.toConsumable())
            _navigateBack.emit(true)
        }
    }

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