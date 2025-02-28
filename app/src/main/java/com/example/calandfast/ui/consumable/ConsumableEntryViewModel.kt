package com.example.calandfast.ui.consumable

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.calandfast.database.Consumable
import com.example.calandfast.database.ConsumablesRepository

class ConsumableEntryViewModel(
    private val consumablesRepository: ConsumablesRepository
) :ViewModel() {
        var consumableUiState by mutableStateOf(ConsumableUiState())
    private set

    fun updateUiState(consumableDetails: ConsumableDetails){
        consumableUiState = ConsumableUiState(consumableDetails = consumableDetails, isEntryValid = validateInput(consumableDetails))
    }

    suspend fun saveConsumable(){
        if (validateInput()){
            consumablesRepository.upsertConsumable(consumableUiState.consumableDetails.toConsumable())
        }
    }

    private fun validateInput(uiState: ConsumableDetails = consumableUiState.consumableDetails): Boolean {
        return with(uiState){
            name.isNotBlank() && calories.isNotBlank() && lastUsed.isNotBlank()
        }
    }
}

data class ConsumableUiState(
    val consumableDetails: ConsumableDetails = ConsumableDetails(),
    val isEntryValid: Boolean = false
)

data class ConsumableDetails(
    val consumableId: Int = 0,
    val name: String = "",
    val calories: String = "",
    val lastUsed: String = ""
)
fun ConsumableDetails.toConsumable(): Consumable = Consumable(
    consumableId = consumableId,
    name = name,
    calories = calories.toIntOrNull() ?: 0,
    lastUsed = lastUsed
)
fun Consumable.toConsumableUiState(isEntryValid: Boolean = false): ConsumableUiState = ConsumableUiState(
    consumableDetails = this.toConsumableDetails(),
    isEntryValid = isEntryValid
)

fun Consumable.toConsumableDetails(): ConsumableDetails = ConsumableDetails(
    consumableId = consumableId,
    name = name,
    calories = calories.toString(),
    lastUsed = lastUsed
)
