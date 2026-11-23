package com.example.dogbreedsgallery.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.dogbreedsgallery.data.local.entity.BreedEntity
import com.example.dogbreedsgallery.data.repository.BreedRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedDetailScreen(
    breedId: Int,
    repository: BreedRepository,
    onBack: () -> Unit
) {
    var breed by remember { mutableStateOf<BreedEntity?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(breedId) {
        breed = repository.getBreedById(breedId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(breed?.name ?: "Loading...") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Back") }
                }
            )
        }
    ) { padding ->
        breed?.let { b ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                AsyncImage(
                    model = b.imageUrl,
                    contentDescription = b.name,
                    modifier = Modifier.fillMaxWidth().height(250.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(16.dp))
                Text(b.name, style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(8.dp))
                Text("Temperament: ${b.temperament}", style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(8.dp))
                Text("Life Span: ${b.lifeSpan}", style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(8.dp))
                Text("Origin: ${b.origin ?: "Unknown"}", style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(16.dp))
                Text(b.description ?: "No description", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = {
                        scope.launch {
                            repository.toggleFavorite(b.id, !b.isFavorite)
                            breed = repository.getBreedById(b.id)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (b.isFavorite) "Remove from Favorites" else "Add to Favorites")
                }
            }
        } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}