package com.example.agroscan.data.remote.request

import com.google.gson.annotations.SerializedName

class ForgotPasswordRequest(

    @SerializedName("email")
    val email: String
)