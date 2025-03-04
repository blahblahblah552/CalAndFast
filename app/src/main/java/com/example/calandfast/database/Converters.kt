package com.example.calandfast.database

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

class Converters {
    @TypeConverter
    fun localDateToInt(value: LocalDate?): Long? {
        return value?.let { value.toEpochDay() }
    }

    @TypeConverter
    fun fromDayOfYear(value: Long?): LocalDate? {
        return value?.let { LocalDate.ofEpochDay(value) }
    }

    fun convertMillisToDate(millis: Long): String {
        val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        return formatter.format(Date(millis))
    }
}
