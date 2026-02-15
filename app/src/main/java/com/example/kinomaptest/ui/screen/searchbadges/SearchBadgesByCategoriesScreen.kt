package com.example.kinomaptest.ui.screen.searchbadges

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun SearchBadgesByCategoriesScreen(
    navController: NavController,
    viewModel: SearchBadgesByCategoriesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Filtrer par catégories", style = MaterialTheme.typography.titleMedium)
                    TextButton(onClick = viewModel::clear, enabled = uiState.selected.isNotEmpty()) {
                        Text("Reset")
                    }
                }

                Spacer(Modifier.height(8.dp))

                if (uiState.categories.isEmpty()) {
                    Text("Aucune catégorie")
                } else {
                    uiState.categories.forEach { cat ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.toggle(cat) }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = uiState.selected.contains(cat),
                                onCheckedChange = { viewModel.toggle(cat) }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(cat)
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Optionnel (minimal): button to go back to the list
                Button(onClick = { navController.navigate("all_badges") }) {
                    Text("Voir les badges")
                }
            }
        }
    }
}
