package io.writeopia.intronotes.persistence.repository

import io.writeopia.intronotes.dynamo.introNotesTable
import io.writeopia.intronotes.persistence.entity.DocumentEntity
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.Key

internal const val INTRO_NOTES_TABLE = "IntroNotes"

object DynamoIntroNotesRepository {
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

    fun readNote(
        id: String,
        notesTable: DynamoDbTable<DocumentEntity> = introNotesTable(),
    ): DocumentEntity {
        return notesTable.getItem(Key.builder().partitionValue(id).build())
    }

//    fun readAllNotes(
//        notesTable: DynamoDbTable<DocumentEntity> = introNotesTable()
//    ): List<DocumentEntity> {
//
//    }
}
