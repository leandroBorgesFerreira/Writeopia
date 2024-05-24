package io.writeopia.repository

import io.writeopia.app.sql.UiConfigurationEntity
import io.writeopia.model.ColorThemeOption
import io.writeopia.model.UiConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class UiConfigurationRoomRepository : UiConfigurationRepository {
    override suspend fun insertUiConfiguration(uiConfiguration: UiConfiguration) {
        TODO("Not yet implemented")
    }

    override suspend fun getUiConfigurationEntity(userId: String): UiConfigurationEntity? {
        TODO("Not yet implemented")
    }

    override suspend fun updateShowSideMenu(userId: String, showSideMenu: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun updateColorTheme(userId: String, colorThemeOption: ColorThemeOption) {
        TODO("Not yet implemented")
    }

    override fun listenForUiConfiguration(
        getUserId: suspend () -> String,
        coroutineScope: CoroutineScope
    ): Flow<UiConfiguration?> {
        TODO("Not yet implemented")
    }
}
