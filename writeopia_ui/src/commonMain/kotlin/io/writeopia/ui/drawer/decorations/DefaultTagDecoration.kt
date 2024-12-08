package io.writeopia.ui.drawer.decorations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.models.story.Tag
import io.writeopia.sdk.models.story.TagInfo
import io.writeopia.ui.model.DrawConfig

object DefaultTagDecoration : TagDecoration {

    @Composable
    override fun decorate(
        modifier: Modifier,
        tags: Iterable<TagInfo>,
        config: DrawConfig
    ): Modifier =
        if (tags.mapTo(mutableSetOf()) { it.tag }.contains(Tag.HIGH_LIGHT_BLOCK)) {
            modifier
                .background(config.selectedColor(), shareForTagInfo(tags))
                .padding(paddingForTagInfo(tags))
        } else {
            modifier
        }

    @Composable
    override fun background(defaultColor: Color, tags: Iterable<Tag>, config: DrawConfig): Color =
        if (tags.contains(Tag.HIGH_LIGHT_BLOCK)) config.selectedColor() else defaultColor

    private fun shareForTagInfo(tagInfoList: Iterable<TagInfo>): Shape {
        val corner = 8.dp

        val tagInfo = tagInfoList.firstOrNull { info ->
            info.tag.hasPosition()
        } ?: return RoundedCornerShape(corner)

        return when (tagInfo.position) {
            -1 -> RoundedCornerShape(topStart = corner, topEnd = corner)
            1 -> RoundedCornerShape(bottomStart = corner, bottomEnd = corner)
            2 -> RoundedCornerShape(corner)
            else -> RoundedCornerShape(0)
        }
    }

    private fun paddingForTagInfo(tagInfoList: Iterable<TagInfo>): PaddingValues {
        val padding = 8.dp

        val tagInfo = tagInfoList.firstOrNull { info ->
            info.tag.hasPosition()
        } ?: return PaddingValues(0.dp)

        return when (tagInfo.position) {
            -1 -> PaddingValues(top = padding)
            1 -> PaddingValues(bottom = padding)
            2 -> PaddingValues(padding)
            else -> PaddingValues(0.dp)
        }
    }
}
