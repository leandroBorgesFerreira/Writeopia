package io.writeopia.repository

import io.writeopia.model.ColorThemeOption
import io.writeopia.model.UiConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class UiConfigurationMemoryRepository : UiConfigurationRepository {
    private var uiConfiguration = MutableStateFlow(
        UiConfiguration(
            userId = "userId",
            showSideMenu = true,
            colorThemeOption = ColorThemeOption.SYSTEM,
            sideMenuWidth = 280F
        )
    )

    override suspend fun insertUiConfiguration(uiConfiguration: UiConfiguration) {
        this.uiConfiguration.value = uiConfiguration
    }

    override suspend fun getUiConfigurationEntity(userId: String): UiConfiguration =
        uiConfiguration.value

    override suspend fun updateShowSideMenu(userId: String, showSideMenu: Boolean) {
        uiConfiguration.value = uiConfiguration.value.copy(showSideMenu = showSideMenu)
    }

    override suspend fun updateColorTheme(userId: String, colorThemeOption: ColorThemeOption) {
        uiConfiguration.value = uiConfiguration.value.copy(colorThemeOption = colorThemeOption)
    }

    override fun listenForUiConfiguration(
        getUserId: suspend () -> String,
        coroutineScope: CoroutineScope
    ): Flow<UiConfiguration?> = uiConfiguration
}
