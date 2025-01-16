package com.ilustris.alicia.utils

import android.content.Context
import android.graphics.Matrix
import android.graphics.RectF
import android.text.TextUtils
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.toAndroidRect
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader


object SVGHelper  {

    fun getPathFromFile(context: Context, filePath: String, size: Size): Path? {
        val svgContent = readSvgFile(filePath, context)
        val pathData = extractPathData(svgContent)
        return pathData.firstOrNull()?.toPath(size)
    }



    fun readSvgFile(filePath: String, context: Context): String {
        return context.readAssetFile(filePath)
    }

    fun extractPathData(svgContent: String): List<String> {
        val pathDataPattern = """d="([^"]*)"""".toRegex()
        return pathDataPattern.findAll(svgContent).map { it.groupValues[1] }.toList()
    }

    fun String?.toPath(size: Size?, pathDestination: Path? = null): Path? {
        this?.let {
            size?.let {
                if (!TextUtils.isEmpty(this)) {
                    val pathDestinationResult = pathDestination ?: kotlin.run {
                        Path()
                    }
                    val scaleMatrix = Matrix()
                    val rectF = RectF()
                    val path =
                        androidx.compose.ui.graphics.vector.PathParser().parsePathString(this)
                            .toPath(pathDestinationResult)
                    val rectPath = path.getBounds().toAndroidRect()
                    val scaleXFactor = size.width / rectPath.width().toFloat()
                    val scaleYFactor = size.height / rectPath.height().toFloat()
                    val androidPath = path.asAndroidPath()
                    scaleMatrix.setScale(
                        scaleXFactor,
                        scaleYFactor,
                        rectF.centerX(),
                        rectF.centerY()
                    )
                    androidPath.computeBounds(rectF, true)
                    androidPath.transform(scaleMatrix)
                    return androidPath.asComposePath()
                }
            }
        }
        return null
    }
}
