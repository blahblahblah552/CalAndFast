package com.example.calandfast.ui.weeklyTotals

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calandfast.database.Consumable
import com.example.calandfast.database.ConsumablesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

var WEEK = 604800000

class WeeklyViewModel (
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ConsumablesRepository,
) : ViewModel() {
    private val weekdayMap = mutableMapOf<DayOfWeek, Int>()
    private var _tuesday = MutableStateFlow(0)
    val myMutableState: MutableStateFlow<Int> = _tuesday
    val uiState: StateFlow<WeeklyUiState> =
        itemsRepository.getCurrentWeekConsumable(System.currentTimeMillis() - WEEK)
            .map { WeeklyUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = WeeklyUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }



    fun initThisWeek() {

        var dayOfWeekCal = 0
        val items = uiState.value.itemList

        items.forEach { item ->
            if (!weekdayMap.containsKey(dayOfWeekFromMillis(item.lastUsed))) {
                dayOfWeekCal = 0
            }
            dayOfWeekCal += item.calories
            weekdayMap[dayOfWeekFromMillis(item.lastUsed)] = dayOfWeekCal
        }
        _tuesday.value = weekdayMap.getOrDefault(DayOfWeek.TUESDAY, 0)
    }
}

private fun dayOfWeekFromMillis(millis: Long, zoneId: ZoneId = ZoneId.systemDefault()): DayOfWeek {
    val instant = Instant.ofEpochMilli(millis)
    val localDateTime = LocalDateTime.ofInstant(instant, zoneId)
    return localDateTime.dayOfWeek
}


data class WeeklyUiState(val itemList: List<Consumable> = listOf())