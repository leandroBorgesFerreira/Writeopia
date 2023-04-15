package br.com.storyteller.model

enum class StepType(val type: String) {
    MESSAGE("message"),
    IMAGE("image"),
    AUDIO("audio"),
    VIDEO("video"),
    ADD_BUTTON("add_button"),
}
