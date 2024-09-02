package io.writeopia.global.shell.di

import androidx.compose.runtime.Composable
import io.writeopia.global.shell.viewmodel.SideMenuViewModel
import kotlinx.coroutines.CoroutineScope

interface SideMenuInjector {

    @Composable
    fun provideSideMenuViewModel(
        coroutineScope: CoroutineScope?,
    ): SideMenuViewModel
}
