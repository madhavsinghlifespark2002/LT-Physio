package com.lifesparktech.lsphysio.android.pages
import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.benasher44.uuid.uuidFrom
import com.example.lsphysio.android.R
import com.google.accompanist.flowlayout.FlowRow
import com.juul.kable.Advertisement
import com.juul.kable.Filter
import com.juul.kable.Scanner
import com.lifesparktech.lsphysio.PeripheralManager.mainScope
import com.lifesparktech.lsphysio.android.MainActivity
import com.lifesparktech.lsphysio.android.components.ConnectDeviced
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons

import androidx.compose.runtime.*


import androidx.compose.material3.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke


@Composable
fun AnimatedAntenna() {
    val animationDuration = 400 // Duration of each wave animation (for appearing)
    val delayBetweenWaves = 100L // Delay between waves
    val disappearanceDuration = 10 // Duration for disappearing waves (1 second)

    // State holders for wave opacity animations
    val waveAlphas = listOf(
        remember { Animatable(0f) },
        remember { Animatable(0f) },
        remember { Animatable(0f) }
    )

    // Launch blink animations continuously in an infinite loop
    LaunchedEffect(Unit) {
        while (true) {
            // Animate each wave sequentially (showing them one by one)
            waveAlphas.forEach { waveAlpha ->
                waveAlpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = animationDuration, easing = LinearEasing)
                )
                delay(delayBetweenWaves)
            }

            // Make all waves disappear at once (after all waves have shown), using a shorter duration
            waveAlphas.forEach { waveAlpha ->
                waveAlpha.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = disappearanceDuration, easing = LinearEasing)
                )
            }

            // Wait a moment before starting the animation again
            delay(10L) // Optional: Delay before restarting the animation
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title above the animation
            Text(
                text = "Scanning for devices....",
                fontSize = 40.sp, // Larger font size
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(30.dp)) // Space between title and animation

            // Canvas for animated antenna
            Canvas(modifier = Modifier.size(400.dp)) { // Larger canvas size for better visibility
                val center = Offset(size.width / 2, size.height / 2)
                val antennaRadius = 24.dp.toPx() // Increase the antenna size
                val lineWidth = 12.dp.toPx() // Increase line width for visibility
                val waveWidth = 12.dp.toPx() // Increase wave width
                val gapBetweenWaves = 60.dp.toPx() // Increase gap between waves

                // Draw antenna base (gray line)
                drawLine(
                    color = Color.Gray,
                    start = Offset(x = center.x, y = center.y + antennaRadius),
                    end = Offset(x = center.x, y = size.height),
                    strokeWidth = lineWidth
                )

                // Draw antenna circle outline (gray circle with border)
                drawCircle(
                    color = Color.Gray,
                    radius = antennaRadius,
                    center = center,
                    style = Stroke(width = lineWidth)
                )

                // Draw green semi-circles that blink at their positions
                waveAlphas.forEachIndexed { index, waveAlpha ->
                    val baseRadius = antennaRadius + gapBetweenWaves * (index + 1) // Fixed position for each wave
                    drawArc(
                        color = Color.Blue.copy(alpha = waveAlpha.value * (0.6f - (index * 0.2f))), // Green color with fading effect
                        startAngle = 180f, // Semi-circle starts from the left
                        sweepAngle = 180f, // Covers 180 degrees for a semi-circle
                        useCenter = false,
                        style = Stroke(width = waveWidth),
                        topLeft = Offset(center.x - baseRadius, center.y - baseRadius),
                        size = Size(baseRadius * 2, baseRadius * 2)
                    )
                }
            }
        }
    }
}

@Composable
fun BluetoothConnectionScreen() {
    var progress by remember { mutableStateOf(0f) }
    var isConnected by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // Simulate connection progress
        while (progress.compareTo(1f) < 0) {
            delay(50)
            progress += 0.02f
        }
        isConnected = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        // Circular animation
        Canvas(modifier = Modifier.size(200.dp)) {
            drawCircle(
                color = LightGray,
                style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = Color(0xFF105749),
                startAngle = -90f,
                sweepAngle = 360 * progress,
                useCenter = false,
                style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.bluetooth_icon),
                contentDescription = "bluetooth",
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .padding(end = 8.dp) // Padding between Image and Text
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (isConnected) "Connected" else "Connecting...",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }
}