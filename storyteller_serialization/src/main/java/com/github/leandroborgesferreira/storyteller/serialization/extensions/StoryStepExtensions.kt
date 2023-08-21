package com.github.leandroborgesferreira.storyteller.serialization.extensions

import com.github.leandroborgesferreira.storyteller.models.document.Document
import com.github.leandroborgesferreira.storyteller.models.story.Decoration
import com.github.leandroborgesferreira.storyteller.models.story.StoryStep
import com.github.leandroborgesferreira.storyteller.models.story.StoryType
import com.github.leandroborgesferreira.storyteller.serialization.data.DecorationApi
import com.github.leandroborgesferreira.storyteller.serialization.data.DocumentApi
import com.github.leandroborgesferreira.storyteller.serialization.data.StoryStepApi
import com.github.leandroborgesferreira.storyteller.serialization.data.StoryTypeApi

fun StoryStep.toApi(position: Int): StoryStepApi =
    StoryStepApi(
        id = this.id,
        type = this.type.toApi(),
        parentId = this.parentId,
        url = this.url,
        path = this.path,
        text = this.text,
        title = this.title,
        checked = this.checked,
        steps = this.steps.map { storyStep -> storyStep.toApi(position) },
        decoration = this.decoration.toApi(),
        position = position
    )

fun StoryStepApi.toModel(): StoryStep =
    StoryStep(
        id = id,
        type = type.toModel(),
        parentId = parentId,
        url = url,
        path = path,
        text = text,
        title = title,
        checked = checked,
        steps = steps.map { it.toModel() },
        decoration = decoration.toModel(),
    )

fun StoryType.toApi(): StoryTypeApi =
    StoryTypeApi(
        name = this.name,
        number = this.number,
    )

fun StoryTypeApi.toModel(): StoryType =
    StoryType(
        name = this.name,
        number = this.number,
    )

fun Decoration.toApi(): DecorationApi = DecorationApi(this.backgroundColor)

fun DecorationApi.toModel(): Decoration = Decoration(this.backgroundColor)

fun DocumentApi.toModel(): Document =
    Document(
        id = id,
        title = title,
        content = content?.associateBy { it.position }?.mapValues { (_, story) -> story.toModel() },
        createdAt = createdAt,
        lastUpdatedAt = lastUpdatedAt,
    )
