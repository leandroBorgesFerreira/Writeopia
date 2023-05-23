package com.github.leandroferreira.storyteller.utils

import com.github.leandroferreira.storyteller.model.story.GroupStep
import com.github.leandroferreira.storyteller.model.story.StoryStep
import com.github.leandroferreira.storyteller.model.story.StoryUnit

object ListStoryData {

    fun imageGroup(): List<StoryUnit> = buildList {
        add(
            GroupStep(
                localId = "0",
                type = "group_image",
                steps = listOf(
                    StoryStep(
                        localId = "1",
                        type = "image",
                        parentId = "0"
                    ),

                    StoryStep(
                        localId = "2",
                        type = "image",
                        parentId = "0"
                    ),

                    StoryStep(
                        localId = "3",
                        type = "image",
                        parentId = "0"
                    )
                )
            )
        )
        add(
            StoryStep(
                localId = "4",
                type = "image",
            )
        )
    }

    fun imageGroupToDetach(): List<StoryUnit> = buildList {
        add(
            GroupStep(
                localId = "0",
                type = "group_image",
                steps = listOf(
                    StoryStep(
                        localId = "1",
                        type = "image",
                    ),
                    StoryStep(
                        localId = "2",
                        type = "image",
                    ),

                    StoryStep(
                        localId = "3",
                        type = "image",
                    )
                )
            )
        )
        add(
            StoryStep(
                localId = "4",
                type = "image",
            )
        )
    }


    fun imageStepsList(): List<StoryStep> = buildList {
        add(
            StoryStep(
                localId = "1",
                type = "image",
            )
        )
        add(
            StoryStep(
                localId = "2",
                type = "image",
            )
        )
        add(
            StoryStep(
                localId = "3",
                type = "image",
            )
        )
    }

    fun spaces(): List<StoryStep> = buildList {
        add(
            StoryStep(
                localId = "1",
                type = "space",
            )
        )
        add(
            StoryStep(
                localId = "2",
                type = "space",
            )
        )
        add(
            StoryStep(
                localId = "2",
                type = "space",
            )
        )
    }

    fun spacedImageStepsList(): List<StoryStep> = buildList {
        add(
            StoryStep(
                localId = "1",
                type = "image",
            )
        )
        add(
            StoryStep(
                localId = "pamsdplams",
                type = "space",
            )
        )
        add(
            StoryStep(
                localId = "3",
                type = "image",
            )
        )
        add(
            StoryStep(
                localId = "askndpalsd",
                type = "space",
            )
        )
        add(
            StoryStep(
                localId = "5",
                type = "image",
            )
        )
    }

    fun imagesInLine(): List<StoryStep> = buildList {
        add(
            StoryStep(
                localId = "1",
                type = "image",
            )
        )
        add(
            StoryStep(
                localId = "2",
                type = "image",
            )
        )
        add(
            StoryStep(
                localId = "3",
                type = "image",
            )
        )
        add(
            StoryStep(
                localId = "4",
                type = "image",
            )
        )
        add(
            StoryStep(
                localId = "5",
                type = "image",
            )
        )
    }

    fun singleMessage(): List<StoryUnit> = listOf(
        StoryStep(
            localId = "0",
            type = "message",
            text = "hi!",
        )
    )

    fun messagesInLine(): List<StoryUnit> = buildList {
        add(
            StoryStep(
                localId = "0",
                type = "message",
                text = "hi!",
            )
        )
        add(
            StoryStep(
                localId = "2",
                type = "message",
                text = "Hey!",
            )
        )
        add(
            StoryStep(
                localId = "4",
                type = "message",
                text = "And it was super awesome!! Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur?",
            )
        )
        add(
            StoryStep(
                localId = "6",
                type = "message",
                text = "I couldn't believe how sunny it was. Santiago is a really beautiful city. There's a lot to " +
                    "do and enjoyed the day. We went to many cozy Cafes and we enjoyed the city by foot. " +
                    "We had to buy some Syn Cards to be able to communicate in the new country. ",
            )
        )
        add(
            StoryStep(
                localId = "7",
                type = "message",
                text = "I couldn't believe how sunny it was. Santiago is a really beautiful city. There's a lot to " +
                    "do and enjoyed the day. We went to many cozy Cafes and we enjoyed the city by foot. " +
                    "We had to buy some Syn Cards to be able to communicate in the new country. ",
            )
        )
        add(
            StoryStep(
                localId = "8",
                type = "message",
                text = "I couldn't believe how sunny it was. Santiago is a really beautiful city. There's a lot to " +
                    "do and enjoyed the day. We went to many cozy Cafes and we enjoyed the city by foot. " +
                    "We had to buy some Syn Cards to be able to communicate in the new country. ",
            )
        )
    }

