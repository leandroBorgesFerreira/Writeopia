package br.com.leandroferreira.app_sample.parse

import br.com.leandroferreira.storyteller.model.story.GroupStep
import br.com.leandroferreira.storyteller.model.story.StoryStep
import br.com.leandroferreira.storyteller.model.story.StoryUnit
import br.com.leandroferreira.storyteller.persistence.entity.story.StoryUnitEntity

fun StoryUnitEntity.toModel(): StoryUnit =
    if (isGroup) {
        GroupStep(
            id = id,
            type = type,
            parentId = parentId,
//            steps = steps, Todo!!!
        )
    } else {
        StoryStep(
            id = id,
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
        id = id,
        type = type,
        parentId = parentId,
        url = url,
        path = path,
        text = text,
        title = title,
        checked = checked,
//        innerUnitIds = innerUnitIds, Todo!!!
        position = position,
        documentId = documentId,
        isGroup = false,
    )


fun GroupStep.toEntity(position: Int, documentId: String): StoryUnitEntity =
    StoryUnitEntity(
        id = id,
        type = type,
        parentId = parentId,
//        innerUnitIds = innerUnitIds, Todo!!!
        position = position,
        documentId = documentId,
        isGroup = false,
    )
