package io.writeopia.note_menu.di

import io.writeopia.repository.UiConfigurationRepository
import io.writeopia.sql.WriteopiaDb
import io.writeopia.sqldelight.theme.UiConfigurationSqlDelightDao

actual class UiConfigurationInjector(private val writeopiaDb: WriteopiaDb?) {

    private fun provideDao(): UiConfigurationSqlDelightDao =
        UiConfigurationSqlDelightDao(writeopiaDb)

    actual fun provideUiConfigurationRepository(): UiConfigurationRepository =
        UiConfigurationRepository(provideDao())
}
