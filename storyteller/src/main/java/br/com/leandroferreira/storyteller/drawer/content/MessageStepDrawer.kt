package br.com.leandroferreira.storyteller.drawer.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.leandroferreira.storyteller.drawer.StoryUnitDrawer
import br.com.leandroferreira.storyteller.model.StoryStep
import br.com.leandroferreira.storyteller.model.StoryUnit

class MessageStepDrawer(
    private val containerModifier: Modifier? = null,
    private val onTextEdit: (String, Int) -> Unit,
) : StoryUnitDrawer {

    @Composable
    override fun Step(step: StoryUnit, editable: Boolean, extraData: Map<String, Any>) {
        val messageStep = step as StoryStep

        Box(
            modifier = containerModifier ?: Modifier
                .padding(vertical = 3.dp, horizontal = 8.dp)
                .clip(shape = RoundedCornerShape(size = 12.dp))
                .background(Color.LightGray)
        ) {
            if (editable) {
                var inputText by remember {
                    mutableStateOf(messageStep.text ?: "")
                }

                TextField(
                    value = inputText,
                    onValueChange = { text ->
                        inputText = text
                        onTextEdit(text, step.localPosition)
                    },
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent
                    ),
                )
            } else {
                Text(
                    text = messageStep.text ?: "",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                )
            }
        }
    }
}
