package io.writeopia.repository

import io.writeopia.app.sql.UiConfigurationEntity
import io.writeopia.extensions.toEntity
import io.writeopia.extensions.toModel
import io.writeopia.model.ColorThemeOption
import io.writeopia.model.UiConfiguration
import io.writeopia.sqldelight.theme.UiConfigurationSqlDelightDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UiConfigurationSqlDelightRepository internal constructor(
    private val uiConfigurationDao: UiConfigurationSqlDelightDao
) : UiConfigurationRepository {

    override suspend fun insertUiConfiguration(uiConfiguration: UiConfiguration) {
        uiConfigurationDao.saveUiConfiguration(uiConfiguration.toEntity())
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
        uiConfigurationDao.listenForConfigurationByUserId(getUserId, coroutineScope)
            .map { entity ->
                entity?.toModel()
            }

    companion object {
        var instance: UiConfigurationSqlDelightRepository? = null

        fun singleton(
            uiConfigurationDao: UiConfigurationSqlDelightDao
        ): UiConfigurationSqlDelightRepository = instance ?: UiConfigurationSqlDelightRepository(
            uiConfigurationDao
        ).also {
            instance = it
        }

    }
}
