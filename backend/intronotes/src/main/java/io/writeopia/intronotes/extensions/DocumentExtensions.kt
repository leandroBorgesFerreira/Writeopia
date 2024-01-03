package io.writeopia.intronotes.extensions

import io.writeopia.intronotes.model.StoryTypes
import io.writeopia.intronotes.persistence.entity.DocumentEntity
import io.writeopia.intronotes.persistence.entity.StoryStepEntity
import io.writeopia.sdk.serialization.data.DocumentApi
import io.writeopia.sdk.serialization.data.StoryStepApi
import io.writeopia.sdk.serialization.extensions.toApi
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

internal fun DocumentApi.toEntity(): DocumentEntity =
    DocumentEntity(
        id = this.id,
        title = this.title,
        content = content.map { storyStep ->
            StoryStepEntity(
                id = storyStep.id,
                type = storyStep.type.name,
                parentId = storyStep.parentId,
                url = storyStep.url,
                path = storyStep.path,
                text = storyStep.text,
                checked = storyStep.checked,
                position = storyStep.position,
            )
        },
        createdAt = this.createdAt,
        lastUpdatedAt = this.lastUpdatedAt
    )

internal fun DocumentEntity.toAPi(): DocumentApi {
    val now = Clock.System.now()

    return DocumentApi(
        id = this.id ?: "",
        title = this.title ?: "",
        content = content?.map { storyStepEntity ->
            StoryStepApi(
                id = storyStepEntity.id!!,
                type = StoryTypes.fromName(storyStepEntity.type!!).type.toApi(),
                parentId = storyStepEntity.parentId,
                url = storyStepEntity.url,
                path = storyStepEntity.path,
                text = storyStepEntity.text,
                checked = storyStepEntity.checked,
                position = storyStepEntity.position!!,
            )
        } ?: emptyList(),
        createdAt = this.createdAt ?: now.toEpochMilliseconds(),
        lastUpdatedAt = this.lastUpdatedAt ?: now.toEpochMilliseconds(),
        userId = this.userId ?: ""
    )
}
