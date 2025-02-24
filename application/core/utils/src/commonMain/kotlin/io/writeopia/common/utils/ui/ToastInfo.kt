package io.writeopia.common.utils.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp

class ToastInfo {
    var hideGlobalContent: Boolean by mutableStateOf(false)
}

@Composable
fun GlobalToastBox(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    val state by remember { mutableStateOf(ToastInfo()) }

    val newModifier = if (state.hideGlobalContent) modifier.blur(10.dp) else modifier

    CompositionLocalProvider(LocalToastInfo provides state) {
        Box(modifier = newModifier) {
            content()
        }
    }
}
