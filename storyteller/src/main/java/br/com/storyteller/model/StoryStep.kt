package br.com.storyteller.model

data class StoryStep(
    val id: String,
    val type: String,
    val url: String? = null,
    val path: String? = null,
    val text: String? = null,
)

enum class StepType(val type: String) {
    MESSAGE("message"),
    IMAGE("image"),
    AUDIO("audio"),
    VIDEO("video"),
}
