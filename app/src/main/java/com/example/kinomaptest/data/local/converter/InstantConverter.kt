package com.example.kinomaptest.data.local.converter

import androidx.room.TypeConverter
import java.time.Instant

class InstantConverter {

    @TypeConverter
    fun fromLong(value: Long?): Instant? {
        return value?.let { Instant.ofEpochSecond(it) }
    }

    @TypeConverter
    fun toLong(instant: Instant?): Long? {
        return instant?.epochSecond
    }

}