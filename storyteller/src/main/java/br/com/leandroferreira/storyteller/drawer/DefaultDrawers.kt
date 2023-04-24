package br.com.leandroferreira.storyteller.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.leandroferreira.storyteller.drawer.commands.CommandsCompositeDrawer
import br.com.leandroferreira.storyteller.drawer.content.AddButtonDrawer
import br.com.leandroferreira.storyteller.drawer.content.ImageGroupDrawer
import br.com.leandroferreira.storyteller.drawer.content.ImageStepDrawer
import br.com.leandroferreira.storyteller.drawer.content.MessageStepDrawer
import br.com.leandroferreira.storyteller.drawer.content.VideoStepDrawer
import br.com.leandroferreira.storyteller.model.Command
import br.com.leandroferreira.storyteller.model.StepType

object DefaultDrawers {

    fun create(
        editable: Boolean = false,
        onListCommand: (Command) -> Unit,
        onTextEdit: (String, Int) -> Unit,
        mergeRequest: (receiverId: String, senderId: String) -> Unit = { _, _ -> }
    ): Map<String, StoryUnitDrawer> =
        buildMap {
            val commandsComposite: (StoryUnitDrawer) -> StoryUnitDrawer = { stepDrawer ->
                CommandsCompositeDrawer(
                    stepDrawer,
                    onMoveUp = onListCommand,
                    onMoveDown = onListCommand,
                    onDelete = onListCommand,
                )
            }

            val imageDrawer = ImageStepDrawer(
                containerModifier = ImageStepDrawer.Companion::defaultModifier,
                mergeRequest = mergeRequest
            )

            val imageDrawerInGroup = ImageStepDrawer(
                containerModifier = ImageStepDrawer.Companion::defaultModifier,
                mergeRequest = mergeRequest
            )

            val messageDrawer = MessageStepDrawer(
                containerModifier = Modifier
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .clip(shape = RoundedCornerShape(size = 12.dp))
                    .background(Color(0xFFFAF8F2)),
                onTextEdit = onTextEdit,
            )

            put(
                StepType.MESSAGE.type,
                if (editable) commandsComposite(messageDrawer) else messageDrawer
            )
            put(StepType.ADD_BUTTON.type, AddButtonDrawer())
            put(StepType.IMAGE.type, if (editable) commandsComposite(imageDrawer) else imageDrawer)
            put(
                StepType.GROUP_IMAGE.type,
                if (editable) {
                    commandsComposite(ImageGroupDrawer(imageDrawerInGroup))
                } else {
                    ImageGroupDrawer(imageDrawerInGroup)
                }
            )
            put(
                StepType.VIDEO.type,
                if (editable) commandsComposite(VideoStepDrawer()) else VideoStepDrawer()
            )
        }
}
