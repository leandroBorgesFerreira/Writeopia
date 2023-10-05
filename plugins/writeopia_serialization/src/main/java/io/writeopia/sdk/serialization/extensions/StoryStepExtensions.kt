package io.writeopia.sdk.serialization.extensions

import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.id.GenerateId
import io.writeopia.sdk.models.story.Decoration
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryType
import io.writeopia.sdk.serialization.data.DecorationApi
import io.writeopia.sdk.serialization.data.DocumentApi
import io.writeopia.sdk.serialization.data.StoryStepApi
import io.writeopia.sdk.serialization.data.StoryTypeApi

fun StoryStep.toApi(position: Int): StoryStepApi =
    StoryStepApi(
        id = this.id,
        type = this.type.toApi(),
        parentId = this.parentId,
        url = this.url,
        path = this.path,
        text = this.text,
        checked = this.checked,
        steps = this.steps.map { storyStep -> storyStep.toApi(position) },
        decoration = this.decoration.toApi(),
        position = position
    )

fun StoryStepApi.toModel(): StoryStep =
    StoryStep(
        id = id,
        localId = GenerateId.generate(),
        type = type.toModel(),
        parentId = parentId,
        url = url,
        path = path,
        text = text,
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
