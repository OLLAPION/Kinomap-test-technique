package com.example.kinomaptest.domain.usecase

import com.example.kinomaptest.domain.repository.BadgeRepository
import javax.inject.Inject

class SeedBadgesIfEmptyUseCase @Inject constructor(
    private val repository: BadgeRepository
) {
    suspend operator fun invoke(): Result<Unit> = repository.seedIfEmpty()
}
