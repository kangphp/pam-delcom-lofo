package com.ifs21031.lostfound.presentation.lofo

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ifs21031.lostfound.data.remote.MyResult
import com.ifs21031.lostfound.data.remote.response.DataAddLoFoResponse
import com.ifs21031.lostfound.data.remote.response.DelcomLoFoResponse
import com.ifs21031.lostfound.data.remote.response.DelcomResponse
import com.ifs21031.lostfound.data.repository.LofoRepository
import com.ifs21031.lostfound.presentation.ViewModelFactory

class LofoViewModel (
    private val lofoRepository: LofoRepository
) : ViewModel() {

    fun getLofo(
        lofoId: Int
    ): LiveData<MyResult<DelcomLoFoResponse>> {
        return lofoRepository.getLofo(
            lofoId
        ).asLiveData()
    }

    fun postLofo(
        title: String,
        description : String
    ): LiveData<MyResult<DataAddLoFoResponse>> {
        return lofoRepository.postLofo(
            title,
            description
        ).asLiveData()
    }

    fun putLofo(
        lofoId: Int,
        title: String,
        description: String,
        isFinished: Boolean,
    ): LiveData<MyResult<DelcomResponse>> {
        return lofoRepository.putLofo(
            lofoId,
            title,
            description,
            isFinished
        ).asLiveData()
    }

    fun deleteLofo(
        lofoId: Int
    ): LiveData<MyResult<DelcomResponse>> {
        return lofoRepository.deleteLofo(
            lofoId
        ).asLiveData()
    }

    companion object {
        @Volatile
        private var INSTANCE: LofoViewModel? = null
        fun getInstance(
            lofoRepository: LofoRepository
        ): LofoViewModel {
            synchronized(ViewModelFactory::class.java) {
                INSTANCE = LofoViewModel(
                    lofoRepository
                )
            }
            return INSTANCE as LofoViewModel
        }
    }
}