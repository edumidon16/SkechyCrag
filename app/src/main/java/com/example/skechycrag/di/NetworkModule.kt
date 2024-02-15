package com.example.skechycrag.di

import com.example.skechycrag.ui.routedetail.RouteDetailViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule{

    @Singleton
    @Provides
    fun provideFirestone(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
}