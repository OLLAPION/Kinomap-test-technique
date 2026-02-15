package com.example.kinomaptest.ui.screen.detailsbadge

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kinomaptest.ui.component.BadgeDetailsContent


@Composable
fun BadgeDetailsScreen(
    badgeId: Int,
    viewModel: BadgeDetailsViewModel = hiltViewModel()
) {
    val uiStateFlow = remember(badgeId) { viewModel.uiState(badgeId) }
    val uiState by uiStateFlow.collectAsState()

    when (val state = uiState) {
        is BadgeDetailsUIState.IsLoading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is BadgeDetailsUIState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Erreur: ${state.sError ?: "inconnue"}")
            }
        }

        is BadgeDetailsUIState.Success -> {
            BadgeDetailsContent(badge = state.badge)
        }
    }
}
