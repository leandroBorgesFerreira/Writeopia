package io.writeopia.ui.edition

import io.writeopia.sdk.models.command.Command
import io.writeopia.sdk.models.command.CommandFactory
import io.writeopia.sdk.models.command.CommandInfo
import io.writeopia.sdk.models.command.CommandTrigger
import io.writeopia.sdk.models.command.TypeInfo
import io.writeopia.sdk.models.command.WhereToFind
import io.writeopia.sdk.models.story.Decoration
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.models.story.Tags
import io.writeopia.ui.manager.WriteopiaStateManager

class TextCommandHandler(private val commandsMap: Map<Command, (StoryStep, Int) -> Unit>) {

    fun handleCommand(text: String, step: StoryStep, position: Int): Boolean {
        // Todo(Leandro): Using a reverse index would improve the speed a lot.
        val command: Command = commandsMap.keys
            .firstOrNull { command ->
                when (command.whereToFind) {
                    WhereToFind.START -> text.startsWith(command.commandText)
                    WhereToFind.ANYWHERE -> text.contains(command.commandText)
                }
            } ?: return false

        commandsMap[command]!!.invoke(step.copy(text = text), position)

        return true
    }

    companion object {
        fun noCommands(): TextCommandHandler = TextCommandHandler(emptyMap())

        fun defaultCommands(manager: WriteopiaStateManager): TextCommandHandler {
            return TextCommandHandler(
                mapOf(
                    CommandFactory.checkItem() to { _, position ->
                        manager.changeStoryType(
                            position,
                            TypeInfo(StoryTypes.CHECK_ITEM.type),
                            CommandInfo(
                                CommandFactory.checkItem(),
                                CommandTrigger.WRITTEN
                            )
                        )
                    },
                    CommandFactory.unOrderedList() to { _, position ->
                        manager.changeStoryType(
                            position,
                            TypeInfo(StoryTypes.UNORDERED_LIST_ITEM.type),
                            CommandInfo(
                                CommandFactory.unOrderedList(),
                                CommandTrigger.WRITTEN
                            )
                        )
                    },
                    CommandFactory.h1() to { _, position ->
                        manager.changeStoryType(
                            position,
                            TypeInfo(
                                StoryTypes.TEXT.type,
                                Decoration(textSize = 28)
                            ),
                            CommandInfo(
                                CommandFactory.h1(),
                                CommandTrigger.WRITTEN,
                                tags = setOf(Tags.H1.tag)
                            )
                        )
                    },
                    CommandFactory.h2() to { _, position ->
                        manager.changeStoryType(
                            position,
                            TypeInfo(
                                StoryTypes.TEXT.type,
                                Decoration(textSize = 24)
                            ),
                            CommandInfo(
                                CommandFactory.h2(),
                                CommandTrigger.WRITTEN,
                                tags = setOf(Tags.H2.tag)
                            )
                        )
                    },
                    CommandFactory.h3() to { _, position ->
                        manager.changeStoryType(
                            position,
                            TypeInfo(
                                StoryTypes.TEXT.type,
                                Decoration(textSize = 20)
                            ),
                            CommandInfo(
                                CommandFactory.h3(),
                                CommandTrigger.WRITTEN,
                                tags = setOf(Tags.H3.tag)
                            )
                        )
                    },
                    CommandFactory.h4() to { _, position ->
                        manager.changeStoryType(
                            position,
                            TypeInfo(
                                StoryTypes.TEXT.type,
                                Decoration(textSize = 18)
                            ),
                            CommandInfo(
                                CommandFactory.h4(),
                                CommandTrigger.WRITTEN,
                                tags = setOf(Tags.H4.tag)
                            )
                        )
                    },
                    CommandFactory.codeBlock() to { _, position ->
                        manager.changeStoryType(
                            position,
                            TypeInfo(
                                StoryTypes.CODE_BLOCK.type,
                                Decoration(textSize = 16)
                            ),
                            CommandInfo(
                                CommandFactory.codeBlock(),
                                CommandTrigger.WRITTEN
                            )
                        )
                    }
                )
            )
        }
    }
}
