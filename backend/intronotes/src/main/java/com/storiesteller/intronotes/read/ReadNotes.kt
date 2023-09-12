package com.storiesteller.intronotes.read

import com.storiesteller.intronotes.extensions.toAPi
import com.storiesteller.intronotes.persistence.entity.DocumentEntity
import com.storiesteller.sdk.serialization.json.storyTellerJson
import com.storiesteller.sdk.serialization.request.wrapInRequest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal fun readNotes(
    json: Json = storyTellerJson,
    provideNote: () -> DocumentEntity,
): String =
    listOf(provideNote())
        .map { documentEntity ->
            documentEntity.toAPi()
        }.let { introNotes ->
            json.encodeToString(introNotes.wrapInRequest())
        }

