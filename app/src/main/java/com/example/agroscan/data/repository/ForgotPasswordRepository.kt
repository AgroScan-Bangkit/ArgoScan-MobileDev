package com.example.agroscan.data.repository

import com.example.agroscan.data.remote.request.ForgotPasswordRequest
import com.example.agroscan.data.remote.respone.ForgotPasswordResponse
import com.example.agroscan.data.remote.retrofit.AuthApiService

class ForgotPasswordRepository(private val authApiService: AuthApiService) {
    suspend fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest): ForgotPasswordResponse {
        return authApiService.forgotPassword(forgotPasswordRequest)
    }
}