package io.writeopia.note_menu.di

import io.writeopia.repository.UiConfigurationRepository
import io.writeopia.repository.UiConfigurationSqlDelightRepository
import io.writeopia.sql.WriteopiaDb
import io.writeopia.sqldelight.theme.UiConfigurationSqlDelightDao
import io.writeopia.viewmodel.UiConfigurationKmpViewModel
import io.writeopia.viewmodel.UiConfigurationViewModel
import kotlinx.coroutines.CoroutineScope

actual class UiConfigurationInjector(private val writeopiaDb: WriteopiaDb?) {

    private fun provideUiConfigurationSqlDelightDao(): UiConfigurationSqlDelightDao =
        UiConfigurationSqlDelightDao(writeopiaDb)

    actual fun provideUiConfigurationRepository(): UiConfigurationRepository =
        UiConfigurationSqlDelightRepository(provideUiConfigurationSqlDelightDao())

    //Todo: Check if the code can be merged into the same implementation,
    // only the repository needs to change
    fun provideUiConfigurationViewModel(
        uiConfigurationSqlDelightRepository: UiConfigurationRepository = provideUiConfigurationRepository(),
        coroutineScope: CoroutineScope? = null
    ): UiConfigurationViewModel =
        UiConfigurationKmpViewModel(uiConfigurationSqlDelightRepository).apply {
            if (coroutineScope != null) {
                initCoroutine(coroutineScope = coroutineScope)
            }
        }
}
