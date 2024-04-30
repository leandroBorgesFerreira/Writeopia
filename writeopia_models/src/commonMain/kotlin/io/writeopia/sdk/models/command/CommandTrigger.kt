package io.writeopia.sdk.models.command

/**
 * [WRITTEN] are commands what were called by an input in the keyboard line typing -[] or ###.
 * [CLICKED] are commands called by a button in the UI of an app.
 */
enum class CommandTrigger {
    WRITTEN,
    CLICKED
}
