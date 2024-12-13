package com.example.agroscan.data.remote.respone

import com.google.gson.annotations.SerializedName

data class PredictResponse(

	@field:SerializedName("recommended_action")
	val recommendedAction: String? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("prediction")
	val prediction: String? = null
)
