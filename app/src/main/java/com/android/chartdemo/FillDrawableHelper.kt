package com.android.chartdemo

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Drawable
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.DataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.Transformer
import kotlin.math.max
import kotlin.math.min

class FillDrawableHelper(
    private val lineChart: LineChart,
    private val dataSet: LineDataSet,
    private val gradientRed: Drawable?,
    private val gradientBlue: Drawable?,
    private val gradientYellow: Drawable?
) : Drawable() {
    override fun draw(canvas: Canvas) {

        val entries = dataSet.values // Lấy danh sách các điểm dữ liệu từ dataset
        val minx = entries.firstOrNull()?.x ?: 0f // Lấy giá trị x của điểm dữ liệu đầu tiên hoặc 0 nếu danh sách trống
        val maxx = entries.lastOrNull()?.x ?: 0f // Lấy giá trị x của điểm dữ liệu cuối cùng hoặc 0 nếu danh sách trống

        val entryFrom = dataSet.getEntryForXValue(minx, Float.NaN, DataSet.Rounding.DOWN)
        val entryTo = dataSet.getEntryForXValue(maxx, Float.NaN, DataSet.Rounding.UP)

        // Draw filled part of line chart
        var currentEntry: Entry?
        var nextEntry: Entry?

        for (j in dataSet.entryCount - 1 downTo 0) {
            currentEntry = dataSet.getEntryForIndex(j)
            nextEntry = dataSet.getEntryForIndex(j + 1)

            if (nextEntry == null || currentEntry.x < entryFrom.x || currentEntry.x > entryTo.x) {
                continue
            }

            val gradient = when {
                currentEntry.y < 1f && nextEntry.y < 1f -> gradientYellow
                currentEntry.y >= 1f && currentEntry.y < 2f && nextEntry.y < 2f -> gradientRed
                currentEntry.y >= 2f && currentEntry.y < 3f && nextEntry.y < 3f -> gradientBlue
                else -> null
            }

            gradient?.let {
                // Chuyển đổi từ RectF sang Rect
                val rectBounds = Rect(
                    bounds.left.toInt(),
                    bounds.top.toInt(),
                    bounds.right.toInt(),
                    bounds.bottom.toInt()
                )

// Thiết lập giới hạn cho gradient và vẽ nó
                gradient?.bounds = rectBounds
                gradient?.draw(canvas)
            }
        }
    }

    override fun setAlpha(alpha: Int) {}

    override fun setColorFilter(colorFilter: ColorFilter?) {}

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }
}