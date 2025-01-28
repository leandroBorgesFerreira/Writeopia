package io.writeopia.requests

import kotlinx.serialization.Serializable

@Serializable
data class OllamaGenerateRequest(val model: String, val prompt: String, val stream: Boolean)
