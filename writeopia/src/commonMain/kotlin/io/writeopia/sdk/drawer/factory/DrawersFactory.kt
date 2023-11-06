package io.writeopia.sdk.drawer.factory

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import io.writeopia.sdk.drawer.StoryStepDrawer
import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.sdk.text.edition.TextCommandHandler

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