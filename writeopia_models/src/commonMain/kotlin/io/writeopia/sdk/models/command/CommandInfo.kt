package io.writeopia.sdk.models.command

import io.writeopia.sdk.models.story.TagInfo

data class CommandInfo(
    val command: Command,
    val commandTrigger: CommandTrigger,
    val tags: Set<TagInfo> = emptySet(),
    val replaceTags: Boolean = false,
)
