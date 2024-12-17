package com.lifesparktech.lsphysio.android.pages
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
@Composable
fun DeviceControlScreen() {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFf4f4f4)),
    ) {
        Text(text = "Device Control Screen")
    }
}
