package io.storiesteller.sdk.serialization.json

import io.storiesteller.sdk.serialization.serializers.InstantSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

fun storiesTellerSerializersModule(): SerializersModule = SerializersModule {
    contextual(InstantSerializer)
}

val storiesTellerJson = Json { serializersModule = storiesTellerSerializersModule() }
