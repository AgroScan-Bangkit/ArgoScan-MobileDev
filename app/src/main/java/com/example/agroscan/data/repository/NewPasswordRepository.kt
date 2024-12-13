package com.example.agroscan.data.repository

import com.example.agroscan.data.remote.request.ResetPasswordRequest
import com.example.agroscan.data.remote.respone.ResetPasswordResponse
import com.example.agroscan.data.remote.retrofit.AuthApiService

class NewPasswordRepository(private val authApiService: AuthApiService) {

    suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest, tempToken: String): ResetPasswordResponse {
        return authApiService.resetPassword(tempToken, resetPasswordRequest)
    }
}