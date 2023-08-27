package com.github.leandroborgesferreira.storyteller.drawer.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.leandroborgesferreira.storyteller.drawer.DrawInfo
import com.github.leandroborgesferreira.storyteller.drawer.StoryStepDrawer
import com.github.leandroborgesferreira.storyteller.drawer.content.decoration.ListItemDecoratorDrawer
import com.github.leandroborgesferreira.storyteller.model.story.StoryTypes
import com.github.leandroborgesferreira.storyteller.models.story.StoryStep

class UnOrderedListItem(
    private val modifier: Modifier,
    private val messageModifier: Modifier,
) : StoryStepDrawer {

    @Composable
    override fun Step(step: StoryStep, drawInfo: DrawInfo) {
        val focusRequester = remember { FocusRequester() }

        ListItemDecoratorDrawer(
            modifier = modifier,
            stepDrawer = MessageDrawer(
                modifier,
                messageModifier,
                focusRequester = focusRequester
            )
        ).Step(step = step, drawInfo = drawInfo)
    }
}

@Preview
@Composable
private fun UnOrderedListItemPreview() {
    val modifier = Modifier
        .background(Color.Cyan)
        .padding(vertical = 4.dp, horizontal = 6.dp)
        .fillMaxWidth()

    val modifierMessage = Modifier
        .background(Color.Cyan)
        .fillMaxWidth()


    UnOrderedListItem(modifier, messageModifier = modifierMessage).Step(
        StoryStep(type = StoryTypes.UNORDERED_LIST_ITEM.type, text = "Item1"),
        DrawInfo()
    )
}