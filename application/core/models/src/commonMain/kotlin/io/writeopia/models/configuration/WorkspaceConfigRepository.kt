package io.writeopia.models.configuration

interface WorkspaceConfigRepository {

    suspend fun loadWorkspacePath(userId: String): String?

    suspend fun saveWorkspacePath(path: String, userId: String)

    suspend fun isOnboarded(): Boolean

    suspend fun setOnboarded()
}
