package com.github.leandroborgesferreira.storyteller.intronotes.persistence.repository

import com.github.leandroborgesferreira.storyteller.intronotes.dynamo.introNotesTable
import com.github.leandroborgesferreira.storyteller.intronotes.persistence.entity.DocumentEntity
import com.github.leandroborgesferreira.storyteller.intronotes.persistence.entity.StoryStepEntity
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable

internal const val INTRO_NOTES_TABLE = "IntroNotes"

object IntroNotesRepository {
    fun saveNotes(
        notes: List<DocumentEntity>,
        notesTable: DynamoDbTable<DocumentEntity> = introNotesTable()
    ) {
        notes.forEach { storyStepEntity ->
            notesTable.putItem(storyStepEntity)
        }
    }

    fun saveNote(
        note: DocumentEntity,
        notesTable: DynamoDbTable<DocumentEntity> = introNotesTable()
    ) {
        notesTable.putItem(note)
    }
}
