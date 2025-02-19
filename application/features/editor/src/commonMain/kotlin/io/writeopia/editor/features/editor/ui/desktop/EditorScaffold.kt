package io.writeopia.editor.features.editor.ui.desktop

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun BoxScope.EditorScaffold(
    clickAtBottom: () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize().align(Alignment.TopCenter)) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .let { modifierLet ->
                    if (maxWidth > 750.dp) {
                        modifierLet.width(850.dp)
                    } else {
                        modifierLet.fillMaxWidth()
                    }
                }
        ) {
            content()

            Box(
                modifier = Modifier
                    .weight(1F)
                    .fillMaxWidth()
                    .clickable(
                        onClick = clickAtBottom,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    )
                    .testTag("EditorBottomContent")
            )
        }
    }
}
