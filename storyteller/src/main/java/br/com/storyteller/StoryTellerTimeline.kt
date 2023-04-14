package br.com.storyteller

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.storyteller.command.Command
import br.com.storyteller.drawer.content.AddButtonDrawer
import br.com.storyteller.drawer.content.ImageStepDrawer
import br.com.storyteller.drawer.content.MessageStepDrawer
import br.com.storyteller.drawer.StepDrawer
import br.com.storyteller.drawer.commands.CommandsCompositeDrawer
import br.com.storyteller.model.StepType
import br.com.storyteller.model.StoryStep

@Composable
fun StoryTellerTimeline(
    modifier: Modifier = Modifier,
    drawers: Map<String, StepDrawer>,
    steps: List<StoryStep>,
    onStepsUpdate: (List<StoryStep>) -> Unit = {}
) {
    LazyColumn(modifier = modifier, content = {
        items(steps) { storyStep ->
            drawers[storyStep.type]?.Step(storyStep)
        }
    })
}

fun defaultDrawers(onCommand: (Command) -> Unit): Map<String, StepDrawer> {
    return buildMap {
        put(StepType.MESSAGE.type, MessageStepDrawer())
        put(StepType.ADD_BUTTON.type, AddButtonDrawer())
        put(
            StepType.IMAGE.type,
            CommandsCompositeDrawer(
                ImageStepDrawer(),
                onMoveUp = onCommand,
                onMoveDown = onCommand,
                onDelete = onCommand,
            )
        )
    }
}
