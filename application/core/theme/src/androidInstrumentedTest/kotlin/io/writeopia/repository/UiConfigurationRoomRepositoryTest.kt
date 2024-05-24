package io.writeopia.repository

import io.writeopia.sqldelight.database.DatabaseFactory
import io.writeopia.sqldelight.database.driver.DriverFactory
import io.writeopia.sqldelight.theme.UiConfigurationSqlDelightDao
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class UiConfigurationRoomRepositoryTest {

    @Test
    fun shouldBePossibleToSaveAndGetColorOptionTheme() = runTest {
        UiConfigurationRepositoryCommonTest.shouldBePossibleToSaveAndGetColorTheme(getRepository())
    }

    @Test
    fun shouldBePossibleToKeepChoiceToHideSideMenu() = runTest {
        UiConfigurationRepositoryCommonTest.shouldBePossibleToKeepChoiceToHideSideMenu(
            getRepository()
        )
    }

    private suspend fun getRepository(): UiConfigurationRepository {
        return UiConfigurationRoomRepository()
    }
}
