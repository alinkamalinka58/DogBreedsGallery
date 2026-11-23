package com.example.dogbreedsgallery

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object BreedListScreen : NavKey

@Serializable
data class BreedDetailScreen(val breedId: Int) : NavKey

@Serializable
data object FavoriteScreen : NavKey