package com.example.kinomaptest.domain.model

import java.time.Instant

data class Badge(
    val id: Int,
    val name: String,
    val description: String,
    val category: String,
    val unlockedDate: Instant?,
    val unlockedPercent: Int?,
    val unlockedImageUrl: String,
    val lockedImageUrl: String
)

