package io.storiesteller.note_menu.extensions

import io.storiesteller.note_menu.ui.dto.DocumentUi
import io.storiesteller.sdk.models.document.Document
import io.storiesteller.sdk.preview.PreviewParser

internal fun Document.toUiCard(
    previewParser: PreviewParser,
    selected: Boolean = false
): DocumentUi =
    DocumentUi(
        documentId = id,
        title = title,
        lastEdit = "",
        preview = content.values.let(previewParser::preview),
        selected = selected
    )