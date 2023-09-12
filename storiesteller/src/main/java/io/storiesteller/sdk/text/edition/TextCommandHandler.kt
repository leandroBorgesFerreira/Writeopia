package io.storiesteller.sdk.text.edition

import io.storiesteller.sdk.model.command.Command
import io.storiesteller.sdk.model.command.WhereToFind
import io.storiesteller.sdk.models.story.StoryStep

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
