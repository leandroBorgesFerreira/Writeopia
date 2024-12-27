package io.writeopia.ui.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.FormatColorReset
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import io.writeopia.ui.icons.all.ALargeSmall
import io.writeopia.ui.icons.all.ArrowDownAZ
import io.writeopia.ui.icons.all.ArrowDownUp
import io.writeopia.ui.icons.all.Bold
import io.writeopia.ui.icons.all.Bolt
import io.writeopia.ui.icons.all.CalendarArrowDown
import io.writeopia.ui.icons.all.ChevronDown
import io.writeopia.ui.icons.all.ChevronRight
import io.writeopia.ui.icons.all.ChevronUp
import io.writeopia.ui.icons.all.CircleArrowLeft
import io.writeopia.ui.icons.all.CircleArrowRight
import io.writeopia.ui.icons.all.CirclePlus
import io.writeopia.ui.icons.all.ClockArrowDown
import io.writeopia.ui.icons.all.Contrast
import io.writeopia.ui.icons.all.Crosshair
import io.writeopia.ui.icons.all.Eye
import io.writeopia.ui.icons.all.EyeClosed
import io.writeopia.ui.icons.all.FileDown
import io.writeopia.ui.icons.all.FileMinus
import io.writeopia.ui.icons.all.FileUp
import io.writeopia.ui.icons.all.Files
import io.writeopia.ui.icons.all.FolderOpen
import io.writeopia.ui.icons.all.FolderSync
import io.writeopia.ui.icons.all.House
import io.writeopia.ui.icons.all.Italic
import io.writeopia.ui.icons.all.LayoutGrid
import io.writeopia.ui.icons.all.LayoutPanelLeft
import io.writeopia.ui.icons.all.Moon
import io.writeopia.ui.icons.all.MoveLeft
import io.writeopia.ui.icons.all.NotebookText
import io.writeopia.ui.icons.all.Play
import io.writeopia.ui.icons.all.Plus
import io.writeopia.ui.icons.all.Redo2
import io.writeopia.ui.icons.all.Rows3
import io.writeopia.ui.icons.all.Save
import io.writeopia.ui.icons.all.Search
import io.writeopia.ui.icons.all.SquareCheck
import io.writeopia.ui.icons.all.SquareCode
import io.writeopia.ui.icons.all.Sun
import io.writeopia.ui.icons.all.Underline
import io.writeopia.ui.icons.all.Undo2
import io.writeopia.ui.icons.all.X

object WrSdkIcons {

    val checkbox: ImageVector = SquareCheck

    val list: ImageVector = io.writeopia.ui.icons.all.List

    val close: ImageVector = Icons.Outlined.Close

    val smallArrowRight: ImageVector = ChevronRight

    val smallArrowDown: ImageVector = ChevronDown

    val smallArrowUp: ImageVector = ChevronUp

    val settings: ImageVector = Bolt

    val home: ImageVector = House

    val search: ImageVector = Search

    val notifications: ImageVector = Icons.Outlined.Notifications

    val favorites: ImageVector = Icons.Outlined.FavoriteBorder

    val file: ImageVector = FileMinus

    val fileDownload: ImageVector = FileDown

    val folder: ImageVector = FolderOpen

    val exportFile: ImageVector = FileUp

    val save: ImageVector = Save

    val sync: ImageVector = FolderSync

    val moreVert: ImageVector = Icons.Outlined.MoreVert

    val moreHoriz: ImageVector = Icons.Outlined.MoreHoriz

    val sort: ImageVector = ArrowDownUp

    val colorModeLight: ImageVector = Sun

    val colorModeDark: ImageVector = Moon

    val colorModeSystem: ImageVector = Contrast

    val bold: ImageVector = Bold

    val italic: ImageVector = Italic

    val underline: ImageVector = Underline

    val textStyle: ImageVector = ALargeSmall

    val pageStyle: ImageVector = NotebookText

    val code: ImageVector = SquareCode

    val addCircle: ImageVector = CirclePlus

    val add: ImageVector = Plus

    val target: ImageVector = Crosshair

    val layoutStaggeredGrid: ImageVector = LayoutPanelLeft

    val layoutGrid: ImageVector = LayoutGrid

    val layoutList: ImageVector = Rows3

    val copy: ImageVector = Files

    val delete: ImageVector = Icons.Outlined.DeleteOutline

    val undo: ImageVector = Undo2

    val redo: ImageVector = Redo2

    val visibilityOff = EyeClosed

    val sortByName: ImageVector = ArrowDownAZ

    val sortByCreated: ImageVector = CalendarArrowDown

    val sortByUpdate: ImageVector = ClockArrowDown

    val backArrowDesktop: ImageVector = MoveLeft

    val backArrowAndroid: ImageVector = Icons.AutoMirrored.Filled.ArrowBack

    val play: ImageVector = Play

    val circularArrowLeft = CircleArrowLeft

    val circularArrowRight = CircleArrowRight

    val visibilityOn = Eye

    val transparent: ImageVector = Icons.Outlined.FormatColorReset

    val person: ImageVector = Icons.Outlined.Person

    val allIcons: Map<String, ImageVector> =
        mapOf(
            "settings" to settings,
            "home" to home,
            "search" to search,
            "notifications" to notifications,
            "favorites" to favorites,
            "file" to file,
            "fileDownload" to fileDownload,
            "folder" to folder,
            "exportFile" to exportFile,
            "save" to save,
            "sync" to sync,
            "moreVert" to moreVert,
            "moreHoriz" to moreHoriz,
            "sort" to sort,
            "colorModeLight" to colorModeLight,
            "colorModeDark" to colorModeDark,
            "colorModeSystem" to colorModeSystem,
            "bold" to bold,
            "italic" to italic,
            "underline" to underline,
            "textStyle" to textStyle,
            "pageStyle" to pageStyle,
            "code" to code,
            "addCircle" to addCircle,
            "add" to add,
            "target" to target,
            "layoutStaggeredGrid" to layoutStaggeredGrid,
            "layoutGrid" to layoutGrid,
            "layoutList" to layoutList,
            "copy" to copy,
            "close" to close,
            "delete" to delete,
            "transparent" to transparent,
            "person" to person,
            "smallArrowRight" to smallArrowRight,
            "smallArrowDown" to smallArrowDown,
            "undo" to undo,
            "redo" to redo,
            "sortByName" to sortByName,
            "sortByCreated" to sortByCreated,
            "sortByUpdate" to sortByUpdate,
            "backArrowDesktop" to backArrowDesktop,
            "backArrowAndroid" to backArrowAndroid,
            "play" to play,
            "circularArrowLeft" to circularArrowLeft,
            "circularArrowRight" to circularArrowRight,
            "visibilityOn" to visibilityOn,
            "visibilityOff" to visibilityOff
        )

    fun fromName(name: String): ImageVector? = allIcons[name]

}
