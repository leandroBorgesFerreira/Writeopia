package io.writeopia.ui.drawer.decorations

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.writeopia.sdk.models.story.Tag
import io.writeopia.ui.model.DrawConfig

interface TagDecoration {

    @Composable
    fun decorate(modifier: Modifier, tags: Set<Tag>, config: DrawConfig) : Modifier

    @Composable
    fun background(defaultColor: Color, tags: Set<Tag>, config: DrawConfig): Color
}
