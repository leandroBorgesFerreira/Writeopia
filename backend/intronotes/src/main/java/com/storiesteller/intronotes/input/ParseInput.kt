package com.storiesteller.intronotes.input

import com.storiesteller.intronotes.extensions.toEntity
import com.storiesteller.intronotes.persistence.entity.DocumentEntity
import com.storiesteller.sdk.serialization.data.DocumentApi
import com.storiesteller.sdk.serialization.json.storyTellerJson
import com.storiesteller.sdk.serialization.request.StoryTellerRequest

object ParseInput {

    fun parse(input: String): DocumentEntity =
        storyTellerJson.decodeFromString<StoryTellerRequest<DocumentApi>>(input).data.toEntity()
}