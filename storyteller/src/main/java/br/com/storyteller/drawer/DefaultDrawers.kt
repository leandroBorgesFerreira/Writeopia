package br.com.storyteller.drawer

import android.os.Message
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.storyteller.drawer.commands.CommandsCompositeDrawer
import br.com.storyteller.drawer.content.AddButtonDrawer
import br.com.storyteller.drawer.content.ImageGroupDrawer
import br.com.storyteller.drawer.content.ImageStepDrawer
import br.com.storyteller.drawer.content.MessageStepDrawer
import br.com.storyteller.drawer.content.VideoStepDrawer
import br.com.storyteller.model.Command
import br.com.storyteller.model.StepType

object DefaultDrawers {

    fun create(onCommand: (Command) -> Unit): Map<String, StoryUnitDrawer> = buildMap {
        val commandsComposite: (StoryUnitDrawer) -> StoryUnitDrawer = { stepDrawer ->
            CommandsCompositeDrawer(
                stepDrawer,
                onMoveUp = onCommand,
                onMoveDown = onCommand,
                onDelete = onCommand,
            )
        }


        val imageDrawer = ImageStepDrawer(
            containerModifier = Modifier
                .clip(shape = RoundedCornerShape(size = 12.dp))
                .background(Color(0xFFE1E0E0))
        )

        val messageDrawer = MessageStepDrawer(
            containerModifier = Modifier
                .padding(2.dp)
                .clip(shape = RoundedCornerShape(size = 12.dp))
                .background(Color(0xFFFAF8F2))
        )

        put(StepType.MESSAGE.type, messageDrawer)
        put(StepType.ADD_BUTTON.type, AddButtonDrawer())
        put(StepType.IMAGE.type, commandsComposite(imageDrawer))
        put(StepType.GROUP_IMAGE.type, ImageGroupDrawer(imageDrawer))
        put(StepType.VIDEO.type, commandsComposite(VideoStepDrawer()))
    }
}
