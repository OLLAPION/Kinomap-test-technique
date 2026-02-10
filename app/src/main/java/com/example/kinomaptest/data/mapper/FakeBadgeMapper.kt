package com.example.kinomaptest.data.mapper

import com.example.kinomaptest.data.local.entity.BadgeDTO
import com.example.kinomaptest.data.remote.dto.FakeBadgeDTO
import java.time.Instant

class FakeBadgeMapper {

    fun fromApiToDto(api: FakeBadgeDTO): BadgeDTO {
        return BadgeDTO(
            id = api.id,
            name = api.name,
            description = api.description,
            category = api.category,

            unlockedDate = api.unlockedDate?.let { Instant.ofEpochSecond(it) },

            unlockedPercent = api.unlockedPercent,
            unlockedImageUrl = api.imagesUrl.unlocked,
            lockedImageUrl = api.imagesUrl.locked
        )
    }
}
