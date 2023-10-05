package io.writeopia.sdk.models.id

object GenerateId {
    fun generate(): String =
        buildList {
            repeat(10) {
                add(listOf(('0'..'9'), ('a'..'z'), ('A'..'Z')).flatten().random())
            }
        }.joinToString(separator = "")
}