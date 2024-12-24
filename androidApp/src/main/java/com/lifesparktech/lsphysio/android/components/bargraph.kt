package com.lifesparktech.lsphysio.android.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.platform.LocalContext
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

@Composable
fun BarChartCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .width(800.dp)
            .height(600.dp)
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Title for the bar chart
            Text(
                text = "Bar Graph",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp), // Space between title and chart
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            // Bar chart composable placed in the card
            BarChartComposable(modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
fun BarChartComposable(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val entries = listOf(
        BarEntry(0f, 10f),
        BarEntry(1f, 20f),
        BarEntry(2f, 30f),
        BarEntry(3f, 40f),
        BarEntry(4f, 50f)
    )

    // Define colors for the bars
    val colors = listOf(
        Color(0xFF83A2CE).toArgb(),
        Color(0xFF6582AA).toArgb(),
        Color(0xFF4b6387).toArgb(),
        Color(0xFF334664).toArgb(),
        Color(0xFF0B111D).toArgb()
    )

    val dataSet = BarDataSet(entries, "Example Data").apply {
        this.colors = colors
        valueTextSize = 32f
    }

    val barData = BarData(dataSet)

    // Create the BarChart view
    val barChart = BarChart(context).apply {
        data = barData
        setFitBars(true)
        description.isEnabled = false

        xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            textSize = 18f
            setDrawGridLines(false)
        }

        axisLeft.apply {
            textSize = 18f
            setDrawGridLines(true)
        }

        axisRight.isEnabled = false
    }

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { barChart }
    )
}