package br.com.leandroferreira.storyteller.utils

import br.com.leandroferreira.storyteller.model.GroupStep
import br.com.leandroferreira.storyteller.model.StoryStep
import br.com.leandroferreira.storyteller.model.StoryUnit

object StoryData {

    fun imageGroup(): List<StoryUnit> = buildList {
        add(
            GroupStep(
                id = "0",
                type = "group_image",
                localPosition = 0,
                steps = listOf(
                    StoryStep(
                        id = "1",
                        type = "image",
                        localPosition = 0,
                        parentId = "0"
                    ),

                    StoryStep(
                        id = "2",
                        type = "image",
                        localPosition = 0,
                        parentId = "0"
                    ),

                    StoryStep(
                        id = "3",
                        type = "image",
                        localPosition = 0,
                        parentId = "0"
                    )
                )
            )
        )
        add(
            StoryStep(
                id = "4",
                type = "image",
                localPosition = 1
            )
        )
    }

    fun imageGroupToDetach(): List<StoryUnit> = buildList {
        add(
            GroupStep(
                id = "0",
                type = "group_image",
                localPosition = 0,
                steps = listOf(
                    StoryStep(
                        id = "1",
                        type = "image",
                        localPosition = 0
                    ),
                    StoryStep(
                        id = "2",
                        type = "image",
                        localPosition = 0
                    ),

                    StoryStep(
                        id = "3",
                        type = "image",
                        localPosition = 1
                    )
                )
            )
        )
        add(
            StoryStep(
                id = "4",
                type = "image",
                localPosition = 1
            )
        )
    }


    fun imageStepsList(): List<StoryStep> = buildList {
        add(
            StoryStep(
                id = "1",
                type = "image",
                localPosition = 0
            )
        )
        add(
            StoryStep(
                id = "2",
                type = "image",
                localPosition = 0
            )
        )
        add(
            StoryStep(
                id = "3",
                type = "image",
                localPosition = 0
            )
        )
    }

    fun spaces(): List<StoryStep> = buildList {
        add(
            StoryStep(
                id = "1",
                type = "space",
                localPosition = 0
            )
        )
        add(
            StoryStep(
                id = "2",
                type = "space",
                localPosition = 1
            )
        )
        add(
            StoryStep(
                id = "2",
                type = "space",
                localPosition = 2
            )
        )
    }

    fun spacedImageStepsList(): List<StoryStep> = buildList {
        add(
            StoryStep(
                id = "1",
                type = "image",
                localPosition = 0
            )
        )
        add(
            StoryStep(
                id = "pamsdplams",
                type = "space",
                localPosition = 1
            )
        )
        add(
            StoryStep(
                id = "3",
                type = "image",
                localPosition = 2
            )
        )
        add(
            StoryStep(
                id = "askndpalsd",
                type = "space",
                localPosition = 3
            )
        )
        add(
            StoryStep(
                id = "5",
                type = "image",
                localPosition = 4
            )
        )
    }

    fun imagesInLine(): List<StoryStep> = buildList {
        add(
            StoryStep(
                id = "1",
                type = "image",
                localPosition = 0
            )
        )
        add(
            StoryStep(
                id = "2",
                type = "image",
                localPosition = 1
            )
        )
        add(
            StoryStep(
                id = "3",
                type = "image",
                localPosition = 3
            )
        )
        add(
            StoryStep(
                id = "4",
                type = "image",
                localPosition = 5
            )
        )
        add(
            StoryStep(
                id = "5",
                type = "image",
                localPosition = 7
            )
        )
    }

    fun messagesInLine(): List<StoryUnit> = buildList {
        add(
            StoryStep(
                id = "2",
                type = "message",
                text = "We arrived in Santiago!! \n\nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                localPosition = 0
            )
        )
        add(
            StoryStep(
                id = "4",
                type = "message",
                text = "And it was super awesome!! \n\nUt enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur?",
                localPosition = 1
            )
        )
        add(
            StoryStep(
                id = "6",
                type = "message",
                text = "I couldn't believe how sunny it was. Santiago is a really beautiful city. There's a lot to " +
                    "do and enjoyed the day. We went to many cozy Cafes and we enjoyed the city by foot. " +
                    "We had to buy some Syn Cards to be able to communicate in the new country. ",
                localPosition = 2
            )
        )
        add(
            StoryStep(
                id = "7",
                type = "message",
                text = "I couldn't believe how sunny it was. Santiago is a really beautiful city. There's a lot to " +
                    "do and enjoyed the day. We went to many cozy Cafes and we enjoyed the city by foot. " +
                    "We had to buy some Syn Cards to be able to communicate in the new country. ",
                localPosition = 3
            )
        )
        add(
            StoryStep(
                id = "8",
                type = "message",
                text = "I couldn't believe how sunny it was. Santiago is a really beautiful city. There's a lot to " +
                    "do and enjoyed the day. We went to many cozy Cafes and we enjoyed the city by foot. " +
                    "We had to buy some Syn Cards to be able to communicate in the new country. ",
                localPosition = 4
            )
        )
    }

