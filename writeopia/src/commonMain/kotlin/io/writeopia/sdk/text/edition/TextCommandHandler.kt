package io.writeopia.sdk.text.edition

import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.sdk.model.action.Action
import io.writeopia.sdk.models.command.*
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes

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

    companion object {
        fun defaultCommands(manager: WriteopiaManager): TextCommandHandler =
            TextCommandHandler(
                mapOf(
                    CommandFactory.checkItem() to { _, position ->
                        manager.changeStoryType(
                            position,
                            StoryTypes.CHECK_ITEM.type,
                            CommandInfo(
                                CommandFactory.checkItem(),
                                CommandTrigger.WRITTEN
                            )
                        )
                    }, CommandFactory.unOrderedList() to { _, position ->
                        manager.changeStoryType(
                            position,
                            StoryTypes.UNORDERED_LIST_ITEM.type,
                            CommandInfo(
                                CommandFactory.unOrderedList(),
                                CommandTrigger.WRITTEN
                            )
                        )
                    },
                    CommandFactory.h1() to { _, position ->
                        manager.changeStoryType(
                            position,
                            StoryTypes.H1.type,
                            CommandInfo(
                                CommandFactory.h1(),
                                CommandTrigger.WRITTEN
                            )
                        )
                    },
                    CommandFactory.h2() to { _, position ->
                        manager.changeStoryType(
                            position,
                            StoryTypes.H2.type,
                            CommandInfo(
                                CommandFactory.h2(),
                                CommandTrigger.WRITTEN
                            )
                        )
                    },
                    CommandFactory.h3() to { _, position ->
                        manager.changeStoryType(
                            position,
                            StoryTypes.H3.type,
                            CommandInfo(
                                CommandFactory.h3(),
                                CommandTrigger.WRITTEN
                            )
                        )
                    },
                    CommandFactory.h4() to { _, position ->
                        manager.changeStoryType(
                            position,
                            StoryTypes.H4.type,
                            CommandInfo(
                                CommandFactory.h4(),
                                CommandTrigger.WRITTEN
                            )
                        )
                    }
                )
            )
    }
}
