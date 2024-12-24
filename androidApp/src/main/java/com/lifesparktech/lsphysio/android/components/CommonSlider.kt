package com.lifesparktech.lsphysio.android.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CommonSlider(
    label: String,
    initialValue: Int,
    onValueChanged: (Int) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChangeFinished: (Int) -> Unit,
    isEnabled: Boolean = false
) {
    val sliderValue = remember { mutableStateOf(initialValue) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$label: ${sliderValue.value}", // Display integer value
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Slider(
            value = sliderValue.value.toFloat(),
            onValueChange = {
                val newValue = it
                sliderValue.value = newValue.toInt()
                onValueChanged(newValue.toInt())
            },
            valueRange = valueRange,
            enabled = isEnabled,
            modifier = Modifier.fillMaxWidth(),
            onValueChangeFinished = { onValueChangeFinished(sliderValue.value)},
            colors = SliderDefaults.colors(
                activeTrackColor = Color(0xFF005749),
                thumbColor = Color(0xFF005749),
                inactiveTrackColor = Color(0xFFCBE2D2)
            ),
        )
    }
}