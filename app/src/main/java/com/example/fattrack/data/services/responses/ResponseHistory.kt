package com.example.fattrack.data.services.responses

import com.google.gson.annotations.SerializedName

data class ResponseHistory(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: HistoryData? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class JsonMember20241201Item(

	@field:SerializedName("food_name")
	val foodName: String? = null,

	@field:SerializedName("prediction_date")
	val predictionDate: String? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("calories")
	val calories: Int? = null
)

data class HistoryData(

	@field:SerializedName("2024-11-30")
	val jsonMember20241130: List<JsonMember20241130Item?>? = null,

	@field:SerializedName("2024-12-01")
	val jsonMember20241201: List<JsonMember20241201Item?>? = null
)

data class JsonMember20241130Item(

	@field:SerializedName("food_name")
	val foodName: String? = null,

	@field:SerializedName("prediction_date")
	val predictionDate: String? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("calories")
	val calories: Int? = null
)
