package io.writeopia.sdk.serialization.storage;

import kotlinx.serialization.Serializable

@Serializable
data class WorkspaceStorageConfig(
    val lastUpdateTable: Long
)