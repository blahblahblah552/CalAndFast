package com.example.calandfast.ui.weeklyTotals

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calandfast.database.Consumable
import com.example.calandfast.database.ConsumablesRepository
import com.example.calandfast.ui.consumable.ConsumableDetailsDestination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WeeklyViewModel (
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ConsumablesRepository,
) : ViewModel() {
    private val itemId: Int = checkNotNull(savedStateHandle[ConsumableDetailsDestination.itemIdArg])
    val uiState: StateFlow<WeeklyUiState> =
        itemsRepository.getAllConsumablesStream().map { WeeklyUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = WeeklyUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    fun thisWeek(){
        viewModelScope.launch {
            var weekdayMap = mutableMapOf("Monday" to 0)
            var currentDayCal = 0
            var dayDate: Long = 0L
            val items = uiState.value.itemList

            items.forEach { item ->
                if (dayDate == 0L){
                    dayDate = item.lastUsed
                }
                if (dayDate == item.lastUsed){
                    currentDayCal += item.calories
                } else {
                    dayDate = item.lastUsed
                    currentDayCal = item.calories
                }
            }

        }
    }
}



data class WeeklyUiState(val itemList: List<Consumable> = listOf())