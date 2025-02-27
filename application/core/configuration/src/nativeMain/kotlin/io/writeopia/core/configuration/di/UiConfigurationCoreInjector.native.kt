package io.writeopia.core.configuration.di

import io.writeopia.repository.UiConfigurationRepository
import io.writeopia.repository.UiConfigurationSqlDelightRepository
import io.writeopia.sqldelight.di.WriteopiaDbInjector
import io.writeopia.sqldelight.theme.UiConfigurationSqlDelightDao

actual class UiConfigurationCoreInjector {

    private fun provideUiConfigurationSqlDelightDao() =
        UiConfigurationSqlDelightDao(WriteopiaDbInjector.singleton()?.database)

    actual fun provideUiConfigurationRepository(): UiConfigurationRepository =
        UiConfigurationSqlDelightRepository.singleton(provideUiConfigurationSqlDelightDao())

    actual companion object {
        private var instance: UiConfigurationCoreInjector? = null

        actual fun singleton(): UiConfigurationCoreInjector =
            instance ?: UiConfigurationCoreInjector().also {
                instance = it
            }
    }
}
