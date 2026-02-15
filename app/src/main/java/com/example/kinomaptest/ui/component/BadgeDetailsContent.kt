@file:OptIn(com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi::class)

package com.example.kinomaptest.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.GlideImage
import com.example.kinomaptest.domain.model.Badge
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun BadgeDetailsContent(
    badge: Badge,
    modifier: Modifier = Modifier
) {
    val unlocked = badge.unlockedDate != null
    val imageUrl = if (unlocked) badge.unlockedImageUrl else badge.lockedImageUrl

    val dateText = remember(badge.unlockedDate) {
        badge.unlockedDate?.let { instant ->
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            formatter.format(instant.atZone(ZoneId.systemDefault()))
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        GlideImage(
            model = imageUrl,
            contentDescription = badge.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Fit,
            colorFilter = if (unlocked) null else ColorFilter.tint(Color.Gray)
        )

        Spacer(Modifier.height(12.dp))

        Text(badge.name, style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))

        Text("Catégorie: ${badge.category}", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(8.dp))

        Text(
            if (unlocked) "Statut: Déverrouillé ✅" else "Statut: Verrouillé 🔒",
            style = MaterialTheme.typography.titleMedium
        )

        badge.unlockedPercent?.let {
            Spacer(Modifier.height(6.dp))
            Text("Progression: $it%", style = MaterialTheme.typography.bodyMedium)
        }

        dateText?.let {
            Spacer(Modifier.height(6.dp))
            Text("Déverrouillé le: $it", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(Modifier.height(16.dp))

        Text("Description", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(6.dp))
        Text(badge.description, style = MaterialTheme.typography.bodyLarge)
    }
}