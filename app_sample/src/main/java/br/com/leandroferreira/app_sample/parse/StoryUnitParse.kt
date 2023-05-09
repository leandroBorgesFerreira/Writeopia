package br.com.leandroferreira.app_sample.parse

import br.com.leandroferreira.storyteller.model.story.GroupStep
import br.com.leandroferreira.storyteller.model.story.StoryStep
import br.com.leandroferreira.storyteller.model.story.StoryUnit
import br.com.leandroferreira.storyteller.persistence.entity.story.StoryUnitEntity

fun Map<Int, StoryUnit>.toEntity(documentId: String): List<StoryUnitEntity> =
    flatMap { (position, storyUnit) ->
        if (storyUnit is GroupStep) {
            listOf(storyUnit.toEntity(position, documentId)) + storyUnit.steps.map { storyUnit ->
                storyUnit.toEntity(position, documentId)
            }
        } else {
            listOf((storyUnit as StoryStep).toEntity(position, documentId))
        }
    }

fun StoryUnitEntity.toModel(steps: List<StoryUnitEntity> = emptyList()): StoryUnit =
    if (isGroup) {
        GroupStep(
            localId = id,
            type = type,
            parentId = parentId,
            steps = steps.map { storyUnitEntity -> storyUnitEntity.toModel() },
        )
    } else {
        StoryStep(
            localId = id,
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
        id = localId,
        type = type,
        parentId = parentId,
        url = url,
        path = path,
        text = text,
        title = title,
        checked = checked,
        innerUnitIds = this.steps.map { storyUnit -> storyUnit.id },
        position = position,
        documentId = documentId,
        isGroup = false,
    )


fun GroupStep.toEntity(position: Int, documentId: String): StoryUnitEntity =
    StoryUnitEntity(
        id = localId,
        type = type,
        parentId = parentId,
        innerUnitIds = this.steps.map { it.id },
        position = position,
        documentId = documentId,
        isGroup = true,
    )
