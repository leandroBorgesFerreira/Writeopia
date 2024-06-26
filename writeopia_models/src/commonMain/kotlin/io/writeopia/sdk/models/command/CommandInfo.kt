package io.writeopia.sdk.models.command

data class CommandInfo(
    val command: Command,
    val commandTrigger: CommandTrigger,
    val tags: Set<String> = emptySet()
)
