package com.example.agroscan.data.repository

import com.example.agroscan.data.remote.request.VerifikasiEmailRequest
import com.example.agroscan.data.remote.respone.VerifikasiEmailResponse
import com.example.agroscan.data.remote.retrofit.AuthApiService

class VerifEmailRepository(private val authApiService: AuthApiService) {

    suspend fun verifyOTP(verifikasiEmailRequest: VerifikasiEmailRequest): VerifikasiEmailResponse {
        return authApiService.verifyOTP(verifikasiEmailRequest)
    }
}