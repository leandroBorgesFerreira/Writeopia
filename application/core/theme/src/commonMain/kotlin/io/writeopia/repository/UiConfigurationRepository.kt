package io.writeopia.repository

import io.writeopia.model.ColorThemeOption
import io.writeopia.model.UiConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface UiConfigurationRepository {
    suspend fun insertUiConfiguration(uiConfiguration: UiConfiguration)

    suspend fun getUiConfigurationEntity(userId: String): UiConfiguration?

    suspend fun updateColorTheme(userId: String, colorThemeOption: ColorThemeOption)

    fun listenForUiConfiguration(
        getUserId: suspend () -> String,
        coroutineScope: CoroutineScope
    ): Flow<UiConfiguration?>
}
