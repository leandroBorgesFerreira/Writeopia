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

internal val BellDot: ImageVector
	get() {
		if (_BellDot != null) {
			return _BellDot!!
		}
		_BellDot = ImageVector.Builder(
            name = "io.writeopia.common.utils.icons.all.getBellDot",
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
				moveTo(19.4f, 14.9f)
				curveTo(20.20f, 16.40f, 210f, 170f, 210f, 170f)
				horizontalLineTo(3f)
				reflectiveCurveToRelative(3f, -2f, 3f, -9f)
				curveToRelative(00f, -3.30f, 2.70f, -60f, 60f, -60f)
				curveToRelative(0.70f, 00f, 1.30f, 0.10f, 1.90f, 0.30f)
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
				moveTo(10.3f, 21f)
				arcToRelative(1.94f, 1.94f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3.4f, 0f)
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
				moveTo(21f, 8f)
				arcTo(3f, 3f, 0f, isMoreThanHalf = false, isPositiveArc = true, 18f, 11f)
				arcTo(3f, 3f, 0f, isMoreThanHalf = false, isPositiveArc = true, 15f, 8f)
				arcTo(3f, 3f, 0f, isMoreThanHalf = false, isPositiveArc = true, 21f, 8f)
				close()
			}
		}.build()
		return _BellDot!!
	}

private var _BellDot: ImageVector? = null
