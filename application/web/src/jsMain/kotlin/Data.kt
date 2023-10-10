import io.writeopia.sdk.models.id.GenerateId
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes


internal fun supermarketList(): Map<Int, StoryStep> = mapOf(
    0 to StoryStep(
        localId = GenerateId.generate(),
        type = StoryTypes.TITLE.type,
        text = "Supermarket List",
    ),
    1 to StoryStep(
        localId = GenerateId.generate(),
        type = StoryTypes.CHECK_ITEM.type,
        text = "Corn",
    ),
    2 to StoryStep(
        localId = GenerateId.generate(),
        type = StoryTypes.CHECK_ITEM.type,
        text = "Chicken",
    ),
    3 to StoryStep(
        localId = GenerateId.generate(),
        type = StoryTypes.CHECK_ITEM.type,
        text = "Olives",
    ),
    4 to StoryStep(
        localId = GenerateId.generate(),
        type = StoryTypes.CHECK_ITEM.type,
        text = "Soup",
    )
)