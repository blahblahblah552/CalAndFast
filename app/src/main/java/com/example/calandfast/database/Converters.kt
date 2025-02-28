package com.example.calandfast.database

import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {
    @TypeConverter
    fun localDateToInt(value: LocalDate?): Long? {
        return value?.let { value.toEpochDay() }
    }

    @TypeConverter
    fun fromDayOfYear(value: Long?): LocalDate? {
        return value?.let { LocalDate.ofEpochDay(value) }
    }
}