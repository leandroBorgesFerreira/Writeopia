package io.writeopia.editor.ui.desktop

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun EditorScaffold(
    clickAtBottom: () -> Unit,
    onBackClick: () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BoxWithConstraints {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .let { modifierLet ->
                        if (maxWidth > 900.dp) {
                            modifierLet.width(1000.dp)
                        } else {
                            modifierLet.fillMaxWidth()
                        }
                    }
                    .defaultMinSize(minHeight = 700.dp)
//                    .clip(RoundedCornerShape(20.dp))
//                    .background(Color.White)
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

            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.align(Alignment.TopStart)
                    .padding(top = 40.dp)
                    .clip(CircleShape)
                    .clickable(
                        onClick = onBackClick
                    )
                    .padding(10.dp)
                    .testTag("NoteEditorScreenNavigateBack"),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}