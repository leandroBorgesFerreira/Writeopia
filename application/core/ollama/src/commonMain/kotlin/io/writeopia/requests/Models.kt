package io.writeopia.requests

import kotlinx.serialization.Serializable

@Serializable
data class ModelsResponse(val models: List<Model>)

@Serializable
data class Model(
    val name: String,
    val model: String,
    val modified_at: String? = null,
    val size: Long? = null,
    val digest: String? = null,
    val details: Details? = null
)

@Serializable
data class Details(
    val parentModel: String? = null, // nullable field for parent_model
    val format: String? = null, // must be "gguf" according to the JSON data
    val family: String? = null, // list of family names
    val families: List<String>? = listOf(), // list of family names
    val parameterSize: String? = null,
    val quantizationLevel: String? = null
)
