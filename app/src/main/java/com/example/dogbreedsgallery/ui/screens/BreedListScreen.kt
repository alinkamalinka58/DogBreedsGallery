package com.example.dogbreedsgallery.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.dogbreedsgallery.data.local.entity.BreedEntity
import com.example.dogbreedsgallery.ui.viewmodel.BreedListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedListScreen(
    viewModel: BreedListViewModel,
    onBreedClick: (Int) -> Unit,
    onFavoritesClick: () -> Unit
) {
    val breeds by viewModel.breeds.collectAsStateWithLifecycle()
    val query by viewModel.searchQuery.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dog Breeds") },
                actions = {
                    // Добавляем кнопку обновления
                    IconButton(onClick = { viewModel.refresh() }) {
                        Text("🔄", fontSize = 20.sp)
                    }
                    TextButton(onClick = onFavoritesClick) {
                        Text("Favorites")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            OutlinedTextField(
                value = query,
                onValueChange = viewModel::onSearchQueryChange,
                label = { Text("Search breed...") },
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                singleLine = true
            )
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = viewModel::refresh,
                modifier = Modifier.fillMaxSize()
            ) {
                if (breeds.isEmpty() && !isRefreshing) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No breeds found")
                    }
                } else {
                    LazyColumn(Modifier.fillMaxSize()) {
                        items(breeds, key = { it.id }) { breed ->
                            BreedCard(
                                breed = breed,
                                onClick = { onBreedClick(breed.id) },
                                onFavoriteToggle = { viewModel.toggleFavorite(breed.id, breed.isFavorite) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BreedCard(
    breed: BreedEntity,
    onClick: () -> Unit,
    onFavoriteToggle: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp).clickable(onClick = onClick)
    ) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = breed.imageUrl,
                contentDescription = breed.name,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop,
                placeholder = null
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(breed.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    breed.temperament.take(40) + if (breed.temperament.length > 40) "..." else "",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = onFavoriteToggle) {
                Text(if (breed.isFavorite) "❤️" else "🤍")
            }
        }
    }
}