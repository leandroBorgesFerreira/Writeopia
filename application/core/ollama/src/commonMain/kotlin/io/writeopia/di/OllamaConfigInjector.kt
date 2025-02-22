package io.writeopia.di

import androidx.compose.runtime.Composable
import io.writeopia.controller.OllamaConfigController

interface OllamaConfigInjector {

    @Composable
    fun provideOllamaConfigController() : OllamaConfigController
}
