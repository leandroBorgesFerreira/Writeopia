package io.writeopia.sdk.model.draw

/**
 * The class holds the information of the content to be draw by the SDK.
 *
 * @param editable if the content can the edited
 * @param focusId which content has the focus
 * @param position the position of the content
 * @param extraData additional data
 * @param selectMode if the content is current being selected and should show a visible feedback of it.
 *
 */
data class DrawInfo(
    val editable: Boolean = true,
    val focusId: String? = null,
    val position: Int = 0,
    val extraData: Map<String, Any> = emptyMap(),
    val selectMode: Boolean = false,
//    val textRange: TextRange //Todo: Populate this!!
)
