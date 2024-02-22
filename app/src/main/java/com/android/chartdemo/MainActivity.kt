package com.android.chartdemo

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
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
            setDrawFilled(true)
            setDrawValues(false) // Ẩn giá trị tại các đỉnh
            lineWidth = 2f
        }

// Tạo gradient cho từng khoảng giá trị
        val gradientRed = ContextCompat.getDrawable(this, R.drawable.gradient_red)
        val gradientBlue = ContextCompat.getDrawable(this, R.drawable.gradient_blue)
        val gradientYellow = ContextCompat.getDrawable(this, R.drawable.gradient_yellow)

// Thiết lập gradient cho từng phần của dải giá trị trên đường
        val fillGradient =
            FillDrawableHelper(lineChart, dataSet, gradientRed, gradientBlue, gradientYellow)
        dataSet.fillDrawable = fillGradient

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
        yAxisLeft.axisMaximum = 5f // Giá trị tối đa của trục Y
        yAxisLeft.setDrawGridLines(true)

        val yAxisRight: YAxis = lineChart.axisRight
        yAxisRight.isEnabled = false

        // Đặt dữ liệu vào biểu đồ
        lineChart.data = lineData

        // Cập nhật lại biểu đồ
        lineChart.invalidate()
    }
}