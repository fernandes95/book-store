package com.example.bookstore.di

import com.example.bookstore.data.api.ApiService
import com.example.bookstore.data.api.VolumeApi
import com.example.bookstore.data.repositories.OfflineRepository
import com.example.bookstore.data.repositories.VolumesRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Singleton
    @Provides
    fun provideApi(): VolumeApi = ApiService.getClient()

    @Singleton
    @Provides
    fun provideAuth(): FirebaseAuth = Firebase.auth

    @Singleton
    @Provides
    fun provideFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    fun provideVolumesRepository() = VolumesRepository()

    @Provides
    fun provideOfflineRepository() = OfflineRepository()
}