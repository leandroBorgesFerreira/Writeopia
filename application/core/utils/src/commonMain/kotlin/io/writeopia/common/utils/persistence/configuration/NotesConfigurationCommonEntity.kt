package io.writeopia.common.utils.persistence.configuration

data class NotesConfigurationCommonEntity(
    val userId: String,
    val arrangementType: String,
    val orderByType: String,
    val hasTutorials: Boolean
)
