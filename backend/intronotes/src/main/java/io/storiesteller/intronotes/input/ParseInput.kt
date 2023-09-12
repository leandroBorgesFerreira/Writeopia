package io.storiesteller.intronotes.input

import io.storiesteller.intronotes.extensions.toEntity
import io.storiesteller.intronotes.persistence.entity.DocumentEntity
import io.storiesteller.sdk.serialization.data.DocumentApi
import io.storiesteller.sdk.serialization.json.storiesTellerJson
import io.storiesteller.sdk.serialization.request.StoriesTellerRequest

object ParseInput {

    fun parse(input: String): DocumentEntity =
        storiesTellerJson.decodeFromString<StoriesTellerRequest<DocumentApi>>(input).data.toEntity()
}