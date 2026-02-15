package com.example.kinomaptest.ui.screen.detailsbadge

import com.example.kinomaptest.domain.model.Badge

sealed class BadgeDetailsUIState {

    data class IsLoading(
        val status: Boolean = true
    ) : BadgeDetailsUIState()

    data class Error(
        val sError: String?
    ) : BadgeDetailsUIState()

    data class Success(
        val badge: Badge
    ) : BadgeDetailsUIState()
}