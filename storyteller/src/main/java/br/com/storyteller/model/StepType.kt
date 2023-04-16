package br.com.storyteller.model

enum class StepType(val type: String) {
    MESSAGE("message"),
    GROUP_MESSAGE("group_message"),
    IMAGE("image"),
    GROUP_IMAGE("group_image"),
    AUDIO("audio"),
    GROUP_VIDEO("group_video"),
    VIDEO("video"),
    ADD_BUTTON("add_button"),
}
