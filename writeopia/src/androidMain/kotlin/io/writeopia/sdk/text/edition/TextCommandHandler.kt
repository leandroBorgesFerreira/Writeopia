package io.writeopia.sdk.text.edition

import io.writeopia.sdk.models.command.Command
import io.writeopia.sdk.models.command.WhereToFind
import io.writeopia.sdk.models.story.StoryStep

class TextCommandHandler(private val commandsMap: Map<Command, (StoryStep, Int) -> Unit>) {

    fun handleCommand(text: String, step: StoryStep, position: Int): Boolean {
        //Todo(Leandro): Using a reverse index would improve the speed a lot.
        val command: Command = commandsMap.keys
            .firstOrNull { command ->
                when (command.whereToFind) {
                    WhereToFind.START -> text.startsWith(command.commandText)
                    WhereToFind.ANYWHERE -> text.contains(command.commandText)
                }
            }
            ?: return false

        commandsMap[command]!!.invoke(step.copy(text = text), position)

        return true
    }
}
