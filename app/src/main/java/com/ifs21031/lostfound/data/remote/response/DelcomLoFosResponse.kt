package com.ifs21031.lostfound.data.remote.response

import com.google.gson.annotations.SerializedName

data class DelcomLoFosResponse(

	@field:SerializedName("data")
	val data: DataLofosResponse,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataLofosResponse(

	@field:SerializedName("lost_founds")
	val lostFounds: List<LostFoundsItem>
)

data class AuthorLofosResponse(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("photo")
	val photo: Any
)

data class LostFoundsItem(

	@field:SerializedName("cover")
	val cover: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("author")
	val author: AuthorLofosResponse,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("is_completed")
	var isCompleted: Int,

	@field:SerializedName("status")
	val status: String
)
