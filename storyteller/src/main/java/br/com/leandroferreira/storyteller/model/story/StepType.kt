package br.com.leandroferreira.storyteller.model.story

/**
 * The default types of [StepType]. You can add more types without extending this enum (which
 * is final), by creating new strings of types.
 */
enum class StepType(val type: String) {
    MESSAGE("message"),
    MESSAGE_BOX("message_box"),
    IMAGE("image"),
    GROUP_IMAGE("group_image"),
    AUDIO("audio"),
    GROUP_VIDEO("group_video"),
    VIDEO("video"),
    SPACE("space"),
    ADD_BUTTON("add_button"),
    CHECK_ITEM("check_item"),
}
