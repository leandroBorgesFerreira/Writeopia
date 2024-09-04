package io.writeopia.sqldelight.theme

import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import io.writeopia.app.sql.UiConfigurationEntity
import io.writeopia.app.sql.UiConfigurationEntityQueries
import io.writeopia.sql.WriteopiaDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class UiConfigurationSqlDelightDao(database: WriteopiaDb?) {

    private val configurationState = MutableStateFlow<UiConfigurationEntity?>(null)

    private var userId: String? = null

    private val uiConfigurationQueries: UiConfigurationEntityQueries? =
        database?.uiConfigurationEntityQueries

    suspend fun saveUiConfiguration(uiConfiguration: UiConfigurationEntity) {
        uiConfiguration.run {
            uiConfigurationQueries?.insert(
                user_id = user_id,
                show_side_menu = show_side_menu,
                color_theme_option = color_theme_option
            )
        }

//        if (userId == uiConfiguration.user_id) {
            configurationState.value = uiConfiguration
//        }
    }

    suspend fun getConfigurationByUserId(userId: String): UiConfigurationEntity? =
        uiConfigurationQueries?.selectConfigurationByUserId(
            "user_offline"
//            userId
        )?.awaitAsOneOrNull()

    fun listenForConfigurationByUserId(
        getUserId: suspend () -> String,
        coroutineScope: CoroutineScope
    ): Flow<UiConfigurationEntity?> {
        coroutineScope.launch(Dispatchers.Default) {
            val id = "user_offline"
//                getUserId()

            userId = id
            configurationState.value = getConfigurationByUserId(id)
        }

        return configurationState
    }

}
