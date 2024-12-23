package io.writeopia.ui.drawer.factory

import android.view.KeyEvent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.drawer.content.VideoDrawer
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.models.story.Tag
import io.writeopia.ui.drawer.StoryStepDrawer
import io.writeopia.ui.drawer.content.ImageDrawer
import io.writeopia.ui.drawer.content.RowGroupDrawer
import io.writeopia.ui.drawer.content.defaultImageShape
import io.writeopia.ui.icons.WrSdkIcons
import io.writeopia.ui.manager.WriteopiaStateManager
import io.writeopia.ui.model.DrawConfig

object DefaultDrawersAndroid : DrawersFactory {

    @Composable
    override fun create(
        manager: WriteopiaStateManager,
        defaultBorder: Shape,
        editable: Boolean,
        groupsBackgroundColor: Color,
        onHeaderClick: () -> Unit,
        selectedColor: Color,
        selectedBorderColor: Color,
        fontFamily: FontFamily?,
    ): Map<Int, StoryStepDrawer> {
        val imageDrawer = ImageDrawer(
            containerModifier = Modifier::defaultImageShape,
            mergeRequest = manager::mergeRequest
        )

        val imageDrawerInGroup = ImageDrawer(
            containerModifier = Modifier::defaultImageShape,
            mergeRequest = manager::mergeRequest
        )

        val commonDrawers = CommonDrawers.create(
            manager,
            marginAtBottom = 500.dp,
            defaultBorder,
            editable,
            onHeaderClick,
            lineBreakByContent = true,
            drawConfig = DrawConfig(
                textDrawerStartPadding = 2,
                textVerticalPadding = 4,
                codeBlockStartPadding = 0,
                checkBoxStartPadding = 0,
                checkBoxEndPadding = 0,
                checkBoxItemVerticalPadding = 0,
                listItemStartPadding = 8,
                selectedColor = { selectedColor },
                selectedBorderColor = { selectedBorderColor }
            ),
            isDesktop = false,
            eventListener = KeyEventListenerFactory.android(
                manager,
                isEmptyErase = DefaultDrawersAndroid::emptyErase
            ),
            fontFamily = fontFamily,
            headerEndContent = { storyStep, drawInfo, _ ->
                val isTitle = storyStep.tags.any { it.tag.isTitle() }
                val isCollapsed by lazy { storyStep.tags.any { it.tag == Tag.COLLAPSED } }
                if (isTitle) {
                    val iconTintOnHover = MaterialTheme.colorScheme.onBackground

                    Icon(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .clickable(onClick = { manager.toggleCollapseItem(drawInfo.position) })
                            .size(24.dp)
                            .padding(4.dp),
                        imageVector = if (isCollapsed) {
                            WrSdkIcons.smallArrowUp
                        } else {
                            WrSdkIcons.smallArrowDown
                        },
                        contentDescription = "Small arrow right",
                        tint = iconTintOnHover
                    )
                }
            }
        )

        return mapOf(
            StoryTypes.VIDEO.type.number to VideoDrawer(),
            StoryTypes.IMAGE.type.number to imageDrawer,
            StoryTypes.GROUP_IMAGE.type.number to RowGroupDrawer(imageDrawerInGroup)
        ) + commonDrawers
    }

    private fun emptyErase(
        keyEvent: androidx.compose.ui.input.key.KeyEvent,
        input: TextFieldValue
    ): Boolean =
        keyEvent.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DEL && input.selection.start == 0
}
