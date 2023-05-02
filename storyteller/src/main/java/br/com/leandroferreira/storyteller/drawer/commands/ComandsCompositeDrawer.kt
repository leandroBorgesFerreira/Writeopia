package br.com.leandroferreira.storyteller.drawer.commands

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.leandroferreira.storyteller.drawer.StoryUnitDrawer
import br.com.leandroferreira.storyteller.model.Command
import br.com.leandroferreira.storyteller.model.StoryUnit

/**
 * Drawer for commands. This drawer adds commands for move up, move down and delete on top of any
 * Composable. If this [CommandsDecoratorDrawer] at the moment it is only possible to have all 3
 * buttons, later it will be added the possibility to chose which button appears to the user.
 */
class CommandsDecoratorDrawer(
    private val innerStep: StoryUnitDrawer,
    private val onMoveUp: (Command) -> Unit = {},
    private val onMoveDown: (Command) -> Unit = {},
    private val onDelete: (Command) -> Unit = {}
) : StoryUnitDrawer {

    @Composable
    override fun Step(step: StoryUnit, editable: Boolean, extraData: Map<String, Any>) {
        Box(modifier = Modifier.padding(vertical = 3.dp)) {
            Box(modifier = Modifier.padding(top = 3.dp)) {
                innerStep.Step(step = step, editable, extraData)
            }

            DeleteButton(step)

            if (step.localPosition != 0) {
                MoveUpButton(step)
            }

            if ((extraData["listSize"] as? Int) != step.localPosition + 1) {
                MoveDownButton(step)
            }
        }
    }

    @Composable
    private fun BoxScope.DeleteButton(step: StoryUnit) {
        Box(modifier = Modifier
            .buttonModifier()
            .align(Alignment.TopStart)) {
            IconButton(onClick = { onDelete(Command(type = "delete", step)) }) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "",
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }

    @Composable
    private fun BoxScope.MoveUpButton(step: StoryUnit) {
        Box(modifier = Modifier
            .buttonModifier()
            .align(Alignment.TopCenter)) {
            IconButton(onClick = { onMoveUp(Command(type = "move_up", step)) }) {
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowUp,
                    contentDescription = "",
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }

    @Composable
    private fun BoxScope.MoveDownButton(step: StoryUnit) {
        Box(modifier = Modifier
            .buttonModifier()
            .align(Alignment.BottomCenter)) {
            IconButton(onClick = { onMoveDown(Command(type = "move_down", step)) }) {
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowDown,
                    contentDescription = "",
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }

    private fun Modifier.buttonModifier() = this
        .clip(shape = CircleShape)
        .border(
            width = 1.dp,
            color = Color.Gray,
            shape = CircleShape
        )
        .background(Color.LightGray)
        .size(30.dp)
}
