package io.writeopia.note_menu.data.repository

import io.writeopia.app.sql.UiConfigurationEntity
import io.writeopia.model.ColorThemeOption
import io.writeopia.model.UiConfiguration
import io.writeopia.note_menu.extensions.toEntity
import io.writeopia.note_menu.extensions.toModel
import io.writeopia.sqldelight.theme.UiConfigurationSqlDelightDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map

class UiConfigurationRepository(private val uiConfigurationDao: UiConfigurationSqlDelightDao) {

    private suspend fun insertUiConfiguration(uiConfiguration: UiConfiguration) {
        uiConfigurationDao.saveUiConfiguration(uiConfiguration.toEntity())
    }

    private suspend fun getUiConfigurationEntity(userId: String): UiConfigurationEntity? =
        uiConfigurationDao.getConfigurationByUserId(userId)

    suspend fun updateShowSideMenu(userId: String, showSideMenu: Boolean) {
        val entity = getUiConfigurationEntity(userId)

        if (entity != null) {
            insertUiConfiguration(entity.toModel().copy(showSideMenu = showSideMenu))
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

    suspend fun updateColorTheme(userId: String, colorThemeOption: ColorThemeOption) {
        val entity = getUiConfigurationEntity(userId)

        if (entity != null) {
            insertUiConfiguration(entity.toModel().copy(colorThemeOption = colorThemeOption))
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

    fun listenForColorTheme(getUserId: suspend () -> String, coroutineScope: CoroutineScope) =
        uiConfigurationDao.listenForConfigurationByUserId(getUserId, coroutineScope).map { entity ->
            entity.toModel()
        }
}
