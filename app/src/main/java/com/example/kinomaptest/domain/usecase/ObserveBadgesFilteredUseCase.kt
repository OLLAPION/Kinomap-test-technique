package com.example.kinomaptest.domain.usecase

import com.example.kinomaptest.domain.model.Badge
import com.example.kinomaptest.domain.repository.BadgeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveBadgesFilteredUseCase @Inject constructor(
    private val repo: BadgeRepository
) {
    operator fun invoke(selectedCategories: Set<String>): Flow<List<Badge>> {
        return if (selectedCategories.isEmpty()) {
            repo.observeAllBadges()
        } else {
            repo.observeBadgesByCategories(selectedCategories.toList())
        }
    }
}


