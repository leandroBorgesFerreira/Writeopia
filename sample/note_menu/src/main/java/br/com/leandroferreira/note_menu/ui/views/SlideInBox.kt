package br.com.leandroferreira.note_menu.ui.views

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.with
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SlideInBox(
    modifier: Modifier = Modifier,
    editState: Boolean,
    animationLabel: String,
    duration: Int = 300,
    content: @Composable AnimatedVisibilityScope.(targetState: Boolean) -> Unit) {
    AnimatedContent(
        modifier = modifier,
        targetState = editState,
        label = animationLabel,
        transitionSpec = {
            slideInVertically(
                animationSpec = tween(durationMillis = duration),
                initialOffsetY = { fullHeight -> fullHeight }
            ) with fadeOut()
        },
        content = content
    )
}