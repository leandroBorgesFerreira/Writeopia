package br.com.storyteller.drawer

import br.com.storyteller.drawer.commands.CommandsCompositeDrawer
import br.com.storyteller.drawer.content.AddButtonDrawer
import br.com.storyteller.drawer.content.ImageStoryUnitDrawer
import br.com.storyteller.drawer.content.MessageStoryUnitDrawer
import br.com.storyteller.drawer.content.VideoStoryUnitDrawer
import br.com.storyteller.model.Command
import br.com.storyteller.model.StepType

object DefaultDrawers {

    fun create(onCommand: (Command) -> Unit): Map<String, StoryUnitDrawer> = buildMap {
        val commandsComposite : (StoryUnitDrawer) -> StoryUnitDrawer = { stepDrawer ->
            CommandsCompositeDrawer(
                stepDrawer,
                onMoveUp = onCommand,
                onMoveDown = onCommand,
                onDelete = onCommand,
            )
        }

        put(StepType.MESSAGE.type, MessageStoryUnitDrawer())
        put(StepType.ADD_BUTTON.type, AddButtonDrawer())
        put(StepType.IMAGE.type, commandsComposite(ImageStoryUnitDrawer()))
        put(StepType.VIDEO.type, commandsComposite(VideoStoryUnitDrawer()))
    }
}
