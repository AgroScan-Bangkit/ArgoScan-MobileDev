package com.example.agroscan.data.remote.retrofit

import com.example.agroscan.data.remote.request.*
import com.example.agroscan.data.remote.respone.*
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthApiService {
    // Register
    @POST("register")
    suspend fun register(@Body registerRequest: RegisterRequest): RegisterResponse

    // Login
    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    // Forgot Password
    @POST("forgot-password")
    suspend fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordRequest): ForgotPasswordResponse

    // Verify OTP
    @POST("verify-reset-code")
    suspend fun verifyOTP(@Body verifikasiEmailRequest: VerifikasiEmailRequest): VerifikasiEmailResponse

    // Reset Password
    @POST("reset-password")
    suspend fun resetPassword(
        @Header("Authorization") tempToken: String,
        @Body resetPassswordRequest: ResetPasswordRequest
    ): ResetPasswordResponse
}


// Endpoint with model ML
interface MainApiService {
    // Scan Image
    @Multipart
    @POST("predict")
    suspend fun predictImage(
        @Part file: MultipartBody.Part
    ): PredictResponse
}