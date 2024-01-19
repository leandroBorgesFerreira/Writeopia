package io.writeopia.documents_spring.api

import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.serialization.data.DocumentApi;
import io.writeopia.sdk.serialization.extensions.toApi
import kotlinx.datetime.Clock;

class DocumentsRepository {

    //Todo: Implement this instead of providing fake notes.
    fun userNotes(): List<DocumentApi> =
        buildList {
            repeat(3) { index ->
                val title = "Fake note $index"
                val now = Clock.System.now()

                val documentList = listOf(
                    StoryStep(type = StoryTypes.TITLE.type, text = title),
                    StoryStep(type = StoryTypes.TEXT.type, text = "Fake note $index"),
                )

                val documentApi = DocumentApi(
                    id = "welcomeNote",
                    title = title,
                    content = documentList.mapIndexed { i, storyStep -> storyStep.toApi(i) },
                    createdAt = now.toEpochMilliseconds(),
                    lastUpdatedAt = now.toEpochMilliseconds(),
                    userId = "null",
                )

                add(documentApi)
            }
        }
}