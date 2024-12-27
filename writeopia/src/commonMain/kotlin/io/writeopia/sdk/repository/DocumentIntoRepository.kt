package io.writeopia.sdk.repository

import io.writeopia.sdk.model.document.DocumentInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface DocumentIntoRepository {
    fun listenForDocumentInfoById(
        id: String,
        coroutineScope: CoroutineScope
    ): Flow<DocumentInfo>
}
