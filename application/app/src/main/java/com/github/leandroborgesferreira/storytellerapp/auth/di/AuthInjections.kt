package com.github.leandroborgesferreira.storytellerapp.auth.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.leandroborgesferreira.storytellerapp.auth.login.LoginViewModel
import com.github.leandroborgesferreira.storytellerapp.auth.register.RegisterViewModel
import com.github.leandroborgesferreira.storytellerapp.navigation.NavigationActivity

class AuthInjections {

    @Composable
    internal fun provideRegisterViewModel(): RegisterViewModel =
        viewModel(initializer = { RegisterViewModel() })

    @Composable
    internal fun provideLoginViewModel(): LoginViewModel =
        viewModel(initializer = { LoginViewModel() })
}
