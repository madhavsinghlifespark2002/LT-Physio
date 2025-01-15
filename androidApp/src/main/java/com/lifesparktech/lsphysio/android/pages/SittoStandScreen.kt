package com.lifesparktech.lsphysio.android.pages
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.lifesparktech.lsphysio.PeripheralManager
import com.lifesparktech.lsphysio.PeripheralManager.charWrite
import com.lifesparktech.lsphysio.PeripheralManager.mainScope
import com.lifesparktech.lsphysio.PeripheralManager.peripheral
import com.lifesparktech.lsphysio.android.Controller.fetchPatients
import com.lifesparktech.lsphysio.android.Controller.updatePatientWithTestResult
import com.lifesparktech.lsphysio.android.components.Screen
import com.lifesparktech.lsphysio.android.components.disconnectDevice
import com.lifesparktech.lsphysio.android.components.testCommand
import com.lifesparktech.lsphysio.android.data.Patient
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SittoStandScreen(navController: NavController){
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFf4f4f4)),
    ) {
        SittoStandCard(navController)
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SittoStandCard(navController: NavController) {
    val command = "mode 4;"
    val clientData = remember { mutableStateOf("") }
    val patients = remember { mutableStateOf<List<Patient>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedOption by remember { mutableStateOf("") }
    val serverData = remember { mutableStateOf("") }
    val timerMillis = remember { mutableStateOf(0L) }
    val timerRunning = remember { mutableStateOf(false) }
    val timeTaken = remember { mutableStateOf<Long?>(null) }
    var collectionJob by remember { mutableStateOf<Job?>(null) }
    var isTestConnected by remember { mutableStateOf(false) }
    var BandStatus by remember { mutableStateOf("no Status") }
    var BandStatusBool by remember { mutableStateOf(false) }
    var showDisconnectedPopup by remember { mutableStateOf(false)}
    var expanded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var patient by remember { mutableStateOf(Patient()) }
    var context = LocalContext.current

    fun trackDeviceStatus() {
        peripheral?.state?.onEach { state ->
            println("Band State: $state")
            BandStatus = state.toString()
            if(BandStatus == "Disconnected(Timeout)"){
                BandStatusBool = true
                peripheral = null
                charWrite = null
            }
        }?.launchIn(scope)
    }
    fun trackSystemStates(context: Context) {
        val bluetoothReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    BluetoothAdapter.ACTION_STATE_CHANGED -> {
                        val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                        if (state == BluetoothAdapter.STATE_OFF) {
                            mainScope.launch{
                                disconnectDevice(navController)
                            }
                        }
                    }
                }
            }
        }
        val locationReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == LocationManager.PROVIDERS_CHANGED_ACTION) {
                    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                    if (!isGpsEnabled && !isNetworkEnabled) {
                        mainScope.launch{
                            disconnectDevice(navController)
                        }
                    }
                }
            }
        }
        val bluetoothFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        val locationFilter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        context.registerReceiver(bluetoothReceiver, bluetoothFilter)
        context.registerReceiver(locationReceiver, locationFilter)
    }
    LaunchedEffect(timerRunning.value) {
        try{
            val result = fetchPatients()
            patients.value = result
            trackSystemStates(context)
            trackDeviceStatus()

            if (PeripheralManager.peripheral != null) {
                isTestConnected = true
                val startTime = System.currentTimeMillis()
                while (timerRunning.value) {
                    kotlinx.coroutines.delay(10) // Update every 10 ms
                    timerMillis.value = System.currentTimeMillis() - startTime
                }
            }
        }
        finally {
            isLoading = false
        }

    }
    if (BandStatusBool) {
        AlertDialog(
            onDismissRequest = {
                BandStatusBool = false
                navController.navigate("SittoStandScreen") {
                    if ("SittoStandScreen" == Screen.Home.route){
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                    else{
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                    }
                    launchSingleTop = true
                    restoreState = true
                }},
            title = { Text(text = "Band Disconnected") },
            text = { Text(text = "Band is currently disconnected. Please check the connection.") },
            confirmButton = {
                TextButton(onClick = {
                    BandStatusBool = false
                    navController.navigate(Screen.DeviceConnectionScreen.route) {
                        popUpTo("DeviceControlScreen") { inclusive = true }
                    }
                }) {
                    Text(text = "OK")
                }
            }
        )
    }
    if (showDisconnectedPopup) {
        AlertDialog(
            onDismissRequest = { showDisconnectedPopup = false },
            title = { Text(text = "Right Band Disconnected") },
            text = { Text(text = "The right band is currently disconnected. Please check the connection.") },
            confirmButton = {
                TextButton(onClick = { showDisconnectedPopup = false }) {
                    Text(text = "OK")
                }
            }
        )
    }
//    if (isLoading){
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ){
//            CircularProgressIndicator(modifier = Modifier.size(40.dp), color = Color.Black)
//        }
//    }
//    else {
        Card(
            modifier = Modifier.padding(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White // Set the card's background color
            )
        ){
            Column(modifier = Modifier.fillMaxSize().background(Color(0xFFf4f4f4)).padding(12.dp))
            {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.clickable { navController.popBackStack() }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Sit to Stand",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF222429)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                ) {
                    Text(text = "Enter a patient", style = TextStyle(fontSize = 16.sp), fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedOption,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            colors =  TextFieldDefaults.textFieldColors(
                                containerColor = Color(0xffEBEBEB),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(color = Color(0xFFf2f4f5))
                        ) {
                            patients.value.forEach{option ->
                                DropdownMenuItem(
                                    text = { Text(option.name) },
                                    onClick = {
                                        patient = option
                                        selectedOption = option.name
                                        expanded = false
                                    }
                                )

                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (selectedOption.isEmpty()) {
                            Toast.makeText(context, "Please select a patient", Toast.LENGTH_LONG).show()
                            return@Button
                        }
                        if (PeripheralManager.peripheral != null) {
                            if (collectionJob == null) {
                                // Start the process
                                collectionJob = mainScope.launch {
                                    timeTaken.value = null // Reset the timeTaken value
                                    testCommand(command).collect { (client, server) ->
                                        clientData.value = client
                                        serverData.value = server
                                        if (client == "0" && server == "0" && !timerRunning.value) {
                                            timerRunning.value = true
                                            timerMillis.value = 0L
                                        }
                                        if (client == "1" && server == "1" && timerRunning.value) {
                                            timerRunning.value = false
                                            timeTaken.value = timerMillis.value
                                            collectionJob?.cancel()
                                            collectionJob = null
                                            updatePatientWithTestResult(patient, timeTaken.value, "stsTest")
                                        }
                                    }
                                }
                            } else {
                                timerRunning.value = false
                                timeTaken.value = timerMillis.value // Record the time taken
                                collectionJob?.cancel() // Stop collecting
                                collectionJob = null
                                updatePatientWithTestResult(patient, timeTaken.value, "stsTest")
                            }
                        } else {
                            Toast.makeText(context, "Device not Connected", Toast.LENGTH_LONG).show()
                        }

                    },
                    enabled = isTestConnected,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (timerRunning.value) Color(0xFF960019) else Color(0xFF005749)
                    )
                ) {
                    Text(text = if (collectionJob == null) "Start test" else "Stop")
                }
                Text(text = if (timerRunning.value) "Running..." else "Start test")
                val status = when {
                    clientData.value.isEmpty() && serverData.value.isEmpty() -> "Start the test"
                    clientData.value == "0" && serverData.value == "0" -> "Sitting"
                    clientData.value == "1" && serverData.value == "1" -> "Standing"
                    else -> "No value"
                }
                Text(text = if (status.isEmpty()) "" else "Status: $status")
                if (timerRunning.value) {
                    val seconds = (timerMillis.value / 1000)
                    val milliseconds = (timerMillis.value % 1000)
                    Text(text = "Timer: ${seconds}s ${milliseconds}ms")
                }
                timeTaken.value?.let {
                    val seconds = (it / 1000)
                    val milliseconds = (it % 1000)
                    Text(text = "Time taken from Sitting to Standing: ${seconds}s ${milliseconds}ms")
                }
            }
        }
   // }
}

