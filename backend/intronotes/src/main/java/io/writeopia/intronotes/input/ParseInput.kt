package io.writeopia.intronotes.input

import io.writeopia.intronotes.extensions.toEntity
import io.writeopia.intronotes.persistence.entity.DocumentEntity
import io.writeopia.sdk.serialization.data.DocumentApi
import io.writeopia.sdk.serialization.json.writeopiaJson
import io.writeopia.sdk.serialization.request.WriteopiaRequest

object ParseInput {

    fun parse(input: String): DocumentEntity =
        writeopiaJson.decodeFromString<WriteopiaRequest<DocumentApi>>(input).data.toEntity()
}