package com.example.dogbreedsgallery.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogbreedsgallery.data.local.entity.BreedEntity
import com.example.dogbreedsgallery.data.repository.BreedRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: BreedRepository) : ViewModel() {
    val favorites: StateFlow<List<BreedEntity>> = repository.getFavoriteBreeds()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleFavorite(id: Int, current: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(id, !current)
        }
    }
}