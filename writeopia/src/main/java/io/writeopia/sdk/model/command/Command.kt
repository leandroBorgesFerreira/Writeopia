package io.writeopia.sdk.model.command

data class Command(val commandText: String, val whereToFind: WhereToFind)

enum class WhereToFind {
    START, ANYWHERE
}

object CommandFactory {
    fun lineBreak() = Command("\n", WhereToFind.ANYWHERE)

    fun checkItem() = Command("-[] ", WhereToFind.START)

    fun unOrderedList() = Command("- ", WhereToFind.START)

    fun h1() = Command("# ", WhereToFind.START)
    fun h2() = Command("## ", WhereToFind.START)
    fun h3() = Command("### ", WhereToFind.START)
    fun h4() = Command("#### ", WhereToFind.START)
}
