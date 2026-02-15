package com.example.kinomaptest.data.repository

import androidx.annotation.WorkerThread
import com.example.kinomaptest.BuildConfig
import com.example.kinomaptest.data.local.dao.BadgeDAO
import com.example.kinomaptest.data.mapper.BadgeMapper
import com.example.kinomaptest.data.mapper.FakeBadgeMapper
import com.example.kinomaptest.data.remote.api.FakeBadgeApiService
import com.example.kinomaptest.domain.model.Badge
import com.example.kinomaptest.domain.repository.BadgeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class BadgeRepositoryImpl @Inject constructor(
    private val badgeDAO: BadgeDAO,
    private val api: FakeBadgeApiService,
    private val mapper: BadgeMapper,
    private val apiMapper: FakeBadgeMapper
): BadgeRepository {

    override fun observeAllBadges(): Flow<List<Badge>> =
        badgeDAO.getAllRT().map { list -> list.map(mapper::toDomainModel) }

    override fun observeBadge(id: Int): Flow<Badge?> =
        badgeDAO.getByIdRT(id).map { it?.let(mapper::toDomainModel) }

    override fun observeBadgesByCategories(categories: List<String>): Flow<List<Badge>> =
        badgeDAO.getBadgesByCategoriesRT(categories)
            .map { list -> list.map(mapper::toDomainModel) }

    override fun observeCategories(): Flow<List<String>> =
        badgeDAO.observeCategories()

    @WorkerThread
    private suspend fun seedIfEmptyInternal(): Unit {
        val existing = badgeDAO.getAll()
        if (existing.isNotEmpty()) return

        val categories = api.getBadges(BuildConfig.KINOMAP_APP_TOKEN).data
        val entities = categories
            .flatMap { it.badges }
            .map(apiMapper::fromApiToDto)

        badgeDAO.insertAll(entities)
    }

    override suspend fun seedIfEmpty(): Result<Unit> {
        return try {
            seedIfEmptyInternal()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}