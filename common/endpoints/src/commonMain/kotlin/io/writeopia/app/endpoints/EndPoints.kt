package io.writeopia.app.endpoints

object EndPoints {
    fun ollamaGenerate() = "generate"

    fun ollamaModels() = "tags"

    fun introNotes() = "document/intro"

    fun userNotes() = "document/user/{id}"

    fun proxyUserNotes(userId: String = "{userId}") = "proxy/document/user/$userId"

    fun userNotes(userId: String = "{userId}") = "document/user/$userId"
}
