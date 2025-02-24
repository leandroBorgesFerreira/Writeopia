package io.writeopia.sqldelight.dao

import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import io.writeopia.app.sql.NotesConfiguration
import io.writeopia.app.sql.NotesConfigurationEntityQueries
import io.writeopia.app.sql.OnboardingEntityQueries
import io.writeopia.app.sql.WorkspaceConfiguration
import io.writeopia.app.sql.WorkspaceConfigurationEntityQueries
import io.writeopia.common.utils.extensions.toBoolean
import io.writeopia.sql.WriteopiaDb

class ConfigurationSqlDelightDao(database: WriteopiaDb?) {

    private val notesConfigurationQueries: NotesConfigurationEntityQueries? =
        database?.notesConfigurationEntityQueries

    private val workspaceConfigurationQueries: WorkspaceConfigurationEntityQueries? =
        database?.workspaceConfigurationEntityQueries

    private val onboardingQueries: OnboardingEntityQueries? =
        database?.onboardingEntityQueries

    suspend fun saveNotesConfiguration(notesConfiguration: NotesConfiguration) {
        notesConfiguration.run {
            notesConfigurationQueries?.insert(user_id, arrangement_type, order_by)
        }
    }

    suspend fun saveWorkspaceConfiguration(workspaceConfiguration: WorkspaceConfiguration) {
        workspaceConfiguration.run {
            workspaceConfigurationQueries?.insert(
                user_id = user_id,
                path = path,
                has_first_configuration = has_first_configuration,
            )
        }
    }

    suspend fun getConfigurationByUserId(userId: String): NotesConfiguration? =
        notesConfigurationQueries?.selectConfigurationByUserId(userId)?.awaitAsOneOrNull()

    suspend fun getWorkspaceByUserId(userId: String): WorkspaceConfiguration? =
        workspaceConfigurationQueries?.selectWorkspaceConfigurationByUserId(userId)
            ?.awaitAsOneOrNull()

    suspend fun isOnboarded(): Boolean =
        onboardingQueries?.query("writeopia_app")?.awaitAsOneOrNull()?.is_onboarded?.toBoolean() ?: false

    suspend fun setOnboarded() {
        onboardingQueries?.insert("writeopia_app", 1L)
    }
}
