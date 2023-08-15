package com.github.leandroborgesferreira.storyteller.model.story

/**
 * The default types of [StoryTypes]. You can add more types without extending this enum (which
 * is final), by creating new strings of types.
 */
enum class StoryTypes(val type: StoryType) {
    MESSAGE(StoryType("message", 0)),
    MESSAGE_BOX(StoryType("message_box", 1)),
    IMAGE(StoryType("image", 2)),
    GROUP_IMAGE(StoryType("group_image", 3)),
    AUDIO(StoryType("audio", 4)),
    GROUP_VIDEO(StoryType("group_video", 5)),
    VIDEO(StoryType("video", 6)),
    SPACE(StoryType("space", 7)),
    LARGE_SPACE(StoryType("large_space", 8)),
    ADD_BUTTON(StoryType("add_button", 9)),
    CHECK_ITEM(StoryType("check_item", 10)),
    TITLE(StoryType("title", 11));

    companion object {
        fun fromName(stringValue: String): StoryTypes =
            StoryTypes.values().first { it.type.name == stringValue }
    }
}
