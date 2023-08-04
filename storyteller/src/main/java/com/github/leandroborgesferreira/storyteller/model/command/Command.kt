package com.github.leandroborgesferreira.storyteller.model.command

data class Command(val commandText: String, val whereToFind: WhereToFind)

enum class WhereToFind {
    START, ANYWHERE
}

object CommandFactory {
    fun lineBreak() = Command("\n", WhereToFind.ANYWHERE)

    fun checkItem() = Command("-[]", WhereToFind.START)
}