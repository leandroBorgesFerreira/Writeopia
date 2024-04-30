package io.writeopia.sdk.sharededition

import io.writeopia.sdk.model.document.DocumentInfo
import io.writeopia.sdk.model.story.StoryState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface SharedEditionManager {

    suspend fun startLiveEdition(
        inFlow: Flow<Pair<StoryState, DocumentInfo>>,
        outFlow: MutableStateFlow<StoryState>
    )

    suspend fun stopLiveEdition()
}
