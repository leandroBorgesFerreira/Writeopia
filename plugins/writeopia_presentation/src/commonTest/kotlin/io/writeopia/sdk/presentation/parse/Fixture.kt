package io.writeopia.sdk.presentation.parse

import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.models.story.Tag
import io.writeopia.sdk.models.story.TagInfo
import io.writeopia.sdk.presentation.model.SlidePage

object Fixture {

    fun document(): Pair<List<StoryStep>, List<SlidePage>> {
        val step1 = StoryStep(
            localId = "0",
            type = StoryTypes.TEXT.type,
            text = "Come and see what you can do!"
        )

        val step2 = StoryStep(
            localId = "0",
            type = StoryTypes.TEXT.type,
            text = "You can use the text editor to create your documents",
        )

        val step3 = StoryStep(
            localId = "0",
            type = StoryTypes.TEXT.type,
            text = "Or you can import markdown files, you chose!",
        )

        val title1 = "Writeopia, welcome!"
        val title2 = "Write documents"
        val title3 = "Present"

        val step4 = StoryStep(
            localId = "0",
            type = StoryTypes.TEXT.type,
            text = "Generate automatically!",
        )

        return listOf(
            StoryStep(
                localId = "0",
                type = StoryTypes.TITLE.type,
                text = title1
            ),
            step1,
            StoryStep(
                localId = "0",
                type = StoryTypes.TEXT.type,
                text = title2,
                tags = setOf(TagInfo( Tag.H1))
            ),
            step2,
            step3,
            StoryStep(
                localId = "0",
                type = StoryTypes.TEXT.type,
                tags = setOf(TagInfo(Tag.H2)),
                text = title3,
            ),
            step4,
        ) to listOf(
            SlidePage(
                title = title1,
                content = listOf(step1)
            ),
            SlidePage(
                title = title2,
                content = listOf(step2, step3)
            ),
            SlidePage(
                title = title3,
                content = listOf(step4)
            ),
        )
    }
}
