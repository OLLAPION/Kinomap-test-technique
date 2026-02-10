package com.example.kinomaptest.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.kinomaptest.data.local.dao.BadgeDAO
import com.example.kinomaptest.data.local.entity.BadgeDTO
import com.example.kinomaptest.data.local.converter.InstantConverter

@Database(
    entities = [BadgeDTO::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(InstantConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun badgeDAO(): BadgeDAO
}