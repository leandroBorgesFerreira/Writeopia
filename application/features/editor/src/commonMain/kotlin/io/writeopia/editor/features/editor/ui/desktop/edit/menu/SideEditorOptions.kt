package io.writeopia.editor.features.editor.ui.desktop.edit.menu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import io.writeopia.common.utils.collections.inBatches
import io.writeopia.common.utils.file.fileChooserLoad
import io.writeopia.common.utils.file.fileChooserSave
import io.writeopia.common.utils.icons.WrIcons
import io.writeopia.common.utils.modifier.clickableWithoutGettingFocus
import io.writeopia.model.Font
import io.writeopia.resources.WrStrings
import io.writeopia.sdk.models.span.Span
import io.writeopia.theme.WriteopiaTheme
import io.writeopia.ui.icons.WrSdkIcons
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SideEditorOptions(
    modifier: Modifier = Modifier,
    fontStyleSelected: () -> StateFlow<Font>,
    isEditableState: StateFlow<Boolean>,
    boldClick: (Span) -> Unit,
    setEditable: () -> Unit,
    checkItemClick: () -> Unit,
    listItemClick: () -> Unit,
    codeBlockClick: () -> Unit,
    highLightBlockClick: () -> Unit,
    onPresentationClick: () -> Unit,
    changeFontFamily: (Font) -> Unit,
    addImage: (String) -> Unit,
    exportJson: (String) -> Unit,
    exportMarkdown: (String) -> Unit,
    moveToRoot: () -> Unit,
    moveToClick: () -> Unit,
    askAiBySelection: () -> Unit,
    addPage: () -> Unit,
    deleteDocument: () -> Unit,
    favoriteDocument: () -> Unit,
) {
    var menuType by remember {
        mutableStateOf(OptionsType.NONE)
    }

    val showSubMenu by remember {
        derivedStateOf {
            menuType != OptionsType.NONE
        }
    }

    Row(modifier) {
        AnimatedVisibility(
            showSubMenu,
            enter = fadeIn(
                animationSpec = spring(stiffness = Spring.StiffnessHigh)
            ) + expandHorizontally(
                animationSpec = spring(
                    stiffness = Spring.StiffnessHigh,
                    visibilityThreshold = IntSize.VisibilityThreshold
                ),
            ),
        ) {
            Crossfade(
                menuType,
                animationSpec = tween(200),
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures { }
                }
            ) { type ->
                when (type) {
                    OptionsType.NONE -> {}

                    OptionsType.PAGE_STYLE -> {
                        PageStyleOptions(
                            changeFontFamily,
                            isEditableState,
                            setEditable,
                            fontStyleSelected(),
                            moveToClick,
                            moveToRoot
                        )
                    }

                    OptionsType.TEXT_OPTIONS -> {
                        TextOptions(
                            boldClick,
                            checkItemClick,
                            listItemClick,
                            codeBlockClick,
                            highLightBlockClick,
                            addImage,
                            addPage
                        )
                    }

                    OptionsType.ACTIONS -> {
                        Actions(
                            exportJson,
                            exportMarkdown,
                            askAiBySelection = askAiBySelection,
                            deleteDocument = deleteDocument,
                            favoriteDocument = favoriteDocument,
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(4.dp))

        Column(
            modifier = Modifier.border(
                1.dp,
                MaterialTheme.colorScheme.surfaceVariant,
                MaterialTheme.shapes.medium
            ).background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.medium)
        ) {
            val spacing = 3.dp

            Spacer(modifier = Modifier.height(spacing))

            val background = @Composable { optionsType: OptionsType ->
                if (optionsType == menuType) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.background
                }
            }

            val tint = @Composable { optionsType: OptionsType ->
                if (optionsType == menuType) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onBackground
                }
            }

            Icon(
                imageVector = WrIcons.pageStyle,
                contentDescription = "Document Style",
                modifier = Modifier
                    .padding(horizontal = spacing)
                    .clip(MaterialTheme.shapes.medium)
                    .background(background(OptionsType.PAGE_STYLE))
                    .clickable {
                        menuType = if (menuType != OptionsType.PAGE_STYLE) {
                            OptionsType.PAGE_STYLE
                        } else {
                            OptionsType.NONE
                        }
                    }
                    .size(40.dp)
                    .padding(10.dp),
                tint = tint(OptionsType.PAGE_STYLE)
            )

            Spacer(modifier = Modifier.height(1.dp))

            Icon(
                imageVector = WrIcons.textStyle,
                contentDescription = "Font Style",
                modifier = Modifier
                    .padding(horizontal = spacing)
                    .clip(MaterialTheme.shapes.medium)
                    .background(background(OptionsType.TEXT_OPTIONS))
                    .clickable {
                        menuType = if (menuType != OptionsType.TEXT_OPTIONS) {
                            OptionsType.TEXT_OPTIONS
                        } else {
                            OptionsType.NONE
                        }
                    }
                    .size(40.dp)
                    .padding(9.dp),
                tint = tint(OptionsType.TEXT_OPTIONS)
            )

            Spacer(modifier = Modifier.height(spacing))

            Icon(
                imageVector = WrIcons.zap,
                contentDescription = "Zap",
                modifier = Modifier
                    .padding(horizontal = spacing)
                    .clip(MaterialTheme.shapes.medium)
                    .background(background(OptionsType.ACTIONS))
                    .clickable {
                        menuType = if (menuType != OptionsType.ACTIONS) {
                            OptionsType.ACTIONS
                        } else {
                            OptionsType.NONE
                        }
                    }
                    .size(40.dp)
                    .padding(9.dp),
                tint = tint(OptionsType.ACTIONS)
            )

//            Spacer(modifier = Modifier.height(spacing))
//
//            Icon(
//                imageVector = WrIcons.play,
//                contentDescription = "Presentation",
//                modifier = Modifier
//                    .padding(horizontal = spacing)
//                    .clip(MaterialTheme.shapes.medium)
//                    .clickable(onClick = onPresentationClick)
//                    .size(40.dp)
//                    .padding(9.dp),
//                tint = MaterialTheme.colorScheme.onBackground
//            )
        }
    }
}

