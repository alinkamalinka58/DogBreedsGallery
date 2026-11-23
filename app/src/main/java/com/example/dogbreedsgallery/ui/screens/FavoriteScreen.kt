package com.example.dogbreedsgallery.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.dogbreedsgallery.ui.viewmodel.FavoriteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    viewModel: FavoriteViewModel,
    onBreedClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favorites") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Back") }
                }
            )
        }
    ) { padding ->
        if (favorites.isEmpty()) {
            Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No favorites yet")
            }
        } else {
            LazyColumn(Modifier.padding(padding).fillMaxSize()) {
                items(favorites, key = { it.id }) { breed ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                            .clickable { onBreedClick(breed.id) }
                    ) {
                        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = breed.imageUrl,
                                contentDescription = breed.name,
                                modifier = Modifier.size(60.dp),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(breed.name, style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
        }
    }
}