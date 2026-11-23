package com.example.dogbreedsgallery.data.remote

import com.example.dogbreedsgallery.data.remote.dto.BreedDto
import retrofit2.http.GET

interface DogApi {
    @GET("breeds")
    suspend fun getBreeds(): List<BreedDto>
}