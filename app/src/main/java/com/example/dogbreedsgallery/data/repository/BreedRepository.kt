package com.example.dogbreedsgallery.data.repository

import com.example.dogbreedsgallery.data.local.dao.BreedDao
import com.example.dogbreedsgallery.data.local.entity.BreedEntity
import com.example.dogbreedsgallery.data.remote.DogApi
import kotlinx.coroutines.flow.Flow

class BreedRepository(
    private val dao: BreedDao,
    private val api: DogApi
) {
    fun getAllBreeds(): Flow<List<BreedEntity>> = dao.getAllBreeds()

    fun getFavoriteBreeds(): Flow<List<BreedEntity>> = dao.getFavoriteBreeds()

    suspend fun getBreedById(id: Int): BreedEntity? = dao.getBreedById(id)

    suspend fun refreshBreeds() {
        try {
            val remoteBreeds = api.getBreeds()
            val entities = remoteBreeds.map { dto ->
                BreedEntity(
                    id = dto.id,
                    name = dto.name,
                    temperament = dto.temperament ?: "",
                    origin = dto.origin,
                    lifeSpan = dto.lifeSpan ?: "",
                    description = dto.description,
                    imageUrl = dto.image?.url ?: "",
                    isFavorite = false
                )
            }
            dao.insertBreeds(entities)
        } catch (e: Exception) {
            android.util.Log.e("API_ERROR", "Ошибка при загрузке пород", e)
            e.printStackTrace()
        }
    }

    suspend fun toggleFavorite(id: Int, isFavorite: Boolean) {
        dao.updateFavorite(id, isFavorite)
    }
}