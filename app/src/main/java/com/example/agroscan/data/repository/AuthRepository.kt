package com.example.agroscan.data.repository

import com.example.agroscan.data.remote.respone.LoginResponse
import com.example.agroscan.data.remote.respone.RegisterResponse
import com.example.agroscan.data.remote.request.LoginRequest
import com.example.agroscan.data.remote.request.RegisterRequest
import com.example.agroscan.data.remote.retrofit.AuthApiService

class AuthRepository(private val authApiService: AuthApiService) {

    // Register
    suspend fun register(registerRequest: RegisterRequest): RegisterResponse {
        return authApiService.register(registerRequest)
    }

    // Login
    suspend fun login(loginRequest: LoginRequest): LoginResponse {
        return authApiService.login(loginRequest)
    }
}