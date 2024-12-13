package com.example.agroscan.data.remote.respone

import com.google.gson.annotations.SerializedName

data class VerifikasiEmailResponse(

	@field:SerializedName("tempToken")
	val tempToken: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
