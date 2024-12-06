package io.writeopia.sdk.models.story

data class TagInfo(val tag: Tag, val position: Int = 0)

enum class Tag(val label: String) {
    H1("H1"),
    H2("H2"),
    H3("H3"),
    H4("H4"),
    HIGH_LIGHT_BLOCK("HIGH_LIGHT_BLOCK");

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

        fun fromString(fromTag: String): Tag? = entries.firstOrNull { it.label == fromTag }
    }
}
