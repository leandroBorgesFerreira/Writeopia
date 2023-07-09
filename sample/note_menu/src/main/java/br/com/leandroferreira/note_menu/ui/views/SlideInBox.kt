package br.com.leandroferreira.note_menu.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun SlideInBox(
    modifier: Modifier = Modifier,
    editState: Boolean,
    animationLabel: String,
    outsideClick: () -> Unit,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = editState,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xAA000000))
                .clickable(onClick = outsideClick)
        )
    }

    AnimatedVisibility(
        modifier = modifier,
        visible = editState,
        label = animationLabel,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight }
        ),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight }
        ),
        content = content
    )
}