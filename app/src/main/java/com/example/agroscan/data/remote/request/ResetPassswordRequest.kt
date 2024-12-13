package com.example.agroscan.data.remote.request

import com.google.gson.annotations.SerializedName

data class ResetPasswordRequest(

    @SerializedName("tempToken")
    val tempToken: String,

    @SerializedName("password")
    val password: String
)