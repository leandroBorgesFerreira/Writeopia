package io.writeopia.sdk.models.story

enum class Tags(val tag: String) {
    H1("H1"),
    H2("H2"),
    H3("H3"),
    H4("H4");

    companion object {
        fun titleTags(): Set<Tags> = setOf(H1, H2, H3, H4)
    }
}
