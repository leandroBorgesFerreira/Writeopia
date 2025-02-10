package io.writeopia.editor.features.editor.viewmodel

import io.writeopia.commonui.dtos.MenuItemUi
import io.writeopia.editor.model.EditState
import io.writeopia.model.Font
import io.writeopia.sdk.models.span.Span
import io.writeopia.ui.backstack.BackstackHandler
import io.writeopia.ui.backstack.BackstackInform
import io.writeopia.ui.manager.WriteopiaStateManager
import io.writeopia.ui.model.DrawState
import kotlinx.coroutines.flow.StateFlow

interface NoteEditorViewModel : BackstackInform, BackstackHandler {

    val writeopiaManager: WriteopiaStateManager

    val isEditable: StateFlow<Boolean>

    val showGlobalMenu: StateFlow<Boolean>

    val editHeader: StateFlow<Boolean>

    val currentTitle: StateFlow<String>

    val shouldGoToNextScreen: StateFlow<Boolean>

    val isEditState: StateFlow<EditState>

    val scrollToPosition: StateFlow<Int?>

    val toDrawWithDecoration: StateFlow<DrawState>

    val documentToShareInfo: StateFlow<ShareDocument?>

    val fontFamily: StateFlow<Font>

    val listenForFolders: StateFlow<List<MenuItemUi.FolderUi>>

    fun toggleEditable()

    fun deleteSelection()

    fun handleBackAction(navigateBack: () -> Unit)

    fun onHeaderClick()

    fun createNewDocument(documentId: String, title: String)

    fun loadDocument(documentId: String)

    fun onHeaderColorSelection(color: Int?)

    fun onHeaderEditionCancel()

    fun onMoreOptionsClick()

    fun shareDocumentInJson()

    fun shareDocumentInMarkdown()

    fun onViewModelCleared()

    fun onAddSpanClick(span: Span)

    fun onAddCheckListClick()

    fun onAddListItemClick()

    fun onAddCodeBlockClick()

    fun onAddHighLightBlockClick()

    fun clearSelections()

    fun changeFontFamily(font: Font)

    fun addImage(imagePath: String)

    fun exportMarkdown(path: String)

    fun exportJson(path: String)

    fun expandFolder(folderId: String)

    fun moveToFolder(folderId: String)

    fun moveToRootFolder()

    fun askAiBySelection()

    fun addPage()
}

data class ShareDocument(val content: String, val title: String, val type: String)
