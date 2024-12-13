package com.example.agroscan.data.remote.respone

import com.google.gson.annotations.SerializedName

data class NewPasswordResponse(

	@field:SerializedName("message")
	val message: String? = null
)
