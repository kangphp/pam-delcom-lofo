package com.ifs21031.lostfound.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope

import com.ifs21031.lostfound.data.pref.UserModel
import com.ifs21031.lostfound.data.remote.MyResult
import com.ifs21031.lostfound.data.remote.response.DelcomLoFosResponse
import com.ifs21031.lostfound.data.remote.response.DelcomResponse
import com.ifs21031.lostfound.data.repository.AuthRepository
import com.ifs21031.lostfound.data.repository.LofoRepository
import com.ifs21031.lostfound.presentation.ViewModelFactory
import kotlinx.coroutines.launch

class MainViewModel(
    private val authRepository: AuthRepository,
    private val lofoRepository: LofoRepository
) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return authRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    fun getLofos(): LiveData<MyResult<DelcomLoFosResponse>> {
        return lofoRepository.getLofos(null, null, null).asLiveData()
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
            isFinished,
        ).asLiveData()
    }

    companion object {
        @Volatile
        private var INSTANCE: MainViewModel? = null
        fun getInstance(
            authRepository: AuthRepository,
            todoRepository: LofoRepository
        ): MainViewModel {
            synchronized(ViewModelFactory::class.java) {
                INSTANCE = MainViewModel(
                    authRepository,
                    todoRepository
                )
            }
            return INSTANCE as MainViewModel
        }
    }
}