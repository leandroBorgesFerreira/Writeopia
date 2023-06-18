package br.com.leandroferreira.app_sample.extensions

import br.com.leandroferreira.app_sample.screens.menu.ui.dto.DocumentCard
import com.github.leandroborgesferreira.storyteller.model.document.Document
import com.github.leandroborgesferreira.storyteller.parse.PreviewParser

fun Document.toUiCard(previewParser: PreviewParser): DocumentCard =
    DocumentCard(
        documentId = id,
        title = title,
        lastEdit = "",
        preview = content?.values?.let(previewParser::preview) ?: emptyList()
    )