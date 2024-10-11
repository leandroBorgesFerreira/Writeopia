package io.writeopia.repository

import io.writeopia.model.ColorThemeOption
import io.writeopia.model.UiConfiguration
import io.writeopia.repository.extensions.toModel
import io.writeopia.repository.extensions.toRoomEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UiConfigurationRepositoryImpl(
    private val uiConfigurationDao: UiConfigurationDao
) : UiConfigurationRepository {
    override suspend fun insertUiConfiguration(uiConfiguration: UiConfiguration) {
        uiConfigurationDao.saveUiConfiguration(uiConfiguration.toRoomEntity())
    }

    override suspend fun getUiConfigurationEntity(userId: String): UiConfiguration? =
        uiConfigurationDao.getConfigurationByUserId(userId)?.toModel()

    override suspend fun updateShowSideMenu(userId: String, showSideMenu: Boolean) {
        val entity = getUiConfigurationEntity(userId)

        if (entity != null) {
            insertUiConfiguration(entity.copy(showSideMenu = showSideMenu))
        } else {
            insertUiConfiguration(
                UiConfiguration(
                    userId = userId,
                    showSideMenu = showSideMenu,
                    colorThemeOption = ColorThemeOption.SYSTEM,
                )
            )
        }
    }

    override suspend fun updateColorTheme(userId: String, colorThemeOption: ColorThemeOption) {
        val entity = getUiConfigurationEntity(userId)

        if (entity != null) {
            insertUiConfiguration(entity.copy(colorThemeOption = colorThemeOption))
        } else {
            insertUiConfiguration(
                UiConfiguration(
                    userId = userId,
                    showSideMenu = true,
                    colorThemeOption = colorThemeOption,
                )
            )
        }
    }

    override fun listenForUiConfiguration(
        getUserId: suspend () -> String,
        coroutineScope: CoroutineScope
    ): Flow<UiConfiguration?> =
        uiConfigurationDao.listenForConfigurationByUserId("")
            .map { entity ->
                entity?.toModel()
            }
}
