package com.example.kinomaptest.domain.repository

import com.example.kinomaptest.domain.model.Badge
import kotlinx.coroutines.flow.Flow

interface BadgeRepository {
    fun observeAllBadges(): Flow<List<Badge>>
    fun observeBadge(id: Int): Flow<Badge?>
    fun observeBadgesByCategories(categories: List<String>): Flow<List<Badge>>
    fun observeCategories(): Flow<List<String>>
    suspend fun seedIfEmpty(): Result<Unit>
}