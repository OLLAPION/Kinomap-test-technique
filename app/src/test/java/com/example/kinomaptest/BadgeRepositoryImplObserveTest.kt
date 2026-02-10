@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.example.kinomaptest

import app.cash.turbine.test
import com.example.kinomaptest.data.local.dao.BadgeDAO
import com.example.kinomaptest.data.local.entity.BadgeDTO
import com.example.kinomaptest.data.mapper.BadgeMapper
import com.example.kinomaptest.data.mapper.FakeBadgeMapper
import com.example.kinomaptest.data.remote.api.FakeBadgeApiService
import com.example.kinomaptest.data.repository.BadgeRepositoryImpl
import com.example.kinomaptest.domain.model.Badge
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.Instant

class BadgeRepositoryImplObserveTest {

    private val dao: BadgeDAO = mockk()
    private val api: FakeBadgeApiService = mockk(relaxed = true) // pas utilisé ici
    private val mapper: BadgeMapper = mockk()
    private val apiMapper: FakeBadgeMapper = mockk(relaxed = true) // pas utilisé ici

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
    fun `observeAllBadges emits mapped list from dao`() = runTest {
        val dto1 = BadgeDTO(
            id = 1,
            name = "A",
            description = "desc",
            category = "Travel",
            unlockedDate = Instant.ofEpochSecond(1736780630),
            unlockedPercent = null,
            unlockedImageUrl = "u",
            lockedImageUrl = "l"
        )
        val dto2 = dto1.copy(id = 2, name = "B", category = "Sport")

        val domain1 = mockk<Badge>()
        val domain2 = mockk<Badge>()

        every { dao.getAllRT() } returns flowOf(listOf(dto1, dto2))
        every { mapper.toDomainModel(dto1) } returns domain1
        every { mapper.toDomainModel(dto2) } returns domain2

        repo.observeAllBadges().test {
            val item = awaitItem()
            assertEquals(listOf(domain1, domain2), item)
            awaitComplete()
        }

        verify(exactly = 1) { dao.getAllRT() }
        verify(exactly = 1) { mapper.toDomainModel(dto1) }
        verify(exactly = 1) { mapper.toDomainModel(dto2) }
    }

    @Test
    fun `observeBadgesByCategories emits only Travel mapped`() = runTest {
        val travelDto = BadgeDTO(
            id = 15,
            name = "Trained in Oceania",
            description = "Trained on a Oceania based video",
            category = "Travel",
            unlockedDate = Instant.ofEpochSecond(1736780630),
            unlockedPercent = null,
            unlockedImageUrl = "https://static.kinomap.com/badges/activity_trained_OC-win.png",
            lockedImageUrl = "https://static.kinomap.com/badges/activity_trained_OC.png"
        )

        val travelDomain = mockk<Badge>()

        every { dao.getBadgesByCategoriesRT(listOf("Travel")) } returns flowOf(listOf(travelDto))
        every { mapper.toDomainModel(travelDto) } returns travelDomain

        repo.observeBadgesByCategories(listOf("Travel")).test {
            val item = awaitItem()
            assertEquals(listOf(travelDomain), item)
            awaitComplete()
        }

        verify(exactly = 1) { dao.getBadgesByCategoriesRT(listOf("Travel")) }
        verify(exactly = 1) { mapper.toDomainModel(travelDto) }
    }

    @Test
    fun `observeBadge emits mapped badge detail for id 15`() = runTest {
        val dto = BadgeDTO(
            id = 15,
            name = "Trained in Oceania",
            description = "Trained on a Oceania based video",
            category = "Travel",
            unlockedDate = Instant.ofEpochSecond(1736780630),
            unlockedPercent = null,
            unlockedImageUrl = "https://static.kinomap.com/badges/activity_trained_OC-win.png",
            lockedImageUrl = "https://static.kinomap.com/badges/activity_trained_OC.png"
        )
        val domain = mockk<Badge>()

        every { dao.getByIdRT(15) } returns flowOf(dto)
        every { mapper.toDomainModel(dto) } returns domain

        repo.observeBadge(15).test {
            val item = awaitItem()
            assertEquals(domain, item)
            awaitComplete()
        }

        verify(exactly = 1) { dao.getByIdRT(15) }
        verify(exactly = 1) { mapper.toDomainModel(dto) }
    }

    @Test
    fun `observeBadge emits null when dao returns null`() = runTest {
        every { dao.getByIdRT(999) } returns flowOf(null)

        repo.observeBadge(999).test {
            val item = awaitItem()
            assertNull(item)
            awaitComplete()
        }

        verify(exactly = 1) { dao.getByIdRT(999) }
        verify(exactly = 0) { mapper.toDomainModel(any()) }
    }
}
