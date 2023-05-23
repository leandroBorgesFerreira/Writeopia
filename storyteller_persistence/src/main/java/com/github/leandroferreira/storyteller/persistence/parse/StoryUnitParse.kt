package com.github.leandroferreira.storyteller.persistence.parse

import com.github.leandroferreira.storyteller.model.story.GroupStep
import com.github.leandroferreira.storyteller.model.story.StoryStep
import com.github.leandroferreira.storyteller.model.story.StoryUnit
import com.github.leandroferreira.storyteller.persistence.entity.story.StoryUnitEntity

fun Map<Int, StoryUnit>.toEntity(documentId: String): List<StoryUnitEntity> =
    flatMap { (position, storyUnit) ->
        if (storyUnit is GroupStep) {
            listOf(storyUnit.toEntity(position, documentId)) + storyUnit.steps.map { innerStory ->
                innerStory.copyWithNewParent(storyUnit.id).toEntity(position, documentId)
            }
        } else {
            listOf((storyUnit as StoryStep).toEntity(position, documentId))
        }
    }

fun StoryUnitEntity.toModel(steps: List<StoryUnitEntity> = emptyList()): StoryUnit =
    if (isGroup) {
        GroupStep(
            id = id,
            localId = localId,
            type = type,
            parentId = parentId,
            steps = steps.map { storyUnitEntity -> storyUnitEntity.toModel() },
        )
    } else {
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
    }

fun StoryUnit.toEntity(position: Int, documentId: String): StoryUnitEntity =
    when (this) {
        is GroupStep -> toEntity(position, documentId)
        is StoryStep -> toEntity(position, documentId)
        else -> throw IllegalStateException()
    }

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


fun GroupStep.toEntity(position: Int, documentId: String): StoryUnitEntity =
    StoryUnitEntity(
        id = id,
        localId = localId,
        type = type,
        parentId = parentId,
        position = position,
        documentId = documentId,
        isGroup = true,
        hasInnerSteps = this.steps.isNotEmpty(),
    )
