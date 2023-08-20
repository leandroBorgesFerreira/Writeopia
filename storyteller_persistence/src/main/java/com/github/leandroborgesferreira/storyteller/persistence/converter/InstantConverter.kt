package com.github.leandroborgesferreira.storyteller.persistence.converter

import androidx.room.TypeConverter
import java.time.Instant

class InstantConverter {

    @TypeConverter
    fun fromTimestamp(value: Long): Instant = Instant.ofEpochMilli(value)

    @TypeConverter
    fun dateToTimestamp(instant: Instant): Long = instant.toEpochMilli()

}