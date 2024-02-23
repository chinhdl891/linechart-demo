package com.android.chartdemo

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Random

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    val lineChart: LineChart? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val lineChart = findViewById<LineChart>(R.id.lineChart)
        lineChart.legend.isEnabled = false // Ẩn ghi chú (legend) của DataSet
        lineChart.description.isEnabled = false // Ẩn mô tả (description) của biểu đồ
        // Tạo dữ liệu mẫu
        val entries: ArrayList<Entry> = ArrayList()

        // Tạo dữ liệu cho trục tung
        val yAxisValues = listOf<String>("A", "B", "C", "D")



        val startTimeMillis = System.currentTimeMillis() // Thời điểm bắt đầu ở hiện tại
        val endTimeMillis = startTimeMillis + 86400000 // Thời điểm kết thúc là 24 giờ sau
        val numberOfPoints = 1000 // Số lượng điểm trên trục hoành

        val timeList = createTimeList(startTimeMillis, endTimeMillis, numberOfPoints)

        // In danh sách thời gian
        timeList.forEach {
            val randomValue = Random().nextInt(5)
            entries.add(Entry(it.toFloat(), randomValue.toFloat()))
        }

        // Tạo dataset cho biểu đồ
        val dataSet = LineDataSet(entries, "DataSet").apply {
            setDrawCircles(false) // Ẩn điểm chấm
//            setDrawFilled(true)
            setDrawValues(false) // Ẩn giá trị tại các đỉnh
            lineWidth = 2f
            mode = LineDataSet.Mode.HORIZONTAL_BEZIER // Sử dụng cubic Bézier
            cubicIntensity = 0.2f // Điều chỉnh độ cong của đường line
            circleRadius = 4f // Độ lớn của điểm chấm
            circleHoleRadius = 2f // Độ lớn của lỗ ở giữa điểm chấm
        }

// Tạo dữ liệu của biểu đồ
        val lineData = LineData(dataSet)

// Đặt dữ liệu vào biểu đồ
        lineChart.data = lineData

        // Cài đặt trục X (hoành độ)
        val xAxis: XAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        val minValue = xAxis.axisMinimum.toLong()
        val maxValue = xAxis.axisMaximum.toLong()

        xAxis.setLabelCount(2, true)


        xAxis.valueFormatter = object : ValueFormatter() {
            private val formatWithAMPM = SimpleDateFormat("hh:mm aa", Locale.getDefault())
            private val formatWithoutAMPM = SimpleDateFormat("hh:mm", Locale.getDefault())

            override fun getFormattedValue(value: Float): String {
                val position = value.toLong()
                return when (position) {
                    minValue, maxValue -> formatWithAMPM.format(Date(position))
                    else -> formatWithoutAMPM.format(Date(position))
                }
            }
        }


        // Cấu hình trục Y (tung độ)
        val yAxisLeft: YAxis = lineChart.axisLeft
        yAxisLeft.axisMinimum = -1f
        yAxisLeft.axisMaximum = 5f // Giá trị tối đa của trục Y
        yAxisLeft.setDrawGridLines(false)
        yAxisLeft.setDrawAxisLine(false)
        val yAxisRight: YAxis = lineChart.axisRight
        yAxisRight.isEnabled = false


// Cài đặt giá trị trục tung
        val yAxisValueFormatter = IndexAxisValueFormatter(yAxisValues)
        val yAxis: YAxis = lineChart.axisLeft
        yAxis.valueFormatter = yAxisValueFormatter


        // Tạo mảng màu cho từng vị trí trên trục tung
        val labelColors = arrayOf(
            resources.getColor(android.R.color.holo_red_light),
            resources.getColor(android.R.color.holo_orange_light),
            resources.getColor(android.R.color.holo_green_light),
            resources.getColor(android.R.color.holo_blue_bright)
        )

// Thiết lập màu sắc mặc định cho trục tung
        yAxis.textColor = Color.BLACK

// Thiết lập YAxisValueFormatter
        yAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt() - 1
                return when {
                    index >= 0 && index < labelColors.size -> {
                        yAxis.textColor = labelColors[index] // Thiết lập màu cho nhãn tại vị trí này
                        "$value" // Trả về nhãn hoặc giá trị của vị trí tùy thuộc vào nhu cầu của bạn
                    }
                    else -> ""
                }
            }
        }






        // Đặt dữ liệu vào biểu đồ
        lineChart.data = lineData
        setupGradient(lineChart)



        // Cập nhật lại biểu đồ
        lineChart.invalidate()


    }


    private fun setupGradient(lineChart: LineChart) {
        lineChart.viewTreeObserver.addOnGlobalLayoutListener {
            val height = lineChart.height.toFloat()
            val width = lineChart.width.toFloat()

            // Toast.makeText(this, "setupGradient() called with height=$height width=$width", Toast.LENGTH_SHORT).show()

            val paint = lineChart.renderer.paintRender

            val colors = intArrayOf(
                getColor(android.R.color.holo_blue_bright), // Màu xanh nước biển
                getColor(android.R.color.holo_green_light), // Màu xanh lá cây
                getColor(android.R.color.holo_orange_light), // Màu cam
                getColor(android.R.color.holo_red_light) // Màu đỏ
            )

            val positions = floatArrayOf(0f, 0.33f, 0.66f, 1f) // Tương ứng với các khoảng từ 0-1

            // Tạo LinearGradient theo chiều dọc
            val linGrad = LinearGradient(
                0f, 0f, 0f, height, // Chiều dài của gradient từ trên xuống dưới
                colors, positions, Shader.TileMode.REPEAT
            )
            paint.shader = linGrad

        }
    }

    fun createTimeList(
        startTimeMillis: Long, endTimeMillis: Long, numberOfPoints: Int
    ): List<Long> {
        val timeList = mutableListOf<Long>()
        val duration = endTimeMillis - startTimeMillis
        val interval = duration / numberOfPoints
        var currentTime = startTimeMillis
        for (i in 0 until numberOfPoints) {
            timeList.add(currentTime)
            currentTime += interval
        }
        return timeList
    }


}