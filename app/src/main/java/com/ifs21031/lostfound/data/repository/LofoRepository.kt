package com.ifs21031.lostfound.data.repository

import com.google.gson.Gson
import com.ifs21031.lostfound.data.remote.MyResult
import com.ifs21031.lostfound.data.remote.response.DelcomResponse
import com.ifs21031.lostfound.data.remote.retrofit.IApiService
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class LofoRepository private constructor(
    private val apiService: IApiService,
) {

    /**
     * Add New Lost & Found
     */
    fun postLofo(
        title: String,
        description: String,
    ) = flow {
        emit(MyResult.Loading)

        try {
            emit(
                MyResult.Success(
                    apiService.postLofo(
                        title,
                        description
                    ).data
                )
            )
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            emit(
                MyResult.Error(
                    Gson().fromJson(
                        jsonInString,
                        DelcomResponse::class.java
                    ).message
                )
            )
        }
    }

    /**
     * Update Lost & Found
     */
    fun putLofo(
        lofoId: Int,
        title: String,
        description: String,
        isFinished: Boolean,
    ) = flow {
        emit(MyResult.Loading)

        try {
            //get success message
            emit(
                MyResult.Success(
                    apiService.putLofo(
                        lofoId,
                        title,
                        description,
                        if (isFinished) 1 else 0
                    )
                )
            )
        } catch (e: HttpException) {
            //get error message
            val jsonInString = e.response()?.errorBody()?.string()
            emit(
                MyResult.Error(
                    Gson()
                        .fromJson(
                            jsonInString,
                            DelcomResponse::class.java
                        )
                        .message
                )
            )
        }
    }

    /**
     * Get All Lost & Found
     */
    fun getLofos(
        isFinished: Int?,
        isMe: Int?,
        lofoStatus: String?,) = flow {
        emit(MyResult.Loading)

        try {
            emit(MyResult.Success(
                apiService.getLofos(
                    isFinished,
                    isMe,
                    lofoStatus
                )
            ))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            emit(
                MyResult.Error(
                    Gson().fromJson(
                        jsonInString,
                        DelcomResponse::class.java
                    ).message
                )
            )
        }
    }

    /**
     * Get Lost & Found by ID
     */
    fun getLofo(
        lofoId: Int
    ) = flow {
        emit(MyResult.Loading)

        try {
            emit(MyResult.Success(
                apiService.getLofo(
                    lofoId
                )
            ))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            emit(
                MyResult.Error(
                    Gson().fromJson(
                        jsonInString,
                        DelcomResponse::class.java
                    ).message
                )
            )
        }
    }

    /**
     * Delete Lost & Found
     */
    fun deleteLofo(
        lofoId: Int
    ) = flow {
        emit(MyResult.Loading)

        try {
            emit(
                MyResult.Success(
                    apiService.deleteLofo(
                        lofoId
                    )
                )
            )
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            emit(
                MyResult.Error(
                    Gson().fromJson(
                        jsonInString,
                        DelcomResponse::class.java
                    ).message
                )
            )
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: LofoRepository? = null
        fun getInstance(
            apiService: IApiService,
        ): LofoRepository {
            synchronized(LofoRepository::class.java) {
                INSTANCE = LofoRepository(
                    apiService
                )
            }
            return INSTANCE as LofoRepository
        }
    }
}