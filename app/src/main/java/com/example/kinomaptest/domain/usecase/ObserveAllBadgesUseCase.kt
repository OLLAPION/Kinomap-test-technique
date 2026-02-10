package com.example.kinomaptest.domain.usecase

import com.example.kinomaptest.domain.model.Badge
import com.example.kinomaptest.domain.repository.BadgeRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveAllBadgesUseCase @Inject constructor(
    private val repository: BadgeRepository
) {
    operator fun invoke(): Flow<List<Badge>> = repository.observeAllBadges()
}
