package com.example.kinomaptest.domain.usecase

import com.example.kinomaptest.domain.repository.BadgeRepository
import javax.inject.Inject

class ObserveCategoriesUseCase @Inject constructor(
    private val repo: BadgeRepository
) {
    operator fun invoke() = repo.observeCategories()
}