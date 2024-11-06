package io.writeopia.sdk.presentation.ui

import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.models.story.Tags
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

        val title1 = "Welcome!"
        val title2 = "Write"
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
                tags = setOf(Tags.H1.tag)
            ),
            step2,
            step3,
            StoryStep(
                localId = "0",
                type = StoryTypes.TEXT.type,
                tags = setOf(Tags.H2.tag),
                text = title3,
            ),
            step4,
        ) to listOf(
            SlidePage(
                title = title1,
                content = listOf(step1),
                imagePath = "https://fastly.picsum.photos/id/114/300/500.jpg?hmac=y8NqItdCPo4J2SrU34EwcnHSRDMZidgxJqccfX9urss"
            ),
            SlidePage(
                title = title2,
                content = listOf(step2, step3),
                imagePath = "https://fastly.picsum.photos/id/950/300/500.jpg?hmac=qqOdrbW-LrKU7geZfvLAyFRNxk1IrXaJh9_4i6P0J8s"
            ),
            SlidePage(
                title = title3,
                content = listOf(step4),
                imagePath = "https://fastly.picsum.photos/id/315/300/500.jpg?hmac=w7aP0ZALb-trZEVj0DM58mCXTUGY_bFAikqJvnxoAqg"
//                imagePath = "https://picsum.photos/300/500"
            ),
        )
    }
}
