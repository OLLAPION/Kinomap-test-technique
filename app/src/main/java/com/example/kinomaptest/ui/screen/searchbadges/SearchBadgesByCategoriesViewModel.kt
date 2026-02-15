package com.example.kinomaptest.ui.screen.searchbadges

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinomaptest.domain.usecase.ObserveCategoriesUseCase
import com.example.kinomaptest.ui.filter.BadgeCategoryFilterStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SearchBadgesByCategoriesViewModel @Inject constructor(
    observeCategoriesUseCase: ObserveCategoriesUseCase,
    private val filterStore: BadgeCategoryFilterStore
) : ViewModel() {

    val uiState: StateFlow<SearchBadgesByCategoriesUIState> =
        combine(
            observeCategoriesUseCase()
                .map { Result.success(it) }
                .catch { emit(Result.failure(it)) },
            filterStore.selected
        ) { categoriesResult, selected ->
            categoriesResult.fold(
                onSuccess = { cats ->
                    SearchBadgesByCategoriesUIState(
                        isLoading = false,
                        error = null,
                        categories = cats,
                        selected = selected
                    )
                },
                onFailure = { e ->
                    SearchBadgesByCategoriesUIState(
                        isLoading = false,
                        error = e.message ?: "Erreur inconnue",
                        categories = emptyList(),
                        selected = selected
                    )
                }
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            SearchBadgesByCategoriesUIState(isLoading = true)
        )

    fun toggle(category: String) = filterStore.toggle(category)
    fun clear() = filterStore.clear()
}
