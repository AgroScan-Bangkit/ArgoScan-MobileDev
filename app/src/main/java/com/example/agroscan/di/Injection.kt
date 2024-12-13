package com.example.agroscan.di

import android.content.Context
import com.example.agroscan.data.local.response.UserPreferences
import com.example.agroscan.data.remote.retrofit.ApiConfig
import com.example.agroscan.data.remote.retrofit.AuthApiService
import com.example.agroscan.data.repository.AuthRepository
import com.example.agroscan.ui.login.LoginViewModel
import com.example.agroscan.ui.register.RegisterViewModel
import com.example.agroscan.ui.register.RegisterViewModelFactory
import kotlinx.coroutines.runBlocking

object Injection {


    // Fungsi untuk menyediakan AuthRepository
    fun provideAuthRepository(context: Context): AuthRepository {
        val apiService = provideApiService(context)
        return AuthRepository(apiService)
    }

    // Fungsi untuk menyediakan RegisterViewModel dengan AuthRepository
    suspend fun provideRegisterViewModel(context: Context): RegisterViewModel {
        val authRepository = provideAuthRepository(context)
        return RegisterViewModel(authRepository)
    }

    // Factory untuk ViewModel
    suspend fun provideRegisterViewModelFactory(context: Context): RegisterViewModelFactory {
        val authRepository = provideAuthRepository(context)
        return RegisterViewModelFactory(authRepository)
    }

    // Login
    suspend fun provideLoginViewModel(context: Context): LoginViewModel {
        val authRepository = provideAuthRepository(context)
        val userPreferences = provideUserPreferences(context)
        return LoginViewModel(authRepository, userPreferences)
    }


    // User Preferences
    fun provideUserPreferences(context: Context): UserPreferences {
        return UserPreferences.getInstance(context)
    }

    // api instance
    private  fun provideApiService(context: Context): AuthApiService {
        val userPreferences = provideUserPreferences(context)
        val token = runBlocking { userPreferences.getToken() }// Ambil token dari DataStore
        return ApiConfig(userPreferences).getAuthApiService()

    }


}