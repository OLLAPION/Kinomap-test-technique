package com.example.kinomaptest.ui.screen.allbadges

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinomaptest.domain.usecase.ObserveBadgesFilteredUseCase
import com.example.kinomaptest.domain.usecase.ObserveCategoriesUseCase
import com.example.kinomaptest.domain.usecase.SeedBadgesIfEmptyUseCase
import com.example.kinomaptest.ui.filter.BadgeCategoryFilterStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class AllBadgesViewModel @Inject constructor(
    private val seedBadgesIfEmptyUseCase: SeedBadgesIfEmptyUseCase,
    private val observeBadgesFilteredUseCase: ObserveBadgesFilteredUseCase,
    private val observeCategoriesUseCase: ObserveCategoriesUseCase,
    private val filterStore: BadgeCategoryFilterStore
) : ViewModel() {

    private val selectedBadgeId = MutableStateFlow<Int?>(null)

    private val categoriesFlow = observeCategoriesUseCase()
        .catch { emit(emptyList()) }

    private val badgesFlow = filterStore.selected
        .flatMapLatest { observeBadgesFilteredUseCase(it) }
        .catch { emit(emptyList()) }

    val uiState: StateFlow<AllBadgesUIState> =
        combine(categoriesFlow, badgesFlow, filterStore.selected, selectedBadgeId) { categories, badges, selectedCats, selectedId ->
            AllBadgesUIState(
                isLoading = false,
                error = null,
                badges = badges,
                categories = categories,
                selectedCategories = selectedCats,
                selectedBadgeId = selectedId
            )
        }
            .onStart {
                emit(AllBadgesUIState(isLoading = true))
                val res = seedBadgesIfEmptyUseCase()
                if (res.isFailure) {
                    emit(AllBadgesUIState(isLoading = false, error = res.exceptionOrNull()?.message))
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AllBadgesUIState(isLoading = true))

    fun selectBadge(id: Int) {
        selectedBadgeId.value = id
    }
}