@file:OptIn(ExperimentalGlideComposeApi::class)

package com.example.kinomaptest.ui.screen.allbadges

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kinomaptest.domain.model.Badge
import com.example.kinomaptest.ui.component.BadgeDetailsContent
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage


@Composable
fun AllBadgesScreen(
    navController: NavController,
    viewModel: AllBadgesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp > 600

    when {
        uiState.isLoading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        uiState.error != null -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Erreur: ${uiState.error}")
            }
        }

        else -> {
            if (isTablet) {
                // Master/Detail tablet : left list , right détail
                Row(Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .widthIn(min = 320.dp)
                            .weight(0.45f)
                            .padding(12.dp)
                    ) {
                        BadgesList(
                            badges = uiState.badges,
                            selectedBadgeId = uiState.selectedBadgeId,
                            onBadgeClick = { badge ->
                                viewModel.selectBadge(badge.id)
                            }
                        )
                    }

                    VerticalDivider(modifier = Modifier.fillMaxHeight())

                    val selectedBadge = uiState.badges.firstOrNull { it.id == uiState.selectedBadgeId }
                    BadgeDetailPane(
                        badge = selectedBadge,
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.55f)
                            .padding(12.dp)
                    )
                }
            } else {
                // phone : liste cliquable -> navigation
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {
                    BadgesList(
                        badges = uiState.badges,
                        selectedBadgeId = null,
                        onBadgeClick = { badge ->
                            navController.navigate("badge_details/${badge.id}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun BadgesList(
    badges: List<Badge>,
    selectedBadgeId: Int?,
    onBadgeClick: (Badge) -> Unit
) {
    Card(Modifier.fillMaxSize()) {
        if (badges.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Aucun badge")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(badges, key = { it.id }) { badge ->
                    BadgeRow(
                        badge = badge,
                        isSelected = (selectedBadgeId == badge.id),
                        onClick = { onBadgeClick(badge) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun BadgeRow(
    badge: Badge,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val unlocked = badge.unlockedDate != null
    val imageUrl = if (unlocked) badge.unlockedImageUrl else badge.lockedImageUrl

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.secondaryContainer
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Miniature
            GlideImage(
                model = imageUrl,
                contentDescription = badge.name,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
                // optionnel: grisé si locked
                colorFilter = if (unlocked) null else ColorFilter.tint(Color.Gray)
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(badge.name, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(2.dp))
                Text(badge.category, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(6.dp))
                Text(
                    badge.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}


@Composable
private fun BadgeDetailPane(
    badge: Badge?,
    modifier: Modifier = Modifier
) {
    Card(modifier.fillMaxSize()) {
        if (badge == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Sélectionne un badge à gauche")
            }
        } else {
            BadgeDetailsContent(badge = badge, modifier = Modifier.fillMaxSize())
        }
    }
}
