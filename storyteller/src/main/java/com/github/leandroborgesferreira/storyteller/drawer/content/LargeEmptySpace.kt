package com.github.leandroborgesferreira.storyteller.drawer.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.leandroborgesferreira.storyteller.drawer.DrawInfo
import com.github.leandroborgesferreira.storyteller.drawer.StoryUnitDrawer
import com.github.leandroborgesferreira.storyteller.model.story.StoryUnit

class LargeEmptySpace(private val moveFocus: (Int) -> Unit = {}) : StoryUnitDrawer {

    @Composable
    override fun LazyItemScope.Step(step: StoryUnit, drawInfo: DrawInfo) {
        Box(
            modifier = Modifier
                .height(500.dp)
                .fillMaxWidth()
                .background(Color.Transparent)
                .clickable { moveFocus(drawInfo.position) }
        )
    }
}
