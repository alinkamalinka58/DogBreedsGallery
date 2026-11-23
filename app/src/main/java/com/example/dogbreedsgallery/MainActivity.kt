package com.example.dogbreedsgallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.dogbreedsgallery.ui.screens.*
import com.example.dogbreedsgallery.ui.theme.DogBreedsGalleryTheme
import com.example.dogbreedsgallery.ui.viewmodel.BreedListViewModel
import com.example.dogbreedsgallery.ui.viewmodel.FavoriteViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppDependencies.init(applicationContext)
        setContent {
            DogBreedsGalleryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(BreedListScreen)
    val repository = remember { AppDependencies.getRepository() }

    val listViewModel: BreedListViewModel = viewModel {
        BreedListViewModel(repository)
    }
    val favoriteViewModel: FavoriteViewModel = viewModel {
        FavoriteViewModel(repository)
    }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<BreedListScreen> {
                BreedListScreen(
                    viewModel = listViewModel,
                    onBreedClick = { id -> backStack.add(BreedDetailScreen(id)) },
                    onFavoritesClick = { backStack.add(FavoriteScreen) }
                )
            }
            entry<BreedDetailScreen> { key ->
                BreedDetailScreen(
                    breedId = key.breedId,
                    repository = repository,
                    onBack = { backStack.removeLastOrNull() }
                )
            }
            entry<FavoriteScreen> {
                FavoriteScreen(
                    viewModel = favoriteViewModel,
                    onBreedClick = { id -> backStack.add(BreedDetailScreen(id)) },
                    onBack = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}