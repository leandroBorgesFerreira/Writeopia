package br.com.leandroferreira.app_sample.parse

import br.com.leandroferreira.storyteller.model.story.GroupStep
import br.com.leandroferreira.storyteller.model.story.StoryStep
import br.com.leandroferreira.storyteller.model.story.StoryUnit
import br.com.leandroferreira.storyteller.persistence.entity.story.StoryUnitEntity

fun Map<Int, StoryUnit>.toEntity(id: String): List<StoryUnitEntity> =
    map { (position, storyUnit) ->
        if (storyUnit is GroupStep) {
            storyUnit.toEntity(position, id)
        } else {
            (storyUnit as StoryStep).toEntity(position, id)
        }
    }


fun StoryUnitEntity.toModel(): StoryUnit =
    if (isGroup) {
        GroupStep(
            localId = id,
            type = type,
            parentId = parentId,
//            steps = steps, Todo!!!
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
//            steps = steps, Todo!!!
        )
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
//        innerUnitIds = this.steps.map { storyUnit -> storyUnit.id },
        position = position,
        documentId = documentId,
        isGroup = false,
    )


fun GroupStep.toEntity(position: Int, documentId: String): StoryUnitEntity =
    StoryUnitEntity(
        id = localId,
        type = type,
        parentId = parentId,
//        innerUnitIds = this.steps.map { it.id }, Todo!
        position = position,
        documentId = documentId,
        isGroup = false,
    )
