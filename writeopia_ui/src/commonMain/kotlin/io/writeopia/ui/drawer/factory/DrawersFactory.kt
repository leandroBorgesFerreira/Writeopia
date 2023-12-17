package io.writeopia.ui.drawer.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.sdk.text.edition.TextCommandHandler
import io.writeopia.ui.drawer.StoryStepDrawer

interface DrawersFactory {

    @Composable
    fun create(
        manager: WriteopiaManager,
        defaultBorder: Shape,
        editable: Boolean,
        groupsBackgroundColor: Color,
        onHeaderClick: () -> Unit,
        textCommandHandler: TextCommandHandler,
    ): Map<Int, StoryStepDrawer>
}