    fun messageStepsList(): List<StoryStep> = buildList {
        add(
            StoryStep(
                localId = "1",
                type = "message",
            )
        )
        add(
            StoryStep(
                localId = "2",
                type = "message",
            )
        )
        add(
            StoryStep(
                localId = "3",
                type = "message",
            )
        )
    }

    fun stepsList(): List<StoryStep> = buildList {
        add(
            StoryStep(
                localId = "1",
                type = "image",
            )
        )
        add(
            StoryStep(
                localId = "2",
                type = "image",
            )
        )
        add(
            StoryStep(
                localId = "3",
                type = "image",
            )
        )
        add(
            StoryStep(
                localId = "4",
                type = "message",
            )
        )
        add(
            StoryStep(
                localId = "5",
                type = "message",
            )
        )
    }

    fun complexList() =
        buildList {
            add(
                StoryStep(
                    localId = "-2",
                    type = "image",
                    url = "https://fastly.picsum.photos/id/15/400/400.jpg?hmac=xv-6mggpYPLIQ9eNAHrl1qKPHjyUCYlBoNBvdsqF4cY",
                )
            )
            add(
                StoryStep(
                    localId = "-1",
                    type = "image",
                    url = "https://fastly.picsum.photos/id/15/400/400.jpg?hmac=xv-6mggpYPLIQ9eNAHrl1qKPHjyUCYlBoNBvdsqF4cY",
                )
            )
            add(
                StoryStep(
                    localId = "0",
                    type = "image",
                    url = "https://fastly.picsum.photos/id/15/400/400.jpg?hmac=xv-6mggpYPLIQ9eNAHrl1qKPHjyUCYlBoNBvdsqF4cY",
                )
            )
            add(
                StoryStep(
                    localId = "1",
                    type = "image",
                    url = "https://fastly.picsum.photos/id/1018/400/400.jpg?hmac=MwHJoMaVXsBbqg-LFoDVL6P8TCDkSEikExptCkkHESQ",
                )
            )
            add(
                StoryStep(
                    localId = "2",
                    type = "image",
                    url = "https://fastly.picsum.photos/id/984/400/400.jpg?hmac=CaqZ-rcUAbmidwURZcBynO7aIAC-FaktVN7X8lIvlmE",
                )
            )
            add(
                StoryStep(
                    localId = "2",
                    type = "message",
                    text = "We arrived in Santiago!! \n\nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                )
            )
            add(
                StoryStep(
                    localId = "3",
                    type = "image",
                    url = "https://fastly.picsum.photos/id/514/1200/600.jpg?hmac=gh5_PZFkQI74GShPTCJ_XP_EgN-X1O0OUP8tDlT7WkY",
                    title = "The hotel entrance"
                )
            )
            add(
                StoryStep(
                    localId = "4",
                    type = "message",
                    text = "And it was super awesome!! \n\nUt enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur?",
                )
            )
            add(
                StoryStep(
                    localId = "6",
                    type = "message",
                    text = "I couldn't believe how sunny it was. Santiago is a really beautiful city. There's a lot to " +
                        "do and enjoyed the day. We went to many cozy Cafes and we enjoyed the city by foot. " +
                        "We had to buy some Syn Cards to be able to communicate in the new country. ",
                )
            )
        }

}
