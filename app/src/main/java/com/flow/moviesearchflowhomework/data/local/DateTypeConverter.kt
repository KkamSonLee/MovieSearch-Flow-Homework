package com.flow.moviesearchflowhomework.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

@ProvidedTypeConverter
class DateTypeConverter {
    @TypeConverter
    fun fromString(value: String): LocalDateTime =
        LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    @TypeConverter
    fun fromLocalDate(value: LocalDateTime): String = value.toString()
}