package com.lifesparktech.lsphysio.android.pages
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lsphysio.android.R
import androidx.compose.material3.Slider

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DeviceControlScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFf4f4f4)).padding(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.clickable{navController.popBackStack() }
            )
            Text(text = "Device Control", fontSize = 22.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF222429))
        }
        Card(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),//.height(700.dp).fillMaxWidth(0.35f),
            //elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White // Set the card's background color
            )
        ){
            Row{
                Image(
                    painter = painterResource(id = R.drawable.batteryillustration),
                    contentDescription = "batteryillustration",
                    modifier = Modifier
                        .size(200.dp)
                )
                Card(
                    modifier = Modifier.padding(12.dp).height(50.dp).width(100.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White // Set the card's background color
                    )
                ){
                    Column{
                        Row(
                            modifier = Modifier.fillMaxWidth().height(100.dp).background(color = Color(0xFFD6E7EE))
                        ){
                            Text("Left")
                        }

                    }
                }
                Card(
                    modifier = Modifier.padding(12.dp).height(50.dp).width(100.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White // Set the card's background color
                    )
                ){
                    Column{
                        Text("Left")
                    }
                }
            }
        }
        Card(
            modifier = Modifier.padding(12.dp).height(350.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White // Set the card's background color
            )
        ){
            Column(modifier = Modifier.fillMaxSize().padding(12.dp)){
                Text("Frequency")
                FrequencySlider()
            }
        }
    }
}
@Composable
fun FrequencySlider() {
    var frequency by remember { mutableStateOf(18f) } // Initial value set to 18
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Frequency: ${frequency.toInt()}",
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Slider
        Slider(
            value = frequency,
            onValueChange = { frequency = it },
            valueRange = 18f..120f, // Slider range
           // steps = 102, // Steps = (max - min) / stepSize - 1
            modifier = Modifier.fillMaxWidth()
        )
    }
}