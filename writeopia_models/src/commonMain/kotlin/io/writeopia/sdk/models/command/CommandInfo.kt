package io.writeopia.sdk.models.command

import io.writeopia.sdk.models.story.Tag

data class CommandInfo(
    val command: Command,
    val commandTrigger: CommandTrigger,
    val tags: Set<Tag> = emptySet()
)
