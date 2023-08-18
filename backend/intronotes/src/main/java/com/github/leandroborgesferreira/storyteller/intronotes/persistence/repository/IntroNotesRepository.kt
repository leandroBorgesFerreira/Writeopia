package com.github.leandroborgesferreira.storyteller.intronotes.persistence.repository

import com.github.leandroborgesferreira.storyteller.intronotes.dynamo.introNotesTable
import com.github.leandroborgesferreira.storyteller.intronotes.persistence.entity.StoryStepEntity
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable

internal const val INTRO_NOTES_TABLE = "IntroNotes"

fun saveNotes(
    notes: List<StoryStepEntity>,
    notesTable: DynamoDbTable<StoryStepEntity> = introNotesTable
) {
    notes.forEach { storyStepEntity ->
        notesTable.putItem(storyStepEntity)
    }
}
