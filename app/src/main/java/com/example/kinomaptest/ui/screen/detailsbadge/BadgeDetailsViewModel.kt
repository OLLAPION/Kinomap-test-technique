package com.example.kinomaptest.ui.screen.detailsbadge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinomaptest.domain.usecase.ObserveBadgeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class BadgeDetailsViewModel @Inject constructor(
    private val observeBadgeUseCase: ObserveBadgeUseCase
) : ViewModel() {

    fun uiState(badgeId: Int): StateFlow<BadgeDetailsUIState> {
        return observeBadgeUseCase(badgeId)
            .map { badge ->
                if (badge == null) {
                    BadgeDetailsUIState.Error("Badge introuvable (id=$badgeId)")
                } else {
                    BadgeDetailsUIState.Success(badge)
                }
            }
            .catch { e ->
                emit(BadgeDetailsUIState.Error(e.message))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = BadgeDetailsUIState.IsLoading()
            )
    }
}
