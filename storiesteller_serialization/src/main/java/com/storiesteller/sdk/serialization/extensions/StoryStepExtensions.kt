package com.storiesteller.sdk.serialization.extensions

import com.storiesteller.sdk.models.document.Document
import com.storiesteller.sdk.models.story.Decoration
import com.storiesteller.sdk.models.story.StoryStep
import com.storiesteller.sdk.models.story.StoryType
import com.storiesteller.sdk.serialization.data.DecorationApi
import com.storiesteller.sdk.serialization.data.DocumentApi
import com.storiesteller.sdk.serialization.data.StoryStepApi
import com.storiesteller.sdk.serialization.data.StoryTypeApi

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

fun Document.toApi(): DocumentApi =
    DocumentApi(
        id = id,
        title = title,
        content = content.map { (position, story) -> story.toApi(position) },
        createdAt = createdAt,
        lastUpdatedAt = lastUpdatedAt,
        userId = userId,
    )

fun DocumentApi.toModel(): Document =
    Document(
        id = id,
        title = title,
        content = content.associateBy { it.position }.mapValues { (_, story) -> story.toModel() },
        createdAt = createdAt,
        lastUpdatedAt = lastUpdatedAt,
        userId = userId,
    )
