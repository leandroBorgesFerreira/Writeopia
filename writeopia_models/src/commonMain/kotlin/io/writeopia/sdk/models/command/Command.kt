package io.writeopia.sdk.models.command

data class Command(val commandId: Int, val commandText: String, val whereToFind: WhereToFind)

enum class WhereToFind {
    START, ANYWHERE
}

object CommandFactory {
    fun checkItem() = Command(1, "-[] ", WhereToFind.START)

    fun unOrderedList() = Command(2, "- ", WhereToFind.START)

    fun h1() = Command(3, "# ", WhereToFind.START)
    fun h2() = Command(4, "## ", WhereToFind.START)
    fun h3() = Command(5, "### ", WhereToFind.START)
    fun h4() = Command(6, "#### ", WhereToFind.START)

    fun codeBlock() = Command(7, "``` ", WhereToFind.START)

    fun defaultCommands(): Set<Command> =
        setOf(checkItem(), unOrderedList(), h1(), h2(), h3(), h4(), codeBlock())
}
