package com.android.chartdemo

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
import java.util.Random

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    val lineChart: LineChart? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val lineChart = findViewById<LineChart>(R.id.lineChart)

        // Tạo dữ liệu mẫu
        val entries: ArrayList<Entry> = ArrayList()

        // Tạo dữ liệu cho trục tung
        val yAxisValues = floatArrayOf(1f, 2f, 3f, 4f)

        // Tạo dữ liệu ngẫu nhiên cho biểu đồ
        val random = Random()
        for (i in 0..24) {
            val randomYValue = yAxisValues[random.nextInt(yAxisValues.size)]
            entries.add(Entry(i.toFloat(), randomYValue))
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
        xAxis.labelCount = 24 // Số lượng label trên trục X

        // Cài đặt trục Y (tung độ)
        val yAxisLeft: YAxis = lineChart.axisLeft
        yAxisLeft.axisMinimum = 0f
        yAxisLeft.axisMaximum = 4f // Giá trị tối đa của trục Y
        yAxisLeft.setDrawGridLines(true)

        val yAxisRight: YAxis = lineChart.axisRight
        yAxisRight.isEnabled = false

        // Đặt dữ liệu vào biểu đồ
        lineChart.data = lineData
        setupGradient(lineChart)

        // Cập nhật lại biểu đồ
        lineChart.invalidate()
    }


    private fun setupGradient(mChart: LineChart) {
        mChart.viewTreeObserver.addOnGlobalLayoutListener {
            val height = mChart.height.toFloat()
            val width = mChart.width.toFloat()

            // Toast.makeText(this, "setupGradient() called with height=$height width=$width", Toast.LENGTH_SHORT).show()

            val paint = mChart.renderer.paintRender

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
}