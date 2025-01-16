package com.ilustris.alicia.core.theme

import android.content.Context
import android.graphics.Matrix
import android.graphics.RectF
import android.text.TextUtils
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.reverse
import androidx.compose.ui.graphics.toAndroidRect
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.graphics.PathParser
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import com.ilustris.alicia.utils.SVGHelper
import java.util.regex.Pattern
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

class HexagonShape : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            path = drawCustomHexagonPath(size)
        )
    }
}

private fun drawCustomHexagonPath(size: Size): Path {
    return Path().apply {
        val radius = min(size.width / 2f, size.height / 2f)
        val triangleHeight = (sqrt(3.0) * radius / 2)
        val centerX = size.width / 2
        val centerY = size.height / 2

        moveTo(x = centerX, y = centerY + radius)
        lineTo(x = (centerX - triangleHeight).toFloat(), y = centerY + radius / 2)
        lineTo(x = (centerX - triangleHeight).toFloat(), y = centerY - radius / 2)
        lineTo(x = centerX, y = centerY - radius)
        lineTo(x = (centerX + triangleHeight).toFloat(), y = centerY - radius / 2)
        lineTo(x = (centerX + triangleHeight).toFloat(), y = centerY + radius / 2)

        close()
    }
}


class TriangleShape(private val reversed: Boolean = false) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            if (!reversed) {// Moves to top center position
                moveTo(size.width / 2f, 0f)
                // Add line to bottom right corner
                lineTo(size.width, size.height)

                // Add line to bottom left corner
                lineTo(0f, size.height)
            } else {
                moveTo(size.width / 2f, size.height)
                // Add line to bottom right corner
                lineTo(size.width, 0f)
                // Add line to bottom left corner
                lineTo(0f, 0f)
            }

            close()
        }
        return Outline.Generic(path)
    }

}

class Polygon(val sides: Int, val rotation: Float = 0f) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            Path().apply {
                val radius = if (size.width > size.height) size.width / 2f else size.height / 2f
                val angle = 2.0 * Math.PI / sides
                val cx = size.width / 2f
                val cy = size.height / 2f
                val r = rotation * (Math.PI / 180)
                moveTo(
                    cx + (radius * cos(0.0 + r).toFloat()),
                    cy + (radius * sin(0.0 + r).toFloat())
                )
                for (i in 1 until sides) {
                    lineTo(
                        cx + (radius * cos(angle * i + r).toFloat()),
                        cy + (radius * sin(angle * i + r).toFloat())
                    )
                }
                close()
            })
    }
}

class NameHolderShape() : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            moveTo(0f, 0f)

            lineTo(size.width, 0f)

            lineTo(size.width * 0.9f, size.height / 2)

            lineTo(size.width, size.height)

            lineTo(.0f, size.height)

            lineTo(size.width * 0.1f, size.height / 2)

            lineTo(0f, 0f)

            close()
        }
        return Outline.Generic(path)
    }

}

class NameHolderArcShape() : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            moveTo(0f, 0f)

            lineTo(size.width, 0f)

            lineTo(size.width * 0.9f, size.height / 2)

            lineTo(size.width, size.height)

            lineTo(.0f, size.height)

            lineTo(size.width * 0.1f, size.height / 2)

            lineTo(0f, 0f)

            close()
        }
        return Outline.Generic(path)
    }

}


abstract class PathShape(private val fileName: String, private val context: Context) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(drawPath(size))
    }


    private fun drawPath(size: Size): Path {
        return Path().apply {
            val pathData = SVGHelper.getPathFromFile(context,fileName, size)
            reset()
            pathData?.let {
                addPath(it)
            }
            close()
        }


    }
}

    class Flow(context: Context) : PathShape(SVGPaths.FLOW_PATH, context)

    class FancyPlate(context: Context) : PathShape(SVGPaths.FANCY_PLATE, context)

    class FireShape(context: Context) : PathShape(SVGPaths.FIRE_SHAPE, context)


    class SVGPaths {
        companion object {
            private const val SHAPES_PREFIX = "shapes"
            const val FLOW_PATH = "$SHAPES_PREFIX/medal.svg"
            const val FIRE_SHAPE = "$SHAPES_PREFIX/fire.svg"
            const val FANCY_PLATE = "$SHAPES_PREFIX/name_plate.svg"
        }
    }
