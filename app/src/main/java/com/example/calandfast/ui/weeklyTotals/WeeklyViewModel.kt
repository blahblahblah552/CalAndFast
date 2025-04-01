package com.example.calandfast.ui.weeklyTotals

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calandfast.database.Consumable
import com.example.calandfast.database.ConsumablesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar

var ZONE_OFFSET = 14927011L

class WeeklyViewModel (
    itemsRepository: ConsumablesRepository,
) : ViewModel() {
    private val weekdayMap = mutableMapOf<DayOfWeek, Int>(
        DayOfWeek.MONDAY to 0,
        DayOfWeek.TUESDAY to 0,
        DayOfWeek.WEDNESDAY to 0,
        DayOfWeek.THURSDAY to 0,
        DayOfWeek.FRIDAY to 0,
        DayOfWeek.SATURDAY to 0,
        DayOfWeek.SUNDAY to 0
        )

    val uiState: StateFlow<WeeklyUiState> =
        itemsRepository.getCurrentWeekConsumable(getStartOfWeek())
            .map { WeeklyUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = WeeklyUiState()
            )


    private fun getStartOfWeek(): Long {
        val currentTime = System.currentTimeMillis()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentTime
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        // Subtract days to reach Monday (1), adjust if necessary
        var daysToSubtract = dayOfWeek - 1

        if (daysToSubtract < 0) {
            daysToSubtract += 7
        }

        calendar.add(Calendar.DAY_OF_MONTH, - daysToSubtract)
        Log.d("WeeklyViewModel", "getStartOfWeek: ${calendar.timeInMillis + ZONE_OFFSET}")
        return calendar.timeInMillis + ZONE_OFFSET
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    fun initThisWeek(items: List<Consumable>): Map<DayOfWeek,Int>{
        items.forEach { item ->
            weekdayMap[dayOfWeekFromMillis(item.lastUsed)] = weekdayMap[dayOfWeekFromMillis(item.lastUsed)]!! + item.calories
        }
        return weekdayMap
    }

    fun mapToList(tempMap: Map<DayOfWeek,Int>):List<Float>{
        val tempList: MutableList<Float> = mutableListOf()
        tempMap.forEach{ (_, value) ->
            tempList.add(value.toFloat())
        }
        return tempList
    }
}

private fun dayOfWeekFromMillis(millis: Long, zoneId: ZoneId = ZoneId.systemDefault()): DayOfWeek {
    val instant = Instant.ofEpochMilli(millis)
    val localDateTime = LocalDateTime.ofInstant(instant, zoneId)
    return localDateTime.dayOfWeek
}


data class WeeklyUiState(
    val itemList: List<Consumable> = listOf())