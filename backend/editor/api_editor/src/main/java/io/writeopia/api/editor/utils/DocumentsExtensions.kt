package io.writeopia.api.editor.utils

import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.serialization.data.DocumentApi
import io.writeopia.sdk.serialization.extensions.toApi
import kotlinx.datetime.Clock

fun DocumentApi.Companion.example(id: String = "document_123"): DocumentApi {
    val title = "Example Document"
    val now = Clock.System.now()

    val documentList = listOf(
        StoryStep(type = StoryTypes.TITLE.type, text = title),
        StoryStep(type = StoryTypes.TEXT.type, text = "sample message"),
    )

    return DocumentApi(
        id = id,
        title = title,
        content = documentList.mapIndexed { i, storyStep -> storyStep.toApi(i) },
        createdAt = now.toEpochMilliseconds(),
        lastUpdatedAt = now.toEpochMilliseconds(),
        userId = "user_123",
    )
}
