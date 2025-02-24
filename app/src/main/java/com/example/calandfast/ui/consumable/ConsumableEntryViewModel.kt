package com.example.calandfast.ui.consumable

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calandfast.database.Consumable
import com.example.calandfast.database.ConsumablesRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate

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
    val id: Int = 0,
    val name: String = "",
    val calories: String = "",
    val lastUsed: String = ""
)
fun ConsumableDetails.toConsumable(): Consumable = Consumable(
    id = id,
    name = name,
    calories = calories.toInt() ?: 0,
    lastUsed = lastUsed
)
fun Consumable.toConsumableUiState(isEntryValid: Boolean = false): ConsumableUiState = ConsumableUiState(
    consumableDetails = this.toConsumableDetails(),
    isEntryValid = isEntryValid
)

fun Consumable.toConsumableDetails(): ConsumableDetails = ConsumableDetails(
    id = id,
    name = name,
    calories = calories.toString(),
    lastUsed = lastUsed
)
