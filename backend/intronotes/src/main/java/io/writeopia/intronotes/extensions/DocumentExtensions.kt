package io.writeopia.intronotes.extensions

import io.writeopia.intronotes.model.StoryTypes
import io.writeopia.intronotes.persistence.entity.DocumentEntity
import io.writeopia.intronotes.persistence.entity.StoryStepEntity
import io.writeopia.sdk.serialization.data.DocumentApi
import io.writeopia.sdk.serialization.data.StoryStepApi
import io.writeopia.sdk.serialization.extensions.toApi
import java.time.Instant

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
                title = storyStep.title,
                checked = storyStep.checked,
                position = storyStep.position,
            )
        },
        createdAt = this.createdAt.toEpochMilli(),
        lastUpdatedAt = this.lastUpdatedAt.toEpochMilli()
    )

internal fun DocumentEntity.toAPi(): DocumentApi =
    DocumentApi(
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
                title = storyStepEntity.title,
                checked = storyStepEntity.checked,
                position = storyStepEntity.position!!,
            )
        } ?: emptyList(),
        createdAt = this.createdAt?.let(Instant::ofEpochMilli) ?: Instant.now(),
        lastUpdatedAt = this.lastUpdatedAt?.let(Instant::ofEpochMilli) ?: Instant.now(),
        userId = this.userId ?: ""
    )