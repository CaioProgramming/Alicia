package com.ilustris.alicia.utils

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

@Composable
fun OutLineText(
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    width: Dp,
    strokeColor: Color,
    fontFamily: String?,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val sizeInPx = with(LocalDensity.current) { textStyle.fontSize.toPx() }
    val strokeWidthInPx = with(LocalDensity.current) { width.toPx() }
    // Create a Paint that has black stroke
    val textPaintStroke =
        Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            style = android.graphics.Paint.Style.STROKE
            textSize = sizeInPx
            color = strokeColor.toArgb()
            strokeWidth = strokeWidthInPx
            strokeMiter = 10f
            strokeJoin = android.graphics.Paint.Join.ROUND
            fontFamily?.let {
                typeface = android.graphics.Typeface.createFromAsset(context.assets, "fonts/$fontFamily.ttf")
            }
        }

    // Create a Paint that has white fill
    val textPaint =
        Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            style = android.graphics.Paint.Style.FILL
            textSize = sizeInPx
            color = textStyle.color.toArgb()
            fontFamily?.let {
                typeface = android.graphics.Typeface.createFromAsset(context.assets, "fonts/$fontFamily.ttf")
            }
        }

    // Create a canvas, draw the black stroke and
    // override it with the white fill
    Canvas(
        modifier = modifier.fillMaxSize(),
        onDraw = {
            drawIntoCanvas {
                val offset = Offset(it.nativeCanvas.width * 0.4f, it.nativeCanvas.height * 0.98f)
                it.nativeCanvas.drawText(
                    text,
                    offset.x,
                    offset.y,
                    textPaintStroke,
                )

                it.nativeCanvas.drawText(
                    text,
                    offset.x,
                    offset.y,
                    textPaint,
                )
            }
        },
    )
}