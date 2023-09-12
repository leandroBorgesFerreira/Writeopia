package com.storiesteller.intronotes.input

import com.storiesteller.intronotes.extensions.toEntity
import com.storiesteller.intronotes.persistence.entity.DocumentEntity
import com.storiesteller.sdk.serialization.data.DocumentApi
import com.storiesteller.sdk.serialization.json.storiesTellerJson
import com.storiesteller.sdk.serialization.request.StoriesTellerRequest

object ParseInput {

    fun parse(input: String): DocumentEntity =
        storiesTellerJson.decodeFromString<StoriesTellerRequest<DocumentApi>>(input).data.toEntity()
}