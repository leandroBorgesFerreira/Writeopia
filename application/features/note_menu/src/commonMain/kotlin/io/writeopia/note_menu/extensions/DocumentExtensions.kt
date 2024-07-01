package io.writeopia.note_menu.extensions

import io.writeopia.note_menu.data.model.Folder
import io.writeopia.note_menu.ui.dto.MenuItemUi
import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.document.MenuItem
import io.writeopia.sdk.preview.PreviewParser

internal fun MenuItem.toUiCard(
    previewParser: PreviewParser? = null,
    selected: Boolean = false,
    limit: Int = 0
): MenuItemUi =
    when (this) {
        is Folder -> {
            MenuItemUi.FolderUi(
                documentId = id,
                title = title,
                selected = selected,
                isFavorite = favorite,
                parentId = parentId,
            )
        }

        is Document -> {
            MenuItemUi.DocumentUi(
                documentId = id,
                title = title,
                lastEdit = "",
                preview = content.values.let { previewParser?.preview(it, limit) ?: emptyList() },
                selected = selected,
                isFavorite = favorite,
            )
        }

        else -> throw IllegalArgumentException("MenuItemUi could not me created")
    }
