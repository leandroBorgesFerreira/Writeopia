package com.github.leandroborgesferreira.storyteller.persistence.parse

import com.github.leandroborgesferreira.storyteller.model.story.StoryStep
import com.github.leandroborgesferreira.storyteller.persistence.entity.story.StoryUnitEntity

fun Map<Int, StoryStep>.toEntity(documentId: String): List<StoryUnitEntity> =
    flatMap { (position, storyUnit) ->
        if (storyUnit.isGroup) {
            listOf(storyUnit.toEntity(position, documentId)) + storyUnit.steps.map { innerStory ->
                innerStory.copy(parentId = storyUnit.id).toEntity(position, documentId)
            }
        } else {
            listOf(storyUnit.toEntity(position, documentId))
        }
    }

fun StoryUnitEntity.toModel(steps: List<StoryUnitEntity> = emptyList()): StoryStep =
    StoryStep(
        id = id,
        localId = localId,
        type = type,
        parentId = parentId,
        url = url,
        path = path,
        text = text,
        title = title,
        checked = checked,
        steps = steps.map { storyUnitEntity -> storyUnitEntity.toModel() },
    )

fun StoryStep.toEntity(position: Int, documentId: String): StoryUnitEntity =
    StoryUnitEntity(
        id = id,
        localId = localId,
        type = type,
        parentId = parentId,
        url = url,
        path = path,
        text = text,
        title = title,
        checked = checked,
        position = position,
        documentId = documentId,
        isGroup = false,
        hasInnerSteps = this.steps.isNotEmpty(),
    )
