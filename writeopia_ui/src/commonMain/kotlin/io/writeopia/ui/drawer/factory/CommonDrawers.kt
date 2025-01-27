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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.ui.drawer.SimpleTextDrawer
import io.writeopia.ui.drawer.StoryStepDrawer
import io.writeopia.ui.drawer.content.AddButtonDrawer
import io.writeopia.ui.drawer.content.ImageDrawer
import io.writeopia.ui.drawer.content.LastEmptySpace
import io.writeopia.ui.drawer.content.LoadingDrawer
import io.writeopia.ui.drawer.content.RowGroupDrawer
import io.writeopia.ui.drawer.content.SpaceDrawer
import io.writeopia.ui.drawer.content.TextDrawer
import io.writeopia.ui.drawer.content.checkItemDrawer
import io.writeopia.ui.drawer.content.defaultImageShape
import io.writeopia.ui.drawer.content.headerDrawer
import io.writeopia.ui.drawer.content.swipeTextDrawer
import io.writeopia.ui.drawer.content.unOrderedListItemDrawer
import io.writeopia.ui.manager.WriteopiaStateManager
import io.writeopia.ui.model.DrawConfig
import io.writeopia.ui.model.DrawInfo
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
        editable: Boolean = true,
        onHeaderClick: () -> Unit = {},
        dragIconWidth: Dp = DRAG_ICON_WIDTH.dp,
        lineBreakByContent: Boolean,
        drawConfig: DrawConfig = DrawConfig(),
        eventListener: (KeyEvent, TextFieldValue, StoryStep, Int, EmptyErase, Int, EndOfText) -> Boolean,
        isDesktop: Boolean,
        fontFamily: FontFamily? = null,
        headerEndContent: @Composable ((StoryStep, DrawInfo, Boolean) -> Unit)? = null,
    ): Map<Int, StoryStepDrawer> {
        val commonTextModifier = Modifier.padding(
            start = drawConfig.codeBlockStartPadding.dp,
            top = drawConfig.textVerticalPadding.dp,
            bottom = drawConfig.textVerticalPadding.dp,
        )

        val innerMessageDrawer: @Composable RowScope.(Modifier, EmptyErase) -> SimpleTextDrawer =
            { modifier, emptyErase ->
                messageDrawer(
                    manager = manager,
                    modifier = modifier,
                    eventListener = eventListener,
                    lineBreakByContent = lineBreakByContent,
                    emptyErase = emptyErase,
                    enabled = editable,
                    onSelectionLister = manager::toggleSelection,
                    textStyle = { defaultTextStyle(it, fontFamily) }
                )
            }

        val codeBlockDrawer = swipeTextDrawer(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium),
            dragIconWidth = dragIconWidth,
            config = drawConfig,
            onDragHover = manager::onDragHover,
            onSelected = manager::onSelected,
            enabled = editable,
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
                    lineBreakByContent = lineBreakByContent,
                    emptyErase = EmptyErase.CHANGE_TYPE,
                    enabled = editable,
                    onSelectionLister = manager::toggleSelection,
                )
            }
        )

        val swipeTextDrawer = swipeTextDrawer(
            manager = manager,
            dragIconWidth = dragIconWidth,
            isDesktop = isDesktop,
            config = drawConfig,
            enabled = editable,
            endContent = headerEndContent
        ) {
            innerMessageDrawer(commonTextModifier, EmptyErase.DELETE)
        }

        val aiAnswerDrawer = swipeTextDrawer(
            manager = manager,
            customBackgroundColor = MaterialTheme.colorScheme.surfaceVariant,
            dragIconWidth = dragIconWidth,
            isDesktop = isDesktop,
            config = drawConfig,
            enabled = true,
            paddingValues = PaddingValues(vertical = 16.dp)
        ) {
            innerMessageDrawer(commonTextModifier, EmptyErase.CHANGE_TYPE)
        }

        val checkItemDrawer = checkItemDrawer(
            manager = manager,
            isDesktop = isDesktop,
            modifier = Modifier,
            dragIconWidth = dragIconWidth,
            config = drawConfig,
            enabled = editable,
            checkBoxPadding = PaddingValues(
                start = drawConfig.checkBoxStartPadding.dp,
                end = drawConfig.checkBoxEndPadding.dp,
            )
        ) {
            innerMessageDrawer(Modifier, EmptyErase.CHANGE_TYPE)
        }

        val unOrderedListItemDrawer =
            unOrderedListItemDrawer(
                manager = manager,
                isDesktop = isDesktop,
                enabled = editable,
                dragIconWidth = dragIconWidth,
                config = drawConfig,
                checkBoxPadding = PaddingValues(
                    start = drawConfig.listItemStartPadding.dp,
                    end = drawConfig.listItemEndPadding.dp
                )
            ) {
                innerMessageDrawer(commonTextModifier, EmptyErase.CHANGE_TYPE)
            }

        val headerDrawer = headerDrawer(
            manager = manager,
            enabled = editable,
            modifier = Modifier.clip(RoundedCornerShape(0.dp, 0.dp, 8.dp, 8.dp)),
            headerClick = onHeaderClick,
            onKeyEvent = eventListener,
            lineBreakByContent = lineBreakByContent,
            selectionState = manager.selectionState,
            drawConfig = drawConfig,
            fontFamily = fontFamily,
        )

        val imageDrawer = ImageDrawer(
            config = drawConfig,
            onSelected = manager::onSelected,
            containerModifier = Modifier::defaultImageShape,
            mergeRequest = manager::mergeRequest,
            onDelete = manager::onDelete,
            onDragStart = manager::onDragStart,
            onDragStop = manager::onDragStop
        )

        val imageDrawerInGroup = ImageDrawer(
            config = drawConfig,
            onSelected = manager::onSelected,
            containerModifier = Modifier::defaultImageShape,
            mergeRequest = manager::mergeRequest,
            onDelete = manager::onDelete,
            onDragStart = manager::onDragStart,
            onDragStop = manager::onDragStop
        )

        val loadingDrawer = LoadingDrawer()

        return buildMap {
            put(StoryTypes.TEXT.type.number, swipeTextDrawer)
            put(StoryTypes.ADD_BUTTON.type.number, AddButtonDrawer())
            put(StoryTypes.SPACE.type.number, SpaceDrawer(drawConfig, manager::moveRequest))
            put(
                StoryTypes.ON_DRAG_SPACE.type.number,
                SpaceDrawer(drawConfig, manager::moveRequest, Color.Gray)
            )
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
            put(StoryTypes.IMAGE.type.number, imageDrawer)
            put(StoryTypes.GROUP_IMAGE.type.number, RowGroupDrawer(imageDrawerInGroup))
            put(StoryTypes.AI_ANSWER.type.number, aiAnswerDrawer)
            put(StoryTypes.LOADING.type.number, loadingDrawer)
        }
    }
}

@Composable
private fun RowScope.messageDrawer(
    manager: WriteopiaStateManager,
    modifier: Modifier = Modifier,
    textStyle: @Composable (StoryStep) -> TextStyle = { defaultTextStyle(it) },
    lineBreakByContent: Boolean,
    enabled: Boolean,
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
        lineBreakByContent = lineBreakByContent,
        enabled = enabled,
        emptyErase = emptyErase,
        selectionState = manager.selectionState,
        onSelectionLister = onSelectionLister
    )
}
