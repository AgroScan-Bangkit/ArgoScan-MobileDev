package com.example.agroscan.data.remote.retrofit

import android.util.Log
import com.example.agroscan.BuildConfig
import com.example.agroscan.data.local.response.UserPreferences
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiConfig(private val userPreferences: UserPreferences? = null) {
    private val BASE_URL_AUTH = BuildConfig.BASE_URL_AUTH
    private val BASE_URL_MAIN = BuildConfig.BASE_URL_MAIN

    fun getAuthApiService(): AuthApiService {
        // Logging untuk request dan response
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_AUTH)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(AuthApiService::class.java)
    }


    suspend fun getMainApiService(): MainApiService {
        val token = userPreferences?.getToken()
        Log.d("ApiConfig", "Token for Interceptor: $token")
        // Logging untuk request dan response
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val requestBuilder = req.newBuilder()
            if (!token.isNullOrEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
//                .addHeader("Authorization", "Bearer $token")
//                .addHeader("Accept", "*/*")
//                .addHeader("Connection", "keep-alive")
//                .addHeader("Cache-Control", "no-cache")
            chain.proceed(requestBuilder.build())
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .retryOnConnectionFailure(false)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_MAIN)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(MainApiService::class.java)
    }
}