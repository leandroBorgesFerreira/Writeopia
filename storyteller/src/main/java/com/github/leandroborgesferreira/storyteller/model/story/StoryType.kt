package com.github.leandroborgesferreira.storyteller.model.story

/**
 * The default types of [StoryType]. You can add more types without extending this enum (which
 * is final), by creating new strings of types.
 */
enum class StoryType(val type: String) {
    MESSAGE("message"),
    MESSAGE_BOX("message_box"),
    IMAGE("image"),
    GROUP_IMAGE("group_image"),
    AUDIO("audio"),
    GROUP_VIDEO("group_video"),
    VIDEO("video"),
    SPACE("space"),
    LARGE_SPACE("large_space"),
    ADD_BUTTON("add_button"),
    CHECK_ITEM("check_item"),
    TITLE("title");

    companion object {
        fun fromStringType(stringValue: String) =
            StoryType.values().firstOrNull { it.name == stringValue }
    }
}