    fun messageStepsList(): List<StoryStep> = buildList {
        add(
            StoryStep(
                id = "1",
                type = "message",
                localPosition = 0
            )
        )
        add(
            StoryStep(
                id = "2",
                type = "message",
                localPosition = 0
            )
        )
        add(
            StoryStep(
                id = "3",
                type = "message",
                localPosition = 0
            )
        )
    }

    fun stepsList(): List<StoryStep> = buildList {
        add(
            StoryStep(
                id = "1",
                type = "image",
                localPosition = 0
            )
        )
        add(
            StoryStep(
                id = "2",
                type = "image",
                localPosition = 0
            )
        )
        add(
            StoryStep(
                id = "3",
                type = "image",
                localPosition = 0
            )
        )
        add(
            StoryStep(
                id = "4",
                type = "message",
                localPosition = 4
            )
        )
        add(
            StoryStep(
                id = "5",
                type = "message",
                localPosition = 5
            )
        )
    }

    fun complexList() =
        buildList {
            add(
                StoryStep(
                    id = "-2",
                    type = "image",
                    url = "https://fastly.picsum.photos/id/15/400/400.jpg?hmac=xv-6mggpYPLIQ9eNAHrl1qKPHjyUCYlBoNBvdsqF4cY",
                    localPosition = -2,
                )
            )
            add(
                StoryStep(
                    id = "-1",
                    type = "image",
                    url = "https://fastly.picsum.photos/id/15/400/400.jpg?hmac=xv-6mggpYPLIQ9eNAHrl1qKPHjyUCYlBoNBvdsqF4cY",
                    localPosition = -1,
                )
            )
            add(
                StoryStep(
                    id = "0",
                    type = "image",
                    url = "https://fastly.picsum.photos/id/15/400/400.jpg?hmac=xv-6mggpYPLIQ9eNAHrl1qKPHjyUCYlBoNBvdsqF4cY",
                    localPosition = 0,
                )
            )
            add(
                StoryStep(
                    id = "1",
                    type = "image",
                    url = "https://fastly.picsum.photos/id/1018/400/400.jpg?hmac=MwHJoMaVXsBbqg-LFoDVL6P8TCDkSEikExptCkkHESQ",
                    localPosition = 0
                )
            )
            add(
                StoryStep(
                    id = "2",
                    type = "image",
                    url = "https://fastly.picsum.photos/id/984/400/400.jpg?hmac=CaqZ-rcUAbmidwURZcBynO7aIAC-FaktVN7X8lIvlmE",
                    localPosition = 0,
                )
            )
            add(
                StoryStep(
                    id = "2",
                    type = "message",
                    text = "We arrived in Santiago!! \n\nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                    localPosition = 2
                )
            )
            add(
                StoryStep(
                    id = "3",
                    type = "image",
                    url = "https://fastly.picsum.photos/id/514/1200/600.jpg?hmac=gh5_PZFkQI74GShPTCJ_XP_EgN-X1O0OUP8tDlT7WkY",
                    localPosition = 3,
                    title = "The hotel entrance"
                )
            )
            add(
                StoryStep(
                    id = "4",
                    type = "message",
                    text = "And it was super awesome!! \n\nUt enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur?",
                    localPosition = 4
                )
            )
            add(
                StoryStep(
                    id = "6",
                    type = "message",
                    text = "I couldn't believe how sunny it was. Santiago is a really beautiful city. There's a lot to " +
                        "do and enjoyed the day. We went to many cozy Cafes and we enjoyed the city by foot. " +
                        "We had to buy some Syn Cards to be able to communicate in the new country. ",
                    localPosition = 6
                )
            )
        }

}
