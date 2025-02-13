package io.writeopia.editor.configuration.ui

import io.writeopia.ui.model.DrawConfig

expect object DrawConfigFactory {
    fun getDrawConfig(): DrawConfig
}
