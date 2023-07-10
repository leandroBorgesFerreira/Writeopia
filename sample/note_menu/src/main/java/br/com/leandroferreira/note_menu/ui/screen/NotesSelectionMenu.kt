package br.com.leandroferreira.note_menu.ui.screen

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import br.com.leandroferreira.note_menu.ui.views.SlideInBox

private const val INNER_PADDING = 3

@Composable
internal fun BoxScope.NotesSelectionMenu(
    modifier: Modifier = Modifier,
    visibilityState: Boolean,
) {

    val animationSpec = spring(
        stiffness = 4000F,
        visibilityThreshold = IntOffset.VisibilityThreshold
    )



    SlideInBox(
        modifier = modifier
            .background(Color.Transparent)
            .align(Alignment.BottomCenter),
        editState = visibilityState,
        showBackground = false,
        outsideClick = { },
        enterAnimationSpec = animationSpec,
        exitAnimationSpec = animationSpec,
        animationLabel = "configurationsMenuAnimation"
    ) {
        val topCorner = CornerSize(16.dp)
        val bottomCorner = CornerSize(0.dp)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        topCorner,
                        topCorner,
                        bottomCorner,
                        bottomCorner
                    )
                )
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
        ) {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}
