package com.github.leandroborgesferreira.storyteller.intronotes.read

import com.github.leandroborgesferreira.storyteller.intronotes.data.introNotes
import com.github.leandroborgesferreira.storyteller.intronotes.persistence.repository.IntroNotesRepository
import com.github.leandroborgesferreira.storyteller.serialization.json.storyTellerJson
import com.github.leandroborgesferreira.storyteller.serialization.request.wrapInRequest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal fun readNotes(
    json: Json = storyTellerJson,
    introNotesRepository: IntroNotesRepository
): String {
    val introNotes = introNotes()
    return json.encodeToString(introNotes().wrapInRequest())
}