@Composable
fun PageStyleOptions(
    changeFontFamily: (Font) -> Unit,
    isEditableState: StateFlow<Boolean>,
    setEditable: () -> Unit,
    selectedState: StateFlow<Font>,
    moveButtonClick: () -> Unit,
    moveToRoot: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.border(
            1.dp,
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.shapes.medium
        ).background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.medium)
            .width(250.dp)
            .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 12.dp)
    ) {
        Title("Font")
        Spacer(modifier = Modifier.height(4.dp))
        FontOptions(changeFontFamily, selectedState)

        Spacer(modifier = Modifier.height(8.dp))

        Title("Actions")
        Spacer(modifier = Modifier.height(6.dp))
        LockButton(isEditableState, setEditable)
        Spacer(modifier = Modifier.height(4.dp))
        MoveToButton(moveButtonClick)
        Spacer(modifier = Modifier.height(4.dp))
        MoveToHomeButton(moveToRoot)
    }
}

@Composable
private fun Title(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun TextChanges(spanClick: (Span) -> Unit) {
    Row(
        modifier = Modifier.horizontalOptionsRow(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = WrIcons.bold,
            contentDescription = "Bold",
            modifier = Modifier.weight(1F)
                .clip(RoundedCornerShape(topStart = 6.dp, bottomStart = 6.dp))
                .size(32.dp)
                .clickableWithoutGettingFocus {
                    spanClick(Span.BOLD)
                }
                .padding(horizontal = 8.dp, vertical = 8.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )

        Icon(
            imageVector = WrIcons.italic,
            contentDescription = "Italic",
            modifier = Modifier.weight(1F)
                .size(32.dp)
                .clickableWithoutGettingFocus { spanClick(Span.ITALIC) }
                .padding(horizontal = 8.dp, vertical = 8.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )

        Icon(
            imageVector = WrIcons.underline,
            contentDescription = "Underlined text",
            modifier = Modifier.weight(1F)
                .clip(RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp))
                .size(32.dp)
                .clickableWithoutGettingFocus { spanClick(Span.UNDERLINE) }
                .padding(horizontal = 8.dp, vertical = 8.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun IconAndText(text: String, iconImage: ImageVector, click: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(start = 2.dp, end = 2.dp, bottom = 3.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = click)
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                MaterialTheme.shapes.medium
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(18.dp),
            imageVector = iconImage,
            contentDescription = "Image",
            tint = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            color = MaterialTheme.colorScheme.onBackground,
            style = buttonsTextStyle(),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun DecorationCommands(commands: Iterable<Pair<String, () -> Unit>>) {
    Column {
        commands.inBatches(2)
            .forEach { line ->
                Row {
                    line.forEach { (command, listener) ->
                        TextButton(
                            modifier = Modifier.weight(1F),
                            text = command,
                            onClick = listener
                        )
                    }
                }
            }
    }
}

@Composable
private fun TextButton(
    modifier: Modifier = Modifier,
    text: String,
    paddingValues: PaddingValues = PaddingValues(
        horizontal = 8.dp,
        vertical = 8.dp
    ),
    onClick: () -> Unit,
) {
    Text(
        modifier = modifier
            .padding(start = 2.dp, end = 2.dp, bottom = 3.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                MaterialTheme.shapes.medium
            )
            .padding(paddingValues),
        text = text,
        color = MaterialTheme.colorScheme.onBackground,
        style = buttonsTextStyle(),
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun InsertCommand(
    checkItemClick: () -> Unit,
    listItemClick: () -> Unit,
    codeBlockClick: () -> Unit,
) {
    Row(modifier = Modifier.horizontalOptionsRow()) {
        Icon(
            imageVector = WrSdkIcons.checkbox,
            contentDescription = "Check box",
            modifier = Modifier.weight(1F)
                .clip(RoundedCornerShape(topStart = 6.dp, bottomStart = 6.dp))
                .size(32.dp)
                .clickable(onClick = checkItemClick)
                .padding(horizontal = 8.dp, vertical = 7.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )

        Icon(
            imageVector = WrSdkIcons.list,
            contentDescription = "List item",
            modifier = Modifier.weight(1F)
                .size(32.dp)
                .clickable(onClick = listItemClick)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )

        Icon(
            imageVector = WrIcons.code,
            contentDescription = "Code block",
            modifier = Modifier.weight(1F)
                .clip(RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp))
                .size(32.dp)
                .clickable(onClick = codeBlockClick)
                .padding(horizontal = 8.dp, vertical = 7.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun Modifier.horizontalOptionsRow() =
    this.fillMaxWidth()
        .background(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.shapes.medium
        )

@Composable
internal fun FontOptions(
    changeFontFamily: (Font) -> Unit,
    selectedState: StateFlow<Font>,
    selectedColor: Color = WriteopiaTheme.colorScheme.highlight,
    defaultColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    val selected by selectedState.collectAsState()

    mapOf(
        Font.SYSTEM.label to FontFamily.Default,
        Font.SERIF.label to FontFamily.Serif,
        Font.MONOSPACE.label to FontFamily.Monospace,
        Font.CURSIVE.label to FontFamily.Cursive,
    ).toList()
        .inBatches(2)
        .forEach { items ->
            Row {
                items.forEach { (name, family) ->
                    Text(
                        name,
                        fontFamily = family,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(2.dp)
                            .background(
                                if (selected.label == name) selectedColor else defaultColor,
                                MaterialTheme.shapes.medium
                            ).weight(1F)
                            .clip(MaterialTheme.shapes.medium)
                            .clickable {
                                changeFontFamily(Font.fromLabel(name))
                            }
                            .padding(4.dp),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = buttonsTextStyle()
                    )
                }
            }
        }
}

@Composable
private fun TextOptions(
    spanClick: (Span) -> Unit,
    checkItemClick: () -> Unit,
    listItemClick: () -> Unit,
    codeBlockClick: () -> Unit,
    highLightBlockClick: () -> Unit,
    addImage: (String) -> Unit,
    addPage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.border(
            1.dp,
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.shapes.medium
        ).background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.medium)
            .width(250.dp)
            .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 12.dp)
    ) {
        Title(WrStrings.text())
        Spacer(modifier = Modifier.height(4.dp))
        TextChanges(spanClick)
        Spacer(modifier = Modifier.height(8.dp))

        Title(WrStrings.insert())
        Spacer(modifier = Modifier.height(4.dp))
        InsertCommand(checkItemClick, listItemClick, codeBlockClick)
        Spacer(modifier = Modifier.height(8.dp))

        Title(WrStrings.decoration())
        Spacer(modifier = Modifier.height(4.dp))

        DecorationCommands(
            commands = listOf(
                WrStrings.box() to highLightBlockClick,
//                "Warning" to {},
//                "Tip" to {},
//                "Code" to {}
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        Title(WrStrings.content())
        Spacer(modifier = Modifier.height(4.dp))
        IconAndText(WrStrings.image(), WrIcons.image) {
            fileChooserLoad("")?.let(addImage)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Title(WrStrings.links())
        Spacer(modifier = Modifier.height(4.dp))
        IconAndText(WrStrings.page(), WrIcons.file, addPage)
    }
}

@Composable
private fun Actions(
    exportJson: (String) -> Unit,
    exportMarkdown: (String) -> Unit,
    askAiBySelection: () -> Unit,
    deleteDocument: () -> Unit,
    favoriteDocument: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.border(
            1.dp,
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.shapes.medium
        ).background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.medium)
            .width(250.dp)
            .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 12.dp)
    ) {
        Title(WrStrings.askAi())

        Spacer(modifier = Modifier.height(4.dp))

        TextButton(
            modifier = Modifier.fillMaxWidth(),
            text = WrStrings.askAi(),
            paddingValues = smallButtonPadding(),
            onClick = askAiBySelection
        )

        Spacer(modifier = Modifier.height(8.dp))

//        TextButton(
//            modifier = Modifier.fillMaxWidth(),
//            text = "AI question box",
//            paddingValues = smallButtonPadding()
//        ) { }

//        Spacer(modifier = Modifier.height(8.dp))

        Title(WrStrings.export())

        Spacer(modifier = Modifier.height(4.dp))

        Row {
            TextButton(
                text = WrStrings.json(),
                modifier = Modifier.weight(1F),
                paddingValues = smallButtonPadding()
            ) {
                fileChooserSave()?.let {
                    exportJson(it)
                }
            }

            TextButton(
                text = WrStrings.markdown(),
                modifier = Modifier.weight(1F),
                paddingValues = smallButtonPadding()
            ) {
                fileChooserSave()?.let(exportMarkdown)
            }

        }

        Spacer(modifier = Modifier.height(8.dp))

        Title(WrStrings.document())

        Spacer(modifier = Modifier.height(4.dp))

        TextButton(
            text = WrStrings.deleteDocument(),
            modifier = Modifier.fillMaxWidth(),
            paddingValues = smallButtonPadding(),
            onClick = deleteDocument
        )
        
        TextButton(
            text = WrStrings.favorite(),
            modifier = Modifier.fillMaxWidth(),
            paddingValues = smallButtonPadding(),
            onClick = favoriteDocument
        )

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun buttonsTextStyle() =
    MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)

@Composable
private fun smallButtonPadding() = PaddingValues(horizontal = 8.dp, vertical = 4.dp)

private enum class OptionsType {
    NONE,
    PAGE_STYLE,
    TEXT_OPTIONS,
    ACTIONS
}
