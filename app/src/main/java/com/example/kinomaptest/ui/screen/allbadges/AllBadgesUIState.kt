package com.example.kinomaptest.ui.screen.allbadges

import com.example.kinomaptest.domain.model.Badge

data class AllBadgesUIState(
    val isLoading: Boolean = true,
    val error: String? = null,

    val badges: List<Badge> = emptyList(),
    val categories: List<String> = emptyList(),

    val selectedCategories: Set<String> = emptySet(),

    // for tablet (right : détail)
    val selectedBadgeId: Int? = null
)
