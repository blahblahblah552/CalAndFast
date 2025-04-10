package com.example.calandfast.ui.weeklyTotals

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
import java.time.ZoneOffset
import java.time.temporal.TemporalAdjusters

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
        itemsRepository.getCurrentWeekConsumable(getStartOfWeekMillis())
            .map { WeeklyUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = WeeklyUiState()
            )


    fun getStartOfWeekMillis(): Long {
        val now = LocalDateTime.now()
        val startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            .withHour(0)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)
        return startOfWeek.toInstant(ZoneOffset.UTC).toEpochMilli()
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
    val itemList: List<Consumable> = listOf()
)