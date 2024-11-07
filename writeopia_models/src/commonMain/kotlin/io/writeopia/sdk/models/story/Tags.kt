package io.writeopia.sdk.models.story

enum class Tags(val tag: String) {
    H1("H1"),
    H2("H2"),
    H3("H3"),
    H4("H4");

    fun isTitle() =
        when (this) {
            H1 -> true
            H2 -> true
            H3 -> true
            H4 -> true
        }

    companion object {
        fun titleTags(): Set<Tags> = setOf(H1, H2, H3, H4)

        fun fromString(fromTag: String): Tags = entries.first { it.tag == fromTag }
    }
}
