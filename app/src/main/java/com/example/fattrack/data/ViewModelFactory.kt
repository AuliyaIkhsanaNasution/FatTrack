package com.example.fattrack.data

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fattrack.data.di.Injection
import com.example.fattrack.data.pref.ProfilePreferences
import com.example.fattrack.data.repositories.ArticleRepository
import com.example.fattrack.data.repositories.AuthRepository
import com.example.fattrack.data.viewmodel.ArticlesViewModel
import com.example.fattrack.data.viewmodel.LoginViewModel
import com.example.fattrack.data.viewmodel.ProfileViewModel
import com.example.fattrack.data.viewmodel.RegisterViewModel

class ViewModelFactory(
    private val articleRepository: ArticleRepository,
    private val profilePreferences: ProfilePreferences,
    private val authRepository: AuthRepository
    ) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context : Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(
                        Injection.provideArticlesRepository(),
                        Injection.provideProfilePreferences(context),
                        Injection.provideAuthRepository(context)
                    )
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(authRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(authRepository) as T
            }
            modelClass.isAssignableFrom(ArticlesViewModel::class.java) -> {
                ArticlesViewModel(articleRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(profilePreferences) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}