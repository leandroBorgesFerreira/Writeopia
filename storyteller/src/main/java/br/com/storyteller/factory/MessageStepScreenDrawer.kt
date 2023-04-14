package br.com.storyteller.factory

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.storyteller.model.StoryStep

class MessageStepScreenDrawer : StepScreenDrawer {

    @Composable
    override fun Screen(step: StoryStep) {
        Surface(
            shape = RoundedCornerShape(20),
            modifier = Modifier.padding(vertical = 5.dp)
        ) {
            Box(modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)) {
                Text(
                    text = step.text!!,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
