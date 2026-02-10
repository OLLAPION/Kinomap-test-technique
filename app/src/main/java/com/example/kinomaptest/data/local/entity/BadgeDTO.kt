package com.example.kinomaptest.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "badge")
data class BadgeDTO(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "unlocked_date")
    val unlockedDate: Instant?,

    @ColumnInfo(name = "unlocked_percent")
    val unlockedPercent: Int?,

    @ColumnInfo(name = "unlocked_image_url")
    val unlockedImageUrl: String,

    @ColumnInfo(name = "locked_image_url")
    val lockedImageUrl: String
)

