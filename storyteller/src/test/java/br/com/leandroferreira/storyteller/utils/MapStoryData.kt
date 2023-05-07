package br.com.leandroferreira.storyteller.utils

import br.com.leandroferreira.storyteller.model.story.GroupStep
import br.com.leandroferreira.storyteller.model.story.StepType
import br.com.leandroferreira.storyteller.model.story.StoryStep
import br.com.leandroferreira.storyteller.model.story.StoryUnit
import java.util.UUID

object MapStoryData {

    fun imageGroup(): Map<Int, StoryUnit> = mapOf(
        0 to GroupStep(
            id = "0",
            type = "group_image",
            steps = listOf(
                StoryStep(
                    id = "1",
                    type = "image",
                    parentId = "0"
                ),

                StoryStep(
                    id = "2",
                    type = "image",
                    parentId = "0"
                ),

                StoryStep(
                    id = "3",
                    type = "image",
                    parentId = "0"
                )
            )
        ),
        1 to StoryStep(
            id = "4",
            type = "image",
        )
    )

    fun imageGroupToDetach(): Map<Int, StoryUnit> = mapOf(
        0 to GroupStep(
            id = "0",
            type = "group_image",
            steps = listOf(
                StoryStep(
                    id = "1",
                    type = "image",
                ),
                StoryStep(
                    id = "2",
                    type = "image",
                ),

                StoryStep(
                    id = "3",
                    type = "image",
                )
            )
        ),
        1 to StoryStep(
            id = "4",
            type = "image",
        )
    )

    fun spaces(): Map<Int, StoryUnit> = mapOf(
        0 to StoryStep(
            id = "1",
            type = "space",
        ),
        1 to StoryStep(
            id = "2",
            type = "space",
        ),
        2 to StoryStep(
            id = "2",
            type = "space",
        )
    )

    fun spacedImageStepsList(): Map<Int, StoryUnit> = mapOf(
        0 to StoryStep(
            id = "1",
            type = "image",
        ),
        1 to StoryStep(
            id = "pamsdplams",
            type = "space",
        ),
        2 to StoryStep(
            id = "3",
            type = "image",
        ),
        3 to StoryStep(
            id = "askndpalsd",
            type = "space",
        ),
        4 to StoryStep(
            id = "5",
            type = "image",
        )
    )

    fun imageStepsList(): Map<Int, StoryUnit> = mapOf(
        0 to StoryStep(
            id = "1",
            type = "image",
        ),
        1 to StoryStep(
            id = "2",
            type = "image",
        ),
        2 to StoryStep(
            id = "3",
            type = "image",
        ),
    )

    fun imageSimpleGroup(): Map<Int, List<StoryUnit>> = mapOf(
        0 to listOf(
            StoryStep(
                id = "1",
                type = "image",
            ),
            StoryStep(
                id = "2",
                type = "image",
            ),
            StoryStep(
                id = "3",
                type = "image",
            ),
        )
    )

    fun messageStepsList(): Map<Int, StoryUnit> = mapOf(
        0 to StoryStep(
            id = "1",
            type = "message",
        ),
        1 to StoryStep(
            id = "2",
            type = "message",
        ),
        2 to StoryStep(
            id = "3",
            type = "message",
        )
    )

    fun twoGroupsImageList(): Map<Int, List<StoryUnit>> {
        val parent1Id = UUID.randomUUID().toString()
        val parent2Id = UUID.randomUUID().toString()

        return mapOf(
            0 to listOf(
                GroupStep(
                    id = parent1Id,
                    type = StepType.GROUP_IMAGE.type,
                    steps = listOf(
                        StoryStep(
                            id = "1",
                            type = "image",
                            parentId = parent1Id
                        ),
                        StoryStep(
                            id = "2",
                            type = "image",
                            parentId = parent1Id
                        ),
                        StoryStep(
                            id = "3",
                            type = "image",
                            parentId = parent1Id
                        )
                    )
                ),
                GroupStep(
                    id = parent2Id,
                    type = StepType.GROUP_IMAGE.type,
                    steps = listOf(
                        StoryStep(
                            id = "12",
                            type = "image",
                             parentId = parent2Id
                        ),
                        StoryStep(
                            id = "12",
                            type = "image",
                             parentId = parent2Id
                        ),
                        StoryStep(
                            id = "13",
                            type = "image",
                             parentId = parent2Id
                        ),
                    )
                )
            ),
        )
    }

    fun stepsList(): Map<Int, List<StoryStep>> = mapOf(
        0 to listOf(
            StoryStep(
                id = "1",
                type = "image",
            ),
            StoryStep(
                id = "2",
                type = "image",
            ),
            StoryStep(
                id = "3",
                type = "image",
            )
        ),
        1 to listOf(
            StoryStep(
                id = "4",
                type = "message",
            )
        ),
        2 to listOf(
            StoryStep(
                id = "5",
                type = "message",
            )
        )
    )

