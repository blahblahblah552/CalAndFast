package com.example.calandfast.ui.consumable

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calandfast.database.Consumable
import com.example.calandfast.database.ConsumablesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ConsumableDetailsViewModel (
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ConsumablesRepository,
) : ViewModel() {

    private val itemId: Int = checkNotNull(savedStateHandle[ConsumableDetailsDestination.itemIdArg])

    /**
     * Holds the item details ui state. The data is retrieved from [ConsumablesRepository] and mapped to
     * the UI state.
     */
    val uiState: StateFlow<ItemDetailsUiState> =
        itemsRepository.getConsumableStream(itemId)
            .filterNotNull()
            .map {
                ItemDetailsUiState(outOfStock = it.calories <= 0, itemDetails = it.toConsumableDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ItemDetailsUiState()
            )

    /**
     * Reduces the item quantity by one and update the [ConsumablesRepository]'s data source.
     */
    fun reduceQuantityByOne() {
        viewModelScope.launch {
            val currentItem = uiState.value.itemDetails.toConsumable()
            if (currentItem.calories > 0) {
                itemsRepository.upsertConsumable(currentItem.copy(calories = currentItem.calories - 1))
            }
        }
    }

    fun copyItem(){
        viewModelScope.launch {
            val currentItem = uiState.value.itemDetails.toConsumable()
            itemsRepository.insertConsumable(Consumable(
                name = currentItem.name,
                calories = currentItem.calories,
                lastUsed = System.currentTimeMillis()
            ))
        }
    }

    /**
     * Deletes the item from the [ConsumablesRepository]'s data source.
     */
    suspend fun deleteItem() {
        itemsRepository.deleteConsumable(uiState.value.itemDetails.toConsumable())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for ItemDetailsScreen
 */
data class ItemDetailsUiState(
    val outOfStock: Boolean = true,
    val itemDetails: ConsumableDetails = ConsumableDetails()
)