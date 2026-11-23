package com.example.dogbreedsgallery.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dogbreedsgallery.data.local.entity.BreedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BreedDao {
    @Query("SELECT * FROM breeds ORDER BY name ASC")
    fun getAllBreeds(): Flow<List<BreedEntity>>

    @Query("SELECT * FROM breeds WHERE isFavorite = 1 ORDER BY name ASC")
    fun getFavoriteBreeds(): Flow<List<BreedEntity>>

    @Query("SELECT * FROM breeds WHERE id = :id")
    suspend fun getBreedById(id: Int): BreedEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBreeds(breeds: List<BreedEntity>)

    @Query("UPDATE breeds SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Int, isFavorite: Boolean)

    @Query("SELECT COUNT(*) FROM breeds")
    suspend fun getCount(): Int
}