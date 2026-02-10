@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.example.kinomaptest


import com.example.kinomaptest.data.local.dao.BadgeDAO
import com.example.kinomaptest.data.mapper.BadgeMapper
import com.example.kinomaptest.data.mapper.FakeBadgeMapper
import com.example.kinomaptest.data.remote.api.FakeBadgeApiService
import com.example.kinomaptest.data.remote.dto.BadgeApiResponse
import com.example.kinomaptest.data.remote.dto.BadgeCategoryDTO
import com.example.kinomaptest.data.remote.dto.FakeBadgeDTO
import com.example.kinomaptest.data.remote.dto.ImageUrlDTO
import com.example.kinomaptest.data.repository.BadgeRepositoryImpl
import com.example.kinomaptest.data.local.entity.BadgeDTO
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class BadgeRepositoryImplTest {

    private val dao: BadgeDAO = mockk(relaxed = true)
    private val api: FakeBadgeApiService = mockk()
    private val mapper: BadgeMapper = mockk(relaxed = true)
    private val apiMapper: FakeBadgeMapper = mockk()

    private lateinit var repo: BadgeRepositoryImpl

    @Before
    fun setup() {
        repo = BadgeRepositoryImpl(
            badgeDAO = dao,
            api = api,
            mapper = mapper,
            apiMapper = apiMapper
        )
    }

    @Test
    fun `seedIfEmpty does nothing when db is not empty`() = runTest {
        coEvery { dao.getAll() } returns listOf(mockk())

        val result = repo.seedIfEmpty()

        assertTrue(result.isSuccess)
        coVerify(exactly = 0) { api.getBadges() }
        coVerify(exactly = 0) { dao.insertAll(any<List<BadgeDTO>>()) }
    }

    @Test
    fun `seedIfEmpty fetches api and inserts when db is empty`() = runTest {
        coEvery { dao.getAll() } returns emptyList()

        val apiBadge = FakeBadgeDTO(
            id = 1,
            name = "Badge",
            description = "Desc",
            action = "action",
            category = "Cat",
            unlockedDate = null,
            unlockedPercent = 10,
            imagesUrl = ImageUrlDTO(unlocked = "u", locked = "l")
        )
        val categoryDto = BadgeCategoryDTO(name = "Cat", badges = listOf(apiBadge))
        coEvery { api.getBadges() } returns BadgeApiResponse(data = listOf(categoryDto))

        val entity = mockk<BadgeDTO>()
        every { apiMapper.fromApiToDto(apiBadge) } returns entity

        coEvery { dao.insertAll(any<List<BadgeDTO>>()) } returns listOf(1L)

        val result = repo.seedIfEmpty()

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { api.getBadges() }
        coVerify(exactly = 1) { dao.insertAll(match { it.size == 1 }) }
    }

    @Test
    fun `seedIfEmpty returns failure when api throws`() = runTest {
        coEvery { dao.getAll() } returns emptyList()
        coEvery { api.getBadges() } throws RuntimeException("network")

        val result = repo.seedIfEmpty()

        assertTrue(result.isFailure)
        coVerify(exactly = 0) { dao.insertAll(any<List<BadgeDTO>>()) }
    }
}
