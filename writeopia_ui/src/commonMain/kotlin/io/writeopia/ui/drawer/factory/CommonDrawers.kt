package io.writeopia.ui.drawer.factory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.ui.drawer.StoryStepDrawer
import io.writeopia.ui.drawer.content.AddButtonDrawer
import io.writeopia.ui.drawer.content.LastEmptySpace
import io.writeopia.ui.drawer.content.SpaceDrawer
import io.writeopia.ui.drawer.content.TextDrawer
import io.writeopia.ui.drawer.content.checkItemDrawer
import io.writeopia.ui.drawer.content.headerDrawer
import io.writeopia.ui.drawer.content.swipeTextDrawer
import io.writeopia.ui.drawer.content.unOrderedListItemDrawer
import io.writeopia.ui.manager.WriteopiaStateManager
import io.writeopia.ui.model.DrawConfig
import io.writeopia.ui.model.EmptyErase
import io.writeopia.ui.utils.codeBlockStyle
import io.writeopia.ui.utils.defaultTextStyle

private const val DRAG_ICON_WIDTH = 24

object CommonDrawers {

    @Composable
    fun create(
        manager: WriteopiaStateManager,
        marginAtBottom: Dp,
        defaultBorder: Shape = MaterialTheme.shapes.medium,
        editable: Boolean = false,
        groupsBackgroundColor: Color = Color.Transparent,
        onHeaderClick: () -> Unit = {},
        dragIconWidth: Dp = DRAG_ICON_WIDTH.dp,
        lineBreakByContent: Boolean,
        drawConfig: DrawConfig = DrawConfig(),
        eventListener: (KeyEvent, TextFieldValue, StoryStep, Int, EmptyErase, Int, EndOfText) -> Boolean,
        isDesktop: Boolean,
    ): Map<Int, StoryStepDrawer> {
        val textBoxDrawer = swipeTextDrawer(
            modifier = Modifier
                .clip(shape = defaultBorder)
                .background(groupsBackgroundColor),
            dragIconWidth = DRAG_ICON_WIDTH.dp,
            onDragHover = manager::onDragHover,
            onDragStart = manager::onDragStart,
            onDragStop = manager::onDragStop,
            onSelected = manager::onSelected,
            isDesktop = isDesktop,
            messageDrawer = {
                messageDrawer(
                    manager = manager,
                    modifier = Modifier.padding(
                        start = drawConfig.codeBlockStartPadding.dp,
                        top = drawConfig.textVerticalPadding.dp,
                        bottom = drawConfig.textVerticalPadding.dp,
                    ),
                    eventListener = eventListener,
                    allowLineBreaks = true,
                    lineBreakByContent = lineBreakByContent,
                    emptyErase = EmptyErase.CHANGE_TYPE,
                    onSelectionLister = manager::toggleSelection,
                )
            }
        )

        val codeBlockDrawer = swipeTextDrawer(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium),
            dragIconWidth = dragIconWidth,
            onDragHover = manager::onDragHover,
            onSelected = manager::onSelected,
            isDesktop = isDesktop,
            messageDrawer = {
                messageDrawer(
                    manager = manager,
                    modifier = Modifier.padding(
                        start = drawConfig.codeBlockStartPadding.dp,
                        top = drawConfig.textVerticalPadding.dp,
                        bottom = drawConfig.textVerticalPadding.dp
                    )
                        .clip(MaterialTheme.shapes.large)
                        .background(Color.Gray)
                        .padding(
                            horizontal = drawConfig.codeBlockHorizontalInnerPadding.dp,
                            vertical = drawConfig.codeBlockVerticalInnerPadding.dp
                        ),
                    eventListener = eventListener,
                    textStyle = { codeBlockStyle() },
                    allowLineBreaks = true,
                    lineBreakByContent = lineBreakByContent,
                    emptyErase = EmptyErase.CHANGE_TYPE,
                    onSelectionLister = manager::toggleSelection,
                )
            }
        )

