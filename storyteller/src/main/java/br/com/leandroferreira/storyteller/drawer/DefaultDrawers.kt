package br.com.leandroferreira.storyteller.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.leandroferreira.storyteller.drawer.commands.CommandsDecoratorDrawer
import br.com.leandroferreira.storyteller.drawer.content.AddButtonDrawer
import br.com.leandroferreira.storyteller.drawer.content.CheckItemDrawer
import br.com.leandroferreira.storyteller.drawer.content.ImageGroupDrawer
import br.com.leandroferreira.storyteller.drawer.content.ImageStepDrawer
import br.com.leandroferreira.storyteller.drawer.content.ImageStepDrawer.Companion.defaultModifier
import br.com.leandroferreira.storyteller.drawer.content.MessageStepDrawer
import br.com.leandroferreira.storyteller.drawer.content.VideoStepDrawer
import br.com.leandroferreira.storyteller.drawer.content.SpaceDrawer
import br.com.leandroferreira.storyteller.model.story.StepType
import br.com.leandroferreira.storyteller.model.story.StoryStep
import br.com.leandroferreira.storyteller.model.change.CheckInfo
import br.com.leandroferreira.storyteller.model.change.DeleteInfo
import br.com.leandroferreira.storyteller.model.change.LineBreakInfo
import br.com.leandroferreira.storyteller.model.change.MergeInfo
import br.com.leandroferreira.storyteller.model.change.MoveInfo

object DefaultDrawers {

    fun create(
        editable: Boolean = false,
        onTextEdit: (String, Int) -> Unit,
        onLineBreak: (LineBreakInfo) -> Unit,
        mergeRequest: (receiver: MergeInfo) -> Unit = { },
        moveRequest: (moveInfo: MoveInfo) -> Unit = { },
        checkRequest: (checkInfo: CheckInfo) -> Unit = { },
        onDeleteRequest: (DeleteInfo) -> Unit
    ): Map<String, StoryUnitDrawer> =
        buildMap {
            val commandsComposite: (StoryUnitDrawer) -> StoryUnitDrawer = { stepDrawer ->
                CommandsDecoratorDrawer(
                    stepDrawer,
                    onDelete = onDeleteRequest,
                )
            }

            val imageDrawer = ImageStepDrawer(
                containerModifier = { inBound -> Modifier.defaultModifier(inBound) },
                mergeRequest = mergeRequest
            )

            val imageDrawerInGroup = ImageStepDrawer(
                containerModifier = { inBound -> Modifier.defaultModifier(inBound) },
                mergeRequest = mergeRequest
            )

            val messageBoxDrawer = MessageStepDrawer(
                containerModifier = Modifier
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .clip(shape = RoundedCornerShape(size = 12.dp))
                    .background(Color(0xFFFAF8F2)),
                innerContainerModifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                onTextEdit = onTextEdit,
                onLineBreak = {},
                onDeleteRequest = onDeleteRequest
            )

            val messageDrawer = MessageStepDrawer(
                containerModifier = Modifier,
                innerContainerModifier = Modifier.padding(horizontal = 8.dp, vertical = 0.dp),
                onTextEdit = onTextEdit,
                onLineBreak = onLineBreak,
                onDeleteRequest = onDeleteRequest
            )

            val checkItemDrawer = CheckItemDrawer(
                onCheckedChange = checkRequest,
                onTextEdit = onTextEdit,
                onLineBreak = onLineBreak,
                onDeleteRequest = onDeleteRequest
            )

            put(StepType.MESSAGE_BOX.type, messageBoxDrawer)
            put(StepType.MESSAGE.type, messageDrawer)
            put(StepType.ADD_BUTTON.type, AddButtonDrawer())
            put(StepType.IMAGE.type, if (editable) commandsComposite(imageDrawer) else imageDrawer)
            put(
                StepType.GROUP_IMAGE.type,
                if (editable) {
                    commandsComposite(ImageGroupDrawer(commandsComposite(imageDrawerInGroup)))
                } else {
                    ImageGroupDrawer(imageDrawerInGroup)
                }
            )
            put(
                StepType.VIDEO.type,
                if (editable) commandsComposite(VideoStepDrawer()) else VideoStepDrawer()
            )

            put(StepType.SPACE.type, SpaceDrawer(moveRequest))
            put(StepType.CHECK_ITEM.type, checkItemDrawer)
        }
}
