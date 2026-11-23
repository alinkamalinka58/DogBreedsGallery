package com.example.dogbreedsgallery.data.remote.dto

import com.squareup.moshi.Json

data class BreedDto(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "temperament") val temperament: String?,
    @Json(name = "origin") val origin: String?,
    @Json(name = "life_span") val lifeSpan: String?,
    @Json(name = "description") val description: String?,
    @Json(name = "image") val image: ImageDto?
)

data class ImageDto(
    @Json(name = "url") val url: String
)