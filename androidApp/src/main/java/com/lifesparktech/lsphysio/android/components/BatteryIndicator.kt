package com.lifesparktech.lsphysio.android.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun BatteryIndicator(
    batteryPercentage: Int,
    modifier: Modifier = Modifier,
    indicatorColor: Color = Color(0xFF0C6658),
    backgroundColor: Color = Color(0xFFB0BEC5),
    size: Dp = 70.dp,
    strokeWidth: Dp = 12.dp,
    isEnabled: Boolean = true
) {
    val adjustedIndicatorColor = if (isEnabled) indicatorColor else Color.Gray
    val adjustedBackgroundColor = if (isEnabled) backgroundColor else Color.LightGray
    Box(contentAlignment = Alignment.Center, modifier = modifier.size(size)) {
        // Circular Indicator
        Canvas(modifier = Modifier.size(size)) {
            val sweepAngle = 360 * (batteryPercentage / 100f)
            val stroke = strokeWidth.toPx()
            drawArc(
                color = adjustedBackgroundColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = stroke, cap = StrokeCap.Round),
                size = Size(size.toPx(), size.toPx()),
                topLeft = Offset(0f, 0f)
            )
            drawArc(
                color = adjustedIndicatorColor,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = stroke, cap = StrokeCap.Round),
                size = Size(size.toPx(), size.toPx()),
                topLeft = Offset(0f, 0f)
            )
        }
        Text(
            text = "$batteryPercentage%",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.Black
        )
    }
}