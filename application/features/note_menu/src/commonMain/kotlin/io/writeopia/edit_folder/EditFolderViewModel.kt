package io.writeopia.edit_folder

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import io.writeopia.common.utils.KmpViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class EditFolderViewModel: KmpViewModel() {

    private val _nameState = MutableStateFlow("")
    private val _colorState = MutableStateFlow(Color.White.toArgb())

    fun createFolder() {

    }

    fun updateFolderName(name: String) {

    }

    fun updateColor() {

    }
}
