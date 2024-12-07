package io.writeopia.editor.features.editor.ui.desktop.edit.menu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import io.writeopia.common.utils.collections.inBatches
import io.writeopia.common.utils.icons.WrIcons
import io.writeopia.theme.WriteopiaTheme
import io.writeopia.ui.icons.WrSdkIcons
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SideEditorOptions(
    modifier: Modifier = Modifier,
    showState: StateFlow<Boolean>,
    fontClick: () -> Unit,
    checkItemClick: () -> Unit,
    listItemClick: () -> Unit,
    codeBlockClick: () -> Unit,
    highLightBlockClick: () -> Unit,
) {
    val showSubMenu by showState.collectAsState()

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
            Column(
                modifier = Modifier.border(
                    1.dp,
                    MaterialTheme.colorScheme.surfaceVariant,
                    MaterialTheme.shapes.medium
                ).background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.medium)
                    .width(250.dp)
                    .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 12.dp)
            ) {
                Title("Text")

                Spacer(modifier = Modifier.height(4.dp))

                TextChanges()

                Title("Insert")

                Spacer(modifier = Modifier.height(4.dp))

                InsertCommand(checkItemClick, listItemClick, codeBlockClick)

                Spacer(modifier = Modifier.height(4.dp))

                Title("Decoration")

                Spacer(modifier = Modifier.height(4.dp))

                DecorationCommands(
                    commands = listOf(
                        "Highlight" to highLightBlockClick,
                        "Warning" to {},
                        "Tip" to {},
                        "Block" to {}
                    )
                )
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

            val background = if (showSubMenu) {
                MaterialTheme.colorScheme.secondary
            } else {
                MaterialTheme.colorScheme.background
            }

            val tint = if (showSubMenu) {
                MaterialTheme.colorScheme.onSecondary
            } else {
                MaterialTheme.colorScheme.onBackground
            }

            Icon(
                imageVector = WrIcons.pageStyle,
                contentDescription = "Document Style",
                modifier = Modifier
                    .padding(horizontal = spacing)
                    .clip(MaterialTheme.shapes.medium)
                    .clickable { }
                    .size(40.dp)
                    .padding(10.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(1.dp))

            Icon(
                imageVector = WrIcons.textStyle,
                contentDescription = "Font Style",
                modifier = Modifier
                    .padding(horizontal = spacing)
                    .clip(MaterialTheme.shapes.medium)
                    .background(background)
                    .clickable(onClick = fontClick)
                    .size(40.dp)
                    .padding(9.dp),
                tint = tint
            )

            Spacer(modifier = Modifier.height(spacing))
        }
    }
}

@Composable
private fun Title(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
private fun TextChanges() {
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
                .clickable { }
                .padding(horizontal = 8.dp, vertical = 8.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )

        Icon(
            imageVector = WrIcons.italic,
            contentDescription = "Italic",
            modifier = Modifier.weight(1F)
                .size(32.dp)
                .clickable { }
                .padding(horizontal = 8.dp, vertical = 8.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )

        Icon(
            imageVector = WrIcons.underline,
            contentDescription = "Underlined text",
            modifier = Modifier.weight(1F)
                .clip(RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp))
                .size(32.dp)
                .clickable { }
                .padding(horizontal = 8.dp, vertical = 8.dp),
            tint = MaterialTheme.colorScheme.onBackground
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
                        Text(
                            modifier = Modifier
                                .weight(1F)
                                .padding(start = 2.dp, end = 2.dp, bottom = 3.dp)
                                .clickable(onClick = listener)
                                .background(
                                    WriteopiaTheme.colorScheme.optionsSelector,
                                    MaterialTheme.shapes.medium
                                )
                                .padding(horizontal = 8.dp, vertical = 8.dp),
                            text = command,
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
    }
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
