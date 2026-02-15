package com.example.kinomaptest.ui.filter

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BadgeCategoryFilterStore @Inject constructor() {

    private val _selected = MutableStateFlow<Set<String>>(emptySet())
    val selected: StateFlow<Set<String>> = _selected.asStateFlow()

    fun toggle(category: String) {
        val cur = _selected.value.toMutableSet()
        if (cur.contains(category)) cur.remove(category) else cur.add(category)
        _selected.value = cur
    }

    fun clear() {
        _selected.value = emptySet()
    }
}
