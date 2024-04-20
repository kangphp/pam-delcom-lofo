package com.ifs21031.lostfound.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ifs21031.lostfound.data.repository.AuthRepository
import com.ifs21031.lostfound.data.repository.LofoRepository
import com.ifs21031.lostfound.data.repository.UserRepository
import com.ifs21031.lostfound.di.Injection
import com.ifs21031.lostfound.presentation.lofo.LofoViewModel
import com.ifs21031.lostfound.presentation.login.LoginViewModel
import com.ifs21031.lostfound.presentation.main.MainViewModel
import com.ifs21031.lostfound.presentation.profile.ProfileViewModel
import com.ifs21031.lostfound.presentation.register.RegisterViewModel

class ViewModelFactory (
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val lofoRepository: LofoRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel
                    .getInstance(authRepository) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel
                    .getInstance(authRepository) as T
            }

            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel
                    .getInstance(authRepository, lofoRepository) as T
            }

            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel
                    .getInstance(authRepository, userRepository) as T
            }

            modelClass.isAssignableFrom(LofoViewModel::class.java) -> {
                LofoViewModel
                    .getInstance(lofoRepository) as T
            }

            else -> throw IllegalArgumentException(
                "Unknown ViewModel class: " + modelClass.name
            )
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            synchronized(ViewModelFactory::class.java) {
                INSTANCE = ViewModelFactory(
                    Injection.provideAuthRepository(context),
                    Injection.provideUserRepository(context),
                    Injection.provideLofoRepository(context)
                )
            }
            return INSTANCE as ViewModelFactory
        }
    }
}