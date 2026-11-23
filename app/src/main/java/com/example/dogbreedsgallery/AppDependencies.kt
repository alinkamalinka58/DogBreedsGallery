package com.example.dogbreedsgallery

import android.content.Context
import androidx.room.Room
import com.example.dogbreedsgallery.data.local.AppDatabase
import com.example.dogbreedsgallery.data.remote.DogApi
import com.example.dogbreedsgallery.data.repository.BreedRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object AppDependencies {
    private var database: AppDatabase? = null
    private var api: DogApi? = null
    private var repository: BreedRepository? = null

    fun init(context: Context) {
        if (database == null) {
            database = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "dog_breeds_db"
            ).build()
        }
        if (api == null) {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                // Добавляем ключ в заголовок
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("x-api-key", "live_OJQvLReuLtyQTq29EClJtCGNmDBViIf6wIcydtHbtHtDSa6rwiGj2IIKEbva59Qv")
                        .build()
                    chain.proceed(request)
                }
                .build()
            api = Retrofit.Builder()
                .baseUrl("https://api.thedogapi.com/v1/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(client)
                .build()
                .create(DogApi::class.java)
        }
        if (repository == null) {
            repository = BreedRepository(database!!.breedDao(), api!!)
        }
    }

    fun getRepository(): BreedRepository = repository!!
}