package io.writeopia.ui.drawer.decorations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.models.story.Tag
import io.writeopia.ui.model.DrawConfig

object DefaultTagDecoration : TagDecoration {

    @Composable
    override fun decorate(modifier: Modifier, tags: Set<Tag>, config: DrawConfig): Modifier =
        if (tags.contains(Tag.HIGH_LIGHT_BLOCK)) {
            modifier
                .background(config.selectedColor(), MaterialTheme.shapes.small)
                .padding(8.dp)
        } else {
            modifier
        }

    @Composable
    override fun background(defaultColor: Color, tags: Set<Tag>, config: DrawConfig): Color =
        if (tags.contains(Tag.HIGH_LIGHT_BLOCK)) config.selectedColor() else defaultColor
}