    fun singleMessage(): Map<Int, StoryUnit> = mapOf(
        0 to StoryStep(
            id = "0",
            type = "message",
            text = "hi!",
        )
    )

    fun messagesInLine(): Map<Int, StoryUnit> = mapOf(
        0 to StoryStep(
            id = "0",
            type = "message",
            text = "hi!",

            ),
        1 to StoryStep(
            id = "2",
            type = "message",
            text = "Hey!",

            ),
        2 to StoryStep(
            id = "4",
            type = "message",
            text = "And it was super awesome!! Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur?",

            ),
        3 to StoryStep(
            id = "6",
            type = "message",
            text = "I couldn't believe how sunny it was. Santiago is a really beautiful city. There's a lot to " +
                "do and enjoyed the day. We went to many cozy Cafes and we enjoyed the city by foot. " +
                "We had to buy some Syn Cards to be able to communicate in the new country. ",

            ),
        4 to StoryStep(
            id = "7",
            type = "message",
            text = "I couldn't believe how sunny it was. Santiago is a really beautiful city. There's a lot to " +
                "do and enjoyed the day. We went to many cozy Cafes and we enjoyed the city by foot. " +
                "We had to buy some Syn Cards to be able to communicate in the new country. ",

            ),
        5 to StoryStep(
            id = "8",
            type = "message",
            text = "I couldn't believe how sunny it was. Santiago is a really beautiful city. There's a lot to " +
                "do and enjoyed the day. We went to many cozy Cafes and we enjoyed the city by foot. " +
                "We had to buy some Syn Cards to be able to communicate in the new country. ",

            )
    )

    fun imagesInLine(): Map<Int, StoryStep> = mapOf(
        0 to
            StoryStep(
                id = "1",
                type = "image",
            ),
        1 to
            StoryStep(
                id = "2",
                type = "image",
            ),
        2 to
            StoryStep(
                id = "3",
                type = "image",
            ),
        3 to
            StoryStep(
                id = "4",
                type = "image",
            ),
        4 to
            StoryStep(
                id = "5",
                type = "image",
            )
    )

    fun complexList(): Map<Int, List<StoryUnit>> = mapOf(
        0 to listOf(
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "image",
                url = "https://fastly.picsum.photos/id/15/400/400.jpg?hmac=xv-6mggpYPLIQ9eNAHrl1qKPHjyUCYlBoNBvdsqF4cY",
            ),
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "image",
                url = "https://fastly.picsum.photos/id/15/400/400.jpg?hmac=xv-6mggpYPLIQ9eNAHrl1qKPHjyUCYlBoNBvdsqF4cY",
            ),
        ),
        1 to listOf(
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "image",
                url = "https://fastly.picsum.photos/id/15/400/400.jpg?hmac=xv-6mggpYPLIQ9eNAHrl1qKPHjyUCYlBoNBvdsqF4cY",
            )
        ),
        2 to listOf(
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "image",
                url = "https://fastly.picsum.photos/id/15/400/400.jpg?hmac=xv-6mggpYPLIQ9eNAHrl1qKPHjyUCYlBoNBvdsqF4cY",
            ),
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "image",
                url = "https://fastly.picsum.photos/id/15/400/400.jpg?hmac=xv-6mggpYPLIQ9eNAHrl1qKPHjyUCYlBoNBvdsqF4cY",
            ),
            StoryStep(
                id = "2",
                type = "image",
                url = "https://fastly.picsum.photos/id/984/400/400.jpg?hmac=CaqZ-rcUAbmidwURZcBynO7aIAC-FaktVN7X8lIvlmE",
            )
        ),
        3 to listOf(
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "message",
                text = "We arrived in Santiago!! \n\nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
            )
        ),
        4 to listOf(
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "image",
                url = "https://fastly.picsum.photos/id/514/1200/600.jpg?hmac=gh5_PZFkQI74GShPTCJ_XP_EgN-X1O0OUP8tDlT7WkY",
                title = "The hotel entrance"
            )
        ),
        5 to listOf(
            StoryStep(
                id = UUID.randomUUID().toString(),
                type = "image",
                text = "And it was super awesome!! \n\nUt enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur?",
            )
        ),
        6 to listOf(
            StoryStep(
                id = "6",
                type = "message",
                text = "I couldn't believe how sunny it was. Santiago is a really beautiful city. There's a lot to " +
                    "do and enjoyed the day. We went to many cozy Cafes and we enjoyed the city by foot. " +
                    "We had to buy some Syn Cards to be able to communicate in the new country. ",
            )
        )
    )
}
