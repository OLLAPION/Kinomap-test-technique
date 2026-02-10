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



/*
    /** INSERT **/

    @WorkerThread
    suspend fun insert(badge: Badge): Long {
        return badgeDAO.insert(mapper.toDto(badge))
    }

    @WorkerThread
    suspend fun insertAll(badges: List<Badge>): List<Long> {
        return badgeDAO.insert(badges.map { mapper.toDto(it) })
    }

    @WorkerThread
    suspend fun insertAsResult(badge: Badge): Result<Long> {
        return try {
            Result.success(insert(badge))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @WorkerThread
    suspend fun insertAllAsResult(badges: List<Badge>): Result<List<Long>> {
        return try {
            Result.success(insertAll(badges))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** UPDATE **/

    @WorkerThread
    suspend fun update(badge: Badge): Int {
        return badgeDAO.update(mapper.toDto(badge))
    }

    @WorkerThread
    suspend fun updateAll(badges: List<Badge>): Int {
        return badgeDAO.update(badges.map { mapper.toDto(it)})
    }

    @WorkerThread
    suspend fun updateAsResult(badge: Badge): Result<Int> {
        return try {
            Result.success(badgeDAO.update(mapper.toDto(badge)))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @WorkerThread
    suspend fun updateAllAsResult(badges: List<Badge>): Result<Int> {
        return try {
            Result.success(badgeDAO.update(badges.map { mapper.toDto(it) }))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** DELETE **/

    @WorkerThread
    suspend fun deleteAll() {
        badgeDAO.deleteAll()
    }

    @WorkerThread
    suspend fun deleteById(id: Int) {
        badgeDAO.deleteById(id)
    }

    @WorkerThread
    suspend fun delete(badge: Badge) {
        badgeDAO.delete(mapper.toDto(badge))
    }

    @WorkerThread
    suspend fun delete(badges: List<Badge>) {
        badgeDAO.delete(badges.map { mapper.toDto(it) })
    }

    @WorkerThread
    suspend fun deleteAllAsResult(): Result<Unit> {
        return try {
            badgeDAO.deleteAll()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @WorkerThread
    suspend fun deleteByIdAsResult(id: Int): Result<Unit> {
        return try {
            badgeDAO.deleteById(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @WorkerThread
    suspend fun deleteAsResult(badge: Badge): Result<Unit> {
        return try {
            badgeDAO.delete(mapper.toDto(badge))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @WorkerThread
    suspend fun deleteAsResult(badges: List<Badge>): Result<Unit> {
        return try {
            badgeDAO.delete(badges.map { mapper.toDto(it) })
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** SELECT REAL TIME / FLOW **/

    fun getBadgeByIdFlow(id: Int): Flow<Badge?> =
        badgeDAO.getByIdRT(id).map { dto ->
            dto?.let { mapper.toDomainModel(it) }
        }

    fun getAllBadgesFlow(): Flow<List<Badge>> =
        badgeDAO.getAllRT().map { list -> list.map { mapper.toDomainModel(it) } }

    /** SELECT SUSPENDED **/

    @WorkerThread
    suspend fun getBadgeById(id: Int): Badge? =
        badgeDAO.getById(id)?.let { mapper.toDomainModel(it) }

    @WorkerThread
    suspend fun getAllBadges(): List<Badge> =
        badgeDAO.getAll().map { mapper.toDomainModel(it) }

    /** SEARCH BY CATEGORY **/

    fun getBadgesByCategoriesFlow(categories: List<String>): Flow<List<Badge>> {
        if (categories.isEmpty()) {
            return flowOf(emptyList())
        }
        return badgeDAO.getBadgesByCategoriesRT(categories)
            .map { list -> list.map { mapper.toDomainModel(it) } }
    }
    @WorkerThread
    suspend fun getBadgesByCategories(categories: List<String>): List<Badge> {
        if (categories.isEmpty()) return emptyList()
        return badgeDAO.getBadgesByCategories(categories)
            .map { mapper.toDomainModel(it) }
    }
    */

}