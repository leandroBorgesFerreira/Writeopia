package io.writeopia.sdk.drawer

import androidx.compose.ui.focus.FocusState

interface SimpleMessageDrawer : StoryStepDrawer {

    var onFocusChanged: (FocusState) -> Unit

}