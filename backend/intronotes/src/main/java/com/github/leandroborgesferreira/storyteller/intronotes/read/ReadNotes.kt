package com.github.leandroborgesferreira.storyteller.intronotes.read

import com.github.leandroborgesferreira.storyteller.intronotes.extensions.toAPi
import com.github.leandroborgesferreira.storyteller.intronotes.persistence.repository.IntroNotesRepository
import com.github.leandroborgesferreira.storyteller.serialization.json.storyTellerJson
import com.github.leandroborgesferreira.storyteller.serialization.request.wrapInRequest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal fun readNotes(
    json: Json = storyTellerJson,
    introNotesRepository: IntroNotesRepository
): String {

    val introNotes = listOf(
        introNotesRepository.readNote("87eb231f-c263-4562-9d86-ce980bf954a7")
    ).map { documentEntity ->
        documentEntity.toAPi()
    }

    return json.encodeToString(introNotes.wrapInRequest())
}

