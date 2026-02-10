package com.example.kinomaptest.data.mapper

import com.example.kinomaptest.data.local.entity.BadgeDTO
import com.example.kinomaptest.domain.model.Badge

class BadgeMapper {

    fun toDto(badge: Badge) : BadgeDTO {
        return BadgeDTO(
            id = badge.id,
            name = badge.name,
            description = badge.description,
            category = badge.category,
            unlockedDate = badge.unlockedDate,
            unlockedPercent = badge.unlockedPercent,
            unlockedImageUrl = badge.unlockedImageUrl,
            lockedImageUrl = badge.lockedImageUrl
        )
    }

    fun toDomainModel(badgeDTO: BadgeDTO): Badge {
        return Badge(
            id=badgeDTO.id,
            name = badgeDTO.name,
            description = badgeDTO.description,
            category = badgeDTO.category,
            unlockedDate = badgeDTO.unlockedDate,
            unlockedPercent = badgeDTO.unlockedPercent,
            unlockedImageUrl = badgeDTO.unlockedImageUrl,
            lockedImageUrl = badgeDTO.lockedImageUrl
        )
    }

}