package com.example.agroscan.data.remote.request

import com.google.gson.annotations.SerializedName

class VerifikasiEmailRequest(

    @SerializedName("resetCode")
    val resetCode: String
)