package io.writeopia.sdk.models.story

class TagInfo(val tag: Tag)

enum class Tag(val tag: String, var position: Int = 0) {
    H1("H1"),
    H2("H2"),
    H3("H3"),
    H4("H4"),
    HIGH_LIGHT_BLOCK("HIGH_LIGHT_BLOCK", 0);

    fun isTitle() =
        when (this) {
            H1, H2, H3, H4 -> true
            HIGH_LIGHT_BLOCK -> false
        }

    fun hasPosition() =
        when (this) {
            H1, H2, H3, H4 -> false
            HIGH_LIGHT_BLOCK -> true
        }

    companion object {
        fun titleTags(): Set<Tag> = setOf(H1, H2, H3, H4)

        fun fromString(fromTag: String): Tag? = entries.firstOrNull { it.tag == fromTag }
    }
}
