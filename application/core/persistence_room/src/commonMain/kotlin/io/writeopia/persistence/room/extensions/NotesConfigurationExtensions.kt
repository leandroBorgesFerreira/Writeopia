package io.writeopia.persistence.room.extensions

import io.writeopia.common.utils.persistence.configuration.NotesConfigurationCommonEntity
import io.writeopia.persistence.room.data.entities.NotesConfigurationEntity

fun NotesConfigurationEntity.toCommonEntity(): NotesConfigurationCommonEntity {
    return NotesConfigurationCommonEntity(
        userId = this.userId,
        arrangementType = this.arrangementType,
        orderByType = this.orderByType
    )
}

fun NotesConfigurationCommonEntity.toEntity(): NotesConfigurationEntity {
    return NotesConfigurationEntity(
        userId = this.userId,
        arrangementType = this.arrangementType,
        orderByType = this.orderByType
    )
}
