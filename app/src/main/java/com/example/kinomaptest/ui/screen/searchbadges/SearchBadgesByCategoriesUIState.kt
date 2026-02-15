package com.example.kinomaptest.ui.screen.searchbadges

data class SearchBadgesByCategoriesUIState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val categories: List<String> = emptyList(),
    val selected: Set<String> = emptySet()
)