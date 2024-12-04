package io.writeopia.sdk.import.markdown

import io.writeopia.sdk.models.command.Command
import io.writeopia.sdk.models.command.CommandFactory

class CharactersReverseIndexParser(
    commandList: List<Command> = CommandFactory.defaultCommands().toList()
) {

    private val reversedIndex: Map<Char, List<CommandIndex>> = createReverseIndexOfCommand(commandList)
}

fun createReverseIndexOfCommand(commandList: List<Command>): Map<Char, List<CommandIndex>> {
    return commandList.map { command -> commandToIndexes(command) }
        .reduce { acc, map ->
            buildMap {
                putAll(acc)

                map.map { (char, commandIndexList) ->
                    val indexes = get(char) ?: emptyList()
                    put(char, indexes + commandIndexList)
                }
            }
        }
}

private fun commandToIndexes(command: Command): Map<Char, List<CommandIndex>> {
    val resultMap = mutableMapOf<Char, List<CommandIndex>>()

    command.commandText.forEachIndexed { index, char ->
        val indexList = resultMap[char] ?: emptyList()
        val commandIndex = CommandIndex(commandId = command.commandId, position = index)

        resultMap[char] = indexList + commandIndex
    }

    return resultMap
}

/**
 * The command index is the index that each character should be mapped to. Each character can be in
 * many commands in a single position. So each character should have a list of [CommandIndex]
 */
data class CommandIndex(
    val commandId: Int,
    val position: Int
)
