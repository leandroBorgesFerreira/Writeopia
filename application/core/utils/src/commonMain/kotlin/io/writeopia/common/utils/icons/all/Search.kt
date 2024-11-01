package io.writeopia.common.utils.icons.all
/*
* Converted using https://composables.com/svgtocompose
*/

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

internal val Search: ImageVector
    get() {
        if (_Search != null) {
            return _Search!!
        }
        _Search = ImageVector.Builder(
            name = "io.writeopia.common.utils.icons.all.getSearch",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFF000000)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(19f, 11f)
                arcTo(8f, 8f, 0f, isMoreThanHalf = false, isPositiveArc = true, 11f, 19f)
                arcTo(8f, 8f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3f, 11f)
                arcTo(8f, 8f, 0f, isMoreThanHalf = false, isPositiveArc = true, 19f, 11f)
                close()
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFF000000)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(21f, 21f)
                lineToRelative(-4.3f, -4.3f)
            }
        }.build()
        return _Search!!
    }

private var _Search: ImageVector? = null
