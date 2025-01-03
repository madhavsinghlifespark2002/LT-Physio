package com.lifesparktech.lsphysio.android.pages
import android.content.Context
import android.os.Build
import android.os.CountDownTimer
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.juul.kable.Advertisement
import com.juul.kable.Filter
import com.juul.kable.Scanner
import com.lifesparktech.lsphysio.PeripheralManager.mainScope
import com.lifesparktech.lsphysio.android.components.ConnectDeviced
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.accompanist.flowlayout.FlowRow
import com.lifesparktech.lsphysio.android.MainActivity
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DeviceConnectionScreen(navController: NavController) {
    mainScope = CoroutineScope(Dispatchers.Main)

    // State variables to track scanning, devices, and timer
    var isScanning by remember { mutableStateOf(false) }
    var devices by remember { mutableStateOf<List<Advertisement>>(emptyList()) }
    var timerValue by remember { mutableStateOf(10) }
    var isConnecting by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    var context = LocalContext.current
    val mainActivity = LocalContext.current as MainActivity
    var connectingDevice by remember { mutableStateOf<String?>(null) }

    // Layout structure
    Column(
        modifier = Modifier
            .background(color = Color(0xFFf4f4f4))
            .fillMaxSize()
            .padding(12.dp)
    ) {
        // Header Section
        Column {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Device Connection", fontWeight = FontWeight.Bold, fontSize = 24.sp)

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(color = Color(0xFFD6E7EE))
                        .padding(12.dp)
                        .clickable {}
                ) {
                    Row(
                        modifier = Modifier
                            .width(120.dp)
                            .clickable {
                                mainActivity.requestBluetoothPermissions()
                                mainActivity.requestLocationPermissions()

                                // Start scanning when button is clicked
                                if (!isScanning) {
                                    isScanning = true
                                    timerValue = 10 // Reset timer
                                    val timer = object : CountDownTimer(10000, 1000) { // 10 seconds scan
                                        override fun onTick(millisUntilFinished: Long) {
                                            timerValue = (millisUntilFinished / 1000).toInt()
                                        }

                                        override fun onFinish() {
                                            isScanning = false
                                        }
                                    }
                                    timer.start()

                                    // Perform Bluetooth scan
                                    scanBluetoothDevices(context, mainScope) { foundDevices ->
                                        devices = foundDevices
                                        isScanning = false // Stop scanning once devices are found
                                    }
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.device_connection),
                            contentDescription = "logo",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            if (isScanning) "Scanning..." else "Start Scan",
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Available device",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color(0xFF474747),
                modifier = Modifier.padding(start = 12.dp)
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 8.dp),
                color = Color(0xFFD6D6D6),
                thickness = 1.dp
            )

            // Box for displaying devices and scanning animation
            Box(
                modifier = Modifier.height(if (screenWidth <= 800.0.dp) { 800.dp } else { 400.dp })
            ) {
                // Show the animated antenna while scanning is true
                if (isScanning) {
                    AnimatedAntenna() // Call your AnimatedAntenna composable here
                }

                // Display found devices
                if (devices.isNotEmpty()) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        mainAxisSpacing = 2.dp,
                        crossAxisSpacing = 2.dp,
                    ) {
                        devices.forEach { device ->
                            Card(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .width(250.dp)
                                    .height(100.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("${device.name}")
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Button(
                                        onClick = {
                                            mainActivity.requestBluetoothPermissions()
                                            mainActivity.requestLocationPermissions()

                                            isConnecting = true



                                            // Handle connecting to the selected device
                                            mainScope.launch {
                                                connectingDevice = device.name // Set the connecting device

                                                try {
                                                    delay(2000)
                                                    ConnectDeviced(context, navController, device)
                                                } catch (e: Exception) {
                                                    println("Error connecting: ${e.message}")
                                                    Toast.makeText(
                                                        context,
                                                        "Failed to connect: ${e.message}",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } finally {
                                                    connectingDevice = null
                                                    isConnecting = false
                                                }
                                            }
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.textButtonColors(containerColor = Color(0xFF005749)),
                                    ) {
                                        Text(
                                            text = if (connectingDevice == device.name) "Connecting..." else "Connect",
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else if (!isScanning) {
                    Text("No devices found. Click 'Start Scan' to search for devices.", modifier = Modifier.padding(start = 12.dp))
                }
                if (isConnecting) {
                    BluetoothConnectionScreen()
                }
            }
        }
    }
}

// Function to start scanning Bluetooth devices
@RequiresApi(Build.VERSION_CODES.O)
fun scanBluetoothDevices(context: Context, scope: CoroutineScope, onDevicesFound: (List<Advertisement>) -> Unit) {
    val scanner = Scanner {
        filters = listOf(Filter.Service(uuidFrom("0000acf0-0000-1000-8000-00805f9b34fb"))) // Example UUID
    }
    val devices = mutableListOf<Advertisement>()
    val uniqueAddresses = mutableSetOf<String>() // Track unique device addresses

    scope.launch {
        try {
            withTimeout(10_000) { // Scan for 10 seconds
                scanner.advertisements.collect { advertisement ->
                    if (uniqueAddresses.add(advertisement.address)) {
                        println("Found device: ${advertisement.name ?: "Unknown"} - ${advertisement.address}")
                        devices.add(advertisement)
                    }
                }
            }
        } catch (e: TimeoutCancellationException) {
            println("Scanning finished after 10 seconds.")
        } catch (e: Exception) {
            println("Error during scanning: ${e.message}")
            Toast.makeText(context, "Error during scanning: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            onDevicesFound(devices)
        }
    }
}