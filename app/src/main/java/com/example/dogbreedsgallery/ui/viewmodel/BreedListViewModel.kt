package com.example.dogbreedsgallery.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogbreedsgallery.data.local.entity.BreedEntity
import com.example.dogbreedsgallery.data.repository.BreedRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BreedListViewModel(private val repository: BreedRepository) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    val breeds: StateFlow<List<BreedEntity>> = combine(
        repository.getAllBreeds(),
        _searchQuery
    ) { breeds, query ->
        if (query.isBlank()) breeds
        else breeds.filter { it.name.contains(query, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            repository.refreshBreeds()
            _isRefreshing.value = false
        }
    }

    fun toggleFavorite(id: Int, current: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(id, !current)
        }
    }
}