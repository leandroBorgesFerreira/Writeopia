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
        val commandsComposite : (StepDrawer) -> StepDrawer = { stepDrawer ->
            CommandsCompositeDrawer(
                stepDrawer,
                onMoveUp = onCommand,
                onMoveDown = onCommand,
                onDelete = onCommand,
            )
        }

        put(StepType.MESSAGE.type, MessageStepDrawer())
        put(StepType.ADD_BUTTON.type, AddButtonDrawer())
        put(StepType.IMAGE.type, commandsComposite(ImageStepDrawer()))
        put(StepType.VIDEO.type, commandsComposite(VideoStepDrawer()))
    }
}
