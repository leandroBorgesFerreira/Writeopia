package com.github.leandroborgesferreira.storyteller.intronotes.extensions

import com.github.leandroborgesferreira.storyteller.intronotes.persistence.entity.DocumentEntity
import com.github.leandroborgesferreira.storyteller.intronotes.persistence.entity.StoryStepEntity
import com.github.leandroborgesferreira.storyteller.serialization.data.DocumentApi

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