package com.github.leandroborgesferreira.storytellerapp.auth.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.leandroborgesferreira.storytellerapp.auth.login.LoginViewModel
import com.github.leandroborgesferreira.storytellerapp.auth.register.RegisterViewModel

class AuthInjections {

    @Composable
    fun provideRegisterViewModel(): RegisterViewModel =
        viewModel(initializer = { RegisterViewModel() })

    @Composable
    fun provideLoginViewModel(): LoginViewModel = viewModel(initializer = { LoginViewModel() })
}
