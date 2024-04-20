package com.ifs21031.lostfound.data.remote.retrofit

import com.ifs21031.lostfound.data.remote.response.DelcomAddLoFoResponse
import com.ifs21031.lostfound.data.remote.response.DelcomLoFoResponse
import com.ifs21031.lostfound.data.remote.response.DelcomLoFosResponse
import com.ifs21031.lostfound.data.remote.response.DelcomLoginResponse
import com.ifs21031.lostfound.data.remote.response.DelcomResponse
import com.ifs21031.lostfound.data.remote.response.DelcomUserResponse

import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface IApiService {
    /**
     * Authentication Register
     */
    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): DelcomResponse

    /**
     * Authentication Login
     */
    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): DelcomLoginResponse

    /**
     * Get User Information
     */
    @GET("users/me")
    suspend fun getMe(): DelcomUserResponse

    /**
     * Add Lost & Found
     */
    @FormUrlEncoded
    @POST("lost-founds")
    suspend fun postLofo(
        @Field("title") title: String,
        @Field("description") description: String,
    ): DelcomAddLoFoResponse

    /**
     * Edit Lost & Found
     */
    @FormUrlEncoded
    @PUT("lost-founds/{id}")
    suspend fun putLofo(
        @Path("id") lofoId: Int,
        @Field("title") title: String,
        @Field("description") description: String,
        @Field("is_finished") isFinished: Int,
    ): DelcomResponse

    /**
     * Get All Lost & Found
     */
    @GET("lost-founds")
    suspend fun getLofos(
        @Query("is_finished") isFinished: Int?,
        @Query("is_me") isMe: Int?,
        @Query("status") lofoStatus: String?,
    ): DelcomLoFosResponse

    /**
     * Get Lost & Found by ID
     */
    @GET("lost-founds/{id}")
    suspend fun getLofo(
        @Path("id") lofoId: Int,
    ): DelcomLoFoResponse

    /**
     * Delete Lost & Found
     */
    @DELETE("lost-founds/{id}")
    suspend fun deleteLofo(
        @Path("id") lofoId: Int,
    ): DelcomResponse
}