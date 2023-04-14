package br.com.storyteller.drawer

import br.com.storyteller.drawer.commands.CommandsCompositeDrawer
import br.com.storyteller.drawer.content.AddButtonDrawer
import br.com.storyteller.drawer.content.ImageStepDrawer
import br.com.storyteller.drawer.content.MessageStepDrawer
import br.com.storyteller.drawer.content.VideoStepDrawer
import br.com.storyteller.model.Command
import br.com.storyteller.model.StepType

object DefaultDrawers {

    fun create(onCommand: (Command) -> Unit): Map<String, StepDrawer> = buildMap {
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
        put(StepType.VIDEO.type, VideoStepDrawer())
    }
}