        val swipeTextDrawer = swipeTextDrawer(
            manager = manager,
            dragIconWidth = dragIconWidth,
            isDesktop = isDesktop,
        ) {
            messageDrawer(
                manager = manager,
                modifier = Modifier.padding(
                    start = drawConfig.codeBlockStartPadding.dp,
                    top = drawConfig.textVerticalPadding.dp,
                    bottom = drawConfig.textVerticalPadding.dp
                ),
                eventListener = eventListener,
                lineBreakByContent = lineBreakByContent,
                emptyErase = EmptyErase.DELETE,
                onSelectionLister = manager::toggleSelection,
            )
        }

        val checkItemDrawer = checkItemDrawer(
            manager = manager,
            isDesktop = isDesktop,
            modifier = Modifier.padding(vertical = drawConfig.checkBoxItemVerticalPadding.dp),
            dragIconWidth = dragIconWidth,
            checkBoxPadding = PaddingValues(
                start = drawConfig.checkBoxStartPadding.dp,
                end = drawConfig.checkBoxEndPadding.dp
            )
        ) {
            messageDrawer(
                manager,
                eventListener = eventListener,
                emptyErase = EmptyErase.CHANGE_TYPE,
                lineBreakByContent = lineBreakByContent,
                onSelectionLister = manager::toggleSelection,
            )
        }

        val unOrderedListItemDrawer =
            unOrderedListItemDrawer(
                manager = manager,
                isDesktop = isDesktop,
                dragIconWidth = dragIconWidth,
                checkBoxPadding = PaddingValues(
                    start = drawConfig.listItemStartPadding.dp,
                    end = drawConfig.listItemEndPadding.dp
                )
            ) {
                messageDrawer(
                    manager,
                    eventListener = eventListener,
                    emptyErase = EmptyErase.CHANGE_TYPE,
                    lineBreakByContent = lineBreakByContent,
                    onSelectionLister = manager::toggleSelection,
                )
            }

        val headerDrawer = headerDrawer(
            manager = manager,
            modifier = Modifier.clip(RoundedCornerShape(0.dp, 0.dp, 8.dp, 8.dp)),
            headerClick = onHeaderClick,
            onKeyEvent = eventListener,
            lineBreakByContent = lineBreakByContent,
            selectionState = manager.selectionState,
            drawConfig = drawConfig,
        )

        return buildMap {
            put(StoryTypes.TEXT_BOX.type.number, textBoxDrawer)
            put(StoryTypes.TEXT.type.number, swipeTextDrawer)
            put(StoryTypes.ADD_BUTTON.type.number, AddButtonDrawer())
            put(StoryTypes.SPACE.type.number, SpaceDrawer(manager::moveRequest))
            put(StoryTypes.ON_DRAG_SPACE.type.number, SpaceDrawer(manager::moveRequest, Color.Gray))
            put(
                StoryTypes.LAST_SPACE.type.number,
                LastEmptySpace(
                    moveRequest = manager::moveRequest,
                    click = manager::clickAtTheEnd,
                    height = marginAtBottom
                )
            )
            put(StoryTypes.CHECK_ITEM.type.number, checkItemDrawer)
            put(StoryTypes.UNORDERED_LIST_ITEM.type.number, unOrderedListItemDrawer)
            put(StoryTypes.TITLE.type.number, headerDrawer)
            put(StoryTypes.CODE_BLOCK.type.number, codeBlockDrawer)
        }
    }

    @Composable
    private fun RowScope.messageDrawer(
        manager: WriteopiaStateManager,
        modifier: Modifier = Modifier,
        textStyle: @Composable (StoryStep) -> TextStyle = { defaultTextStyle(it) },
        allowLineBreaks: Boolean = false,
        lineBreakByContent: Boolean,
        emptyErase: EmptyErase,
        eventListener: (KeyEvent, TextFieldValue, StoryStep, Int, EmptyErase, Int, EndOfText) -> Boolean,
        onSelectionLister: (Int) -> Unit
    ): TextDrawer {
        return TextDrawer(
            modifier = modifier.weight(1F),
            onKeyEvent = eventListener,
            onTextEdit = manager::handleTextInput,
            textStyle = textStyle,
            onFocusChanged = { position, focus ->
                manager.onFocusChange(position, focus.isFocused)
            },
            allowLineBreaks = allowLineBreaks,
            lineBreakByContent = lineBreakByContent,
            emptyErase = emptyErase,
            selectionState = manager.selectionState,
            onSelectionLister = onSelectionLister
        )
    }
}
