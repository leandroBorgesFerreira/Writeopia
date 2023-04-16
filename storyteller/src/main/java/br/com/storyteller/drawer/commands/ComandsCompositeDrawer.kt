package br.com.storyteller.drawer.commands

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.storyteller.drawer.StoryUnitDrawer
import br.com.storyteller.model.Command
import br.com.storyteller.model.StoryUnit

class CommandsCompositeDrawer(
    private val innerStep: StoryUnitDrawer,
    private val onMoveUp: (Command) -> Unit = {},
    private val onMoveDown: (Command) -> Unit = {},
    private val onDelete: (Command) -> Unit = {}
) : StoryUnitDrawer {

    @Composable
    override fun Step(step: StoryUnit) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            innerStep.Step(step = step)

            Spacer(modifier = Modifier.width(10.dp))

            Surface(
                shape = RoundedCornerShape(20),
                modifier = Modifier.padding(vertical = 5.dp)
            ) {
                Row(modifier = Modifier.padding(vertical = 2.dp)) {
                    IconButton(onClick = { onMoveUp(Command(type = "move_up", step)) }) {
                        Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "")
                    }
                    IconButton(onClick = { onMoveDown(Command(type = "move_down", step)) }) {
                        Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "")
                    }
                    IconButton(onClick = { onDelete(Command(type = "delete", step)) }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "")
                    }
                }
            }
        }
    }
}
