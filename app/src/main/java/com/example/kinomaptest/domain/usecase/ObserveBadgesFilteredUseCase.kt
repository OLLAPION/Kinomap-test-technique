package com.example.kinomaptest.domain.usecase

import com.example.kinomaptest.domain.model.Badge
import com.example.kinomaptest.domain.repository.BadgeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveBadgesFilteredUseCase @Inject constructor(
    private val repository: BadgeRepository
) {
    operator fun invoke(selectedCategories: Set<String>): Flow<List<Badge>> {
        return if (selectedCategories.isEmpty()) {
            repository.observeAllBadges()
        } else {
            repository.observeBadgesByCategories(selectedCategories.toList())
        }
    }
}


