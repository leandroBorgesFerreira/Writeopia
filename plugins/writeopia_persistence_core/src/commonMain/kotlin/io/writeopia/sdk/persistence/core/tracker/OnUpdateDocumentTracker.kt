package io.writeopia.sdk.persistence.core.tracker

import io.writeopia.sdk.filter.DocumentFilter
import io.writeopia.sdk.filter.DocumentFilterObject
import io.writeopia.sdk.manager.DocumentTracker
import io.writeopia.sdk.manager.DocumentUpdate
import io.writeopia.sdk.model.document.DocumentInfo
import io.writeopia.sdk.model.story.LastEdit
import io.writeopia.sdk.model.story.StoryState
import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.id.GenerateId
import io.writeopia.sdk.models.story.StoryTypes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.datetime.Clock

class OnUpdateDocumentTracker(
    private val documentUpdate: DocumentUpdate,
    private val documentFilter: DocumentFilter = DocumentFilterObject
) : DocumentTracker {

    override suspend fun saveOnStoryChanges(
        documentEditionFlow: Flow<Pair<StoryState, DocumentInfo>>,
        userId: String
    ) {
        documentEditionFlow.collectLatest { (storyState, documentInfo) ->
            when (val lastEdit = storyState.lastEdit) {
                is LastEdit.LineEdition -> {
                    if (lastEdit.storyStep.ephemeral) return@collectLatest

                    documentUpdate.saveStoryStep(
                        storyStep = lastEdit.storyStep.copy(
                            localId = GenerateId.generate()
                        ),
                        position = lastEdit.position,
                        documentId = documentInfo.id,
                    )

                    val stories = storyState.stories
                    val titleFromContent = stories.values
                        .firstOrNull { storyStep ->
                            // Todo: Change the type of change to allow different types. The client code should decide what is a title
                            // It is also interesting to inv
                            storyStep.type == StoryTypes.TITLE.type
                        }?.text

                    documentUpdate.saveDocumentMetadata(
                        Document(
                            id = documentInfo.id,
                            title = titleFromContent ?: documentInfo.title,
                            createdAt = documentInfo.createdAt,
                            lastUpdatedAt = Clock.System.now(),
                            lastSyncedAt = documentInfo.lastSyncedAt,
                            userId = userId,
                            parentId = documentInfo.parentId,
                            icon = documentInfo.icon,
                            isLocked = documentInfo.isLocked
                        )
                    )
                }

                LastEdit.Nothing -> {}

                LastEdit.Whole -> {
                    val stories = storyState.stories.filter { (_, story) -> !story.ephemeral }
                    val titleFromContent = stories.values
                        .firstOrNull { storyStep ->
                            // Todo: Change the type of change to allow different types. The client code should decide what is a title
                            // It is also interesting to inv
                            storyStep.type == StoryTypes.TITLE.type
                        }?.text

                    val document = Document(
                        id = documentInfo.id,
                        title = titleFromContent ?: documentInfo.title,
                        content = documentFilter.removeTypesFromDocument(stories),
                        createdAt = documentInfo.createdAt,
                        lastUpdatedAt = Clock.System.now(),
                        lastSyncedAt = documentInfo.lastSyncedAt,
                        userId = userId,
                        parentId = documentInfo.parentId,
                        icon = documentInfo.icon,
                        isLocked = documentInfo.isLocked
                    )

                    documentUpdate.saveDocument(document)
                }

                is LastEdit.InfoEdition -> {
                    val stories = storyState.stories
                    val titleFromContent = stories.values.firstOrNull { storyStep ->
                        // Todo: Change the type of change to allow different types. The client code should decide what is a title
                        // It is also interesting to inv
                        storyStep.type == StoryTypes.TITLE.type
                    }?.text

                    documentUpdate.saveDocumentMetadata(
                        Document(
                            id = documentInfo.id,
                            title = titleFromContent ?: documentInfo.title,
                            createdAt = documentInfo.createdAt,
                            lastUpdatedAt = Clock.System.now(),
                            lastSyncedAt = documentInfo.lastSyncedAt,
                            userId = userId,
                            parentId = documentInfo.parentId,
                            icon = documentInfo.icon,
                            isLocked = documentInfo.isLocked
                        )
                    )

                    if (!lastEdit.storyStep.ephemeral) {
                        documentUpdate.saveStoryStep(
                            storyStep = lastEdit.storyStep,
                            position = lastEdit.position,
                            documentId = documentInfo.id
                        )
                    }
                }

                LastEdit.Metadata -> {
                    val stories = storyState.stories
                    val titleFromContent = stories.values.firstOrNull { storyStep ->
                        // Todo: Change the type of change to allow different types. The client code should decide what is a title
                        // It is also interesting to inv
                        storyStep.type == StoryTypes.TITLE.type
                    }?.text

                    documentUpdate.saveDocumentMetadata(
                        Document(
                            id = documentInfo.id,
                            title = titleFromContent ?: documentInfo.title,
                            createdAt = documentInfo.createdAt,
                            lastUpdatedAt = Clock.System.now(),
                            lastSyncedAt = documentInfo.lastSyncedAt,
                            userId = userId,
                            parentId = documentInfo.parentId,
                            icon = documentInfo.icon,
                            isLocked = documentInfo.isLocked,
                            favorite = documentInfo.isFavorite
                        )
                    )
                }
            }
        }
    }
}
