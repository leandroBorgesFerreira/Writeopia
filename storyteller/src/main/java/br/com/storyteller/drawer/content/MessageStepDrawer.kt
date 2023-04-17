package br.com.storyteller.drawer.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.storyteller.drawer.StoryUnitDrawer
import br.com.storyteller.model.StoryStep
import br.com.storyteller.model.StoryUnit

class MessageStepDrawer(private val containerModifier: Modifier? = null) : StoryUnitDrawer {

    @Composable
    override fun Step(step: StoryUnit) {
        val messageStep = step as StoryStep

        Box(
            modifier = containerModifier ?: Modifier
                .padding(vertical = 3.dp, horizontal = 8.dp)
                .clip(shape = RoundedCornerShape(size = 12.dp))
                .background(Color.LightGray)
        ) {
            Text(
                text = messageStep.text ?: "",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            )
        }
    }
}
