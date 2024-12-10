package io.writeopia.ui.drawer.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.model.action.Action
import io.writeopia.ui.model.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.models.story.Tag
import io.writeopia.ui.drawer.SimpleTextDrawer
import io.writeopia.ui.drawer.StoryStepDrawer
import io.writeopia.ui.icons.WrSdkIcons
import io.writeopia.ui.manager.WriteopiaStateManager
import io.writeopia.ui.model.DrawConfig
import kotlinx.coroutines.flow.MutableStateFlow
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Draw a text that can be edited with a swipe effect to trigger edition.
 */
fun swipeTextDrawer(
    modifier: Modifier = Modifier,
    customBackgroundColor: Color = Color.Transparent,
    clickable: Boolean = true,
    dragIconWidth: Dp = 16.dp,
    config: DrawConfig,
    onSelected: (Boolean, Int) -> Unit = { _, _ -> },
    onDragHover: (Int) -> Unit,
    onDragStart: () -> Unit = {},
    onDragStop: () -> Unit = {},
    moveRequest: (Action.Move) -> Unit = {},
    messageDrawer: @Composable RowScope.() -> SimpleTextDrawer,
    isDesktop: Boolean,
    endContent: @Composable ((StoryStep, DrawInfo, Boolean) -> Unit)? = null,
): StoryStepDrawer =
    DesktopTextItemDrawer(
        modifier,
        customBackgroundColor,
        clickable,
        onSelected,
        dragIconWidth,
        config,
        onDragHover,
        onDragStart,
        onDragStop,
        moveRequest,
        null,
        endContent,
        isDesktop,
        messageDrawer
    )

@OptIn(ExperimentalComposeUiApi::class)
fun swipeTextDrawer(
    manager: WriteopiaStateManager,
    modifier: Modifier = Modifier,
    dragIconWidth: Dp = 16.dp,
    config: DrawConfig,
    isDesktop: Boolean,
    messageDrawer: @Composable RowScope.() -> SimpleTextDrawer
): StoryStepDrawer {
    return swipeTextDrawer(
        modifier = modifier,
        onSelected = manager::onSelected,
        dragIconWidth = dragIconWidth,
        config = config,
        onDragHover = manager::onDragHover,
        onDragStart = manager::onDragStart,
        onDragStop = manager::onDragStop,
        moveRequest = manager::moveRequest,
        customBackgroundColor = Color.Transparent,
        endContent = { storyStep, drawInfo, isHovered ->
            val isTitle = storyStep.tags.any { it.tag.isTitle() }
            val isCollapsed by lazy { storyStep.tags.any { it.tag == Tag.COLLAPSED } }
            if (isTitle && isHovered || isCollapsed) {
                val isCollapsed = storyStep.tags.any { it.tag == Tag.COLLAPSED }
                var active by remember { mutableStateOf(false) }
                val iconTintOnHover = MaterialTheme.colorScheme.onBackground
                val iconTint = Color(0xFFAAAAAA)

                val tintColor by derivedStateOf {
                    if (active) iconTintOnHover else iconTint
                }

                Icon(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .clickable(onClick = { manager.toggleCollapseItem(drawInfo.position) })
                        .onPointerEvent(PointerEventType.Enter) { active = true }
                        .onPointerEvent(PointerEventType.Exit) { active = false }
                        .size(24.dp)
                        .padding(4.dp),
                    imageVector = if (isCollapsed) {
                        WrSdkIcons.smallArrowUp
                    } else {
                        WrSdkIcons.smallArrowDown
                    },
                    contentDescription = "Small arrow right",
                    tint = tintColor
                )
            }
        },
        isDesktop = isDesktop,
        messageDrawer = {
            messageDrawer()
        }
    )
}

@Preview
@Composable
private fun SwipeMessageDrawerPreview() {
    swipeTextDrawer(
        onDragHover = {},
        isDesktop = true,
        config = DrawConfig(),
        messageDrawer = {
            TextDrawer(selectionState = MutableStateFlow(false), onSelectionLister = {})
        },
    ).Step(
        step = StoryStep(text = "Some text", type = StoryTypes.TEXT.type),
        drawInfo = DrawInfo(selectMode = true),
    )
}
