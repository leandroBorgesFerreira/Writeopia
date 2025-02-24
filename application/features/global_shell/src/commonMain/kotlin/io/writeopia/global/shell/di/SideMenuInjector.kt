package io.writeopia.global.shell.di

import androidx.compose.runtime.Composable
import io.writeopia.global.shell.viewmodel.GlobalShellViewModel

interface SideMenuInjector {

    @Composable
    fun provideSideMenuViewModel(): GlobalShellViewModel
}
