package com.github.leandroborgesferreira.storyteller.intronotes.input

import com.github.leandroborgesferreira.storyteller.intronotes.extensions.toEntity
import com.github.leandroborgesferreira.storyteller.intronotes.persistence.entity.DocumentEntity
import com.github.leandroborgesferreira.storyteller.serialization.data.DocumentApi
import com.github.leandroborgesferreira.storyteller.serialization.json.storyTellerJson
import com.github.leandroborgesferreira.storyteller.serialization.request.StoryTellerRequest

object ParseInput {

    fun parse(input: String): DocumentEntity =
        storyTellerJson.decodeFromString<StoryTellerRequest<DocumentApi>>(input).data.toEntity()
}