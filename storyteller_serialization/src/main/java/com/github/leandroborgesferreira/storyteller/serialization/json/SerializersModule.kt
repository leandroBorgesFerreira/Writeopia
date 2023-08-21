package com.github.leandroborgesferreira.storyteller.serialization.json

import com.github.leandroborgesferreira.storyteller.serialization.serializers.InstantSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

fun storyTellerSerializersModule(): SerializersModule = SerializersModule {
    contextual(InstantSerializer)
}

val storyTellerJson = Json { serializersModule = storyTellerSerializersModule() }
