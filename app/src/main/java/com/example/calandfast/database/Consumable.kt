package com.example.calandfast.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Locale

@Entity
data class Consumable(
    @PrimaryKey(autoGenerate = true)
    val consumableId: Int = 0,
    val name: String,
    val calories: Int,
    val lastUsed: Long
)

@Entity
data class Today(
    @PrimaryKey(autoGenerate = false)
    val date: Long = LocalDate.now().toEpochDay(),
    val todayCalSum: Int,
    val currentWeek: Int = epochDayToWeekOfYear(date)
)

data class TodayConsumables(
    @Embedded
    val today: Today,
    @Relation(
        parentColumn = "date",
        entityColumn = "lastUsed"
    ) val consumables: List<Consumable> = emptyList()
)

fun epochDayToWeekOfYear(date: Long): Int {
    val tempDate = LocalDate.ofEpochDay(date)
    val weekFields = WeekFields.of(Locale.getDefault())
    return tempDate.get(weekFields.weekOfWeekBasedYear())
}