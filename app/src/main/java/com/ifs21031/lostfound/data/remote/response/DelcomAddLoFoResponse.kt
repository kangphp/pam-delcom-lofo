package com.ifs21031.lostfound.data.remote.response

import com.google.gson.annotations.SerializedName

data class DelcomAddLoFoResponse(

	@field:SerializedName("data")
	val data: DataAddLoFoResponse,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataAddLoFoResponse(

	@field:SerializedName("lost_found_id")
	val lostFoundId: Int = 0
)
