package br.com.leandroferreira.storyteller.drawer.utilities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.leandroferreira.storyteller.draganddrop.DropTarget
import br.com.leandroferreira.storyteller.drawer.StoryUnitDrawer
import br.com.leandroferreira.storyteller.model.StoryUnit

class SpaceDrawer(private val moveRequest: (String, Int) -> Unit = { _, _ -> }) : StoryUnitDrawer {

    @Composable
    override fun Step(step: StoryUnit, editable: Boolean, extraData: Map<String, Any>) {
        DropTarget(modifier = Modifier.padding(6.dp)) { inBound, data ->
            if (inBound && data != null) {
                moveRequest(data.id, step.localPosition)
            }

            Box(
                modifier = Modifier
                    .height(30.dp)
                    .fillMaxWidth()
                    .background(if (inBound) Color.LightGray else Color.Transparent)
            )
        }
    }
}
