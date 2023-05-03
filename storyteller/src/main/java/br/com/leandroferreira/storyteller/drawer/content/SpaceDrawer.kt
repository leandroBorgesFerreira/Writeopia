package br.com.leandroferreira.storyteller.drawer.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.leandroferreira.storyteller.draganddrop.target.DropTarget
import br.com.leandroferreira.storyteller.drawer.StoryUnitDrawer
import br.com.leandroferreira.storyteller.model.StoryUnit

/**
 * Draws a white space. This Drawer is very important for accepting drop os other Composables for
 * reorder purposes. A space create a move request when dropping Composables in it while the other
 * story units create a mergeRequest.
 */
class SpaceDrawer(private val moveRequest: (String, Int) -> Unit = { _, _ -> }) : StoryUnitDrawer {

    @Composable
    override fun LazyItemScope.Step(
        step: StoryUnit,
        editable: Boolean,
        focusId: String?,
        extraData: Map<String, Any>
    ) {
        DropTarget { inBound, data ->
            if (inBound && data != null) {
                moveRequest(data.id, step.localPosition)
            }

            Box(
                modifier = Modifier
                    .height(10.dp)
                    .fillMaxWidth()
                    .background(if (inBound) Color.LightGray else Color.Transparent)
            )
        }
    }
}
