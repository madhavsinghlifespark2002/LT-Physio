package com.lifesparktech.lsphysio.android.pages
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Build
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import com.lifesparktech.lsphysio.PeripheralManager.charWrite
import com.lifesparktech.lsphysio.PeripheralManager.mainScope
import com.lifesparktech.lsphysio.PeripheralManager.peripheral
import com.lifesparktech.lsphysio.android.components.BatteryIndicator
import com.lifesparktech.lsphysio.android.components.CommonSlider
import com.lifesparktech.lsphysio.android.components.Screen
import com.lifesparktech.lsphysio.android.components.disconnectDevice
import com.lifesparktech.lsphysio.android.components.getBatteryPercentage
import com.lifesparktech.lsphysio.android.components.getFrequency
import com.lifesparktech.lsphysio.android.components.getMagnitudePercentage
import com.lifesparktech.lsphysio.android.components.isClientConnected
import com.lifesparktech.lsphysio.android.components.modesDictionary
import com.lifesparktech.lsphysio.android.components.writeCommand
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.math.round
import kotlin.text.get
import kotlin.toString

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DeviceControlScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Select Mode") }
    var mode by remember { mutableStateOf("") }
    val options = listOf("High Freq", "Swing phase continuous", "Swing phase burst", "Stance phase continuous", "Stance phase burst","Open loop")
    var leftBattery by remember { mutableStateOf(0) }
    var rightBattery by remember { mutableStateOf(0) }
    var leftMagnitude by remember { mutableStateOf<Int?>(null) }
    var rightMagnitude by remember { mutableStateOf<Int?>(null) }
    var frequencyBand by remember { mutableStateOf<Int?>(null) }
    var isRightConnect by remember { mutableStateOf<Boolean?>(null) }
    var showDisconnectedPopup by remember { mutableStateOf(false) }
    var BandStatus by remember { mutableStateOf("no Status") }
    var BandStatusBool by remember { mutableStateOf(false) }
    val context = LocalContext.current

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

        // Register the receivers
        val bluetoothFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        val locationFilter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        context.registerReceiver(bluetoothReceiver, bluetoothFilter)
        context.registerReceiver(locationReceiver, locationFilter)
    }

    LaunchedEffect(Unit) {
        mainScope.launch {
            trackSystemStates(context)
            trackDeviceStatus()
            val batteryValues = getBatteryPercentage()?.let {
                if (it.second == "-1.000000") Pair(it.first, "1.000000") else it
            }
            val magnitudeValues = getMagnitudePercentage()?.let {
                if (it.second == -1) Pair(it.first, 1) else it
            }
            val fetchedFrequency = getFrequency().let {
                if (it == -60) 18 else it
            }
            isRightConnect = isClientConnected()
            showDisconnectedPopup = isRightConnect == true
            frequencyBand = fetchedFrequency
            if (batteryValues != null) {
                leftBattery = batteryValues.first.toFloat().toInt()
                rightBattery = batteryValues.second.toFloat().toInt()
            }
            if (magnitudeValues != null) {
                leftMagnitude = magnitudeValues.first ?: 0
                rightMagnitude = magnitudeValues.second ?: 0
            }
        }

    }
    if (BandStatusBool) {
        AlertDialog(
            onDismissRequest = {
                BandStatusBool = false
                navController.navigate(Screen.DeviceConnectionScreen.route) {
                    popUpTo("DeviceControlScreen") { inclusive = true }
                } },
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
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFf4f4f4)).padding(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ){
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.clickable{navController.popBackStack() }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "Device Control", fontSize = 22.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF222429))
        }
        Card(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),//.height(700.dp).fillMaxWidth(0.35f),
            //elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White // Set the card's background color
            )
        ){
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ){
                Image(
                    painter = painterResource(id = R.drawable.batteryillustration),
                    contentDescription = "batteryillustration",
                    modifier = Modifier
                        .size(200.dp)
                )
                Card(
                    modifier = Modifier.size(150.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White // Set the card's background color
                    ),

                ){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                       // verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Row(
                            modifier = Modifier.fillMaxWidth().height(40.dp).background(color = Color(0xFFD6E7EE)),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text("Left", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)

                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        BatteryIndicator(batteryPercentage = leftBattery)

                    }
                }
                Card(
                    modifier = Modifier.size(150.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White // Set the card's background color
                    ),

                    ){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        // verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Row(
                            modifier = Modifier.fillMaxWidth().height(40.dp).background(color = Color(0xFFD6E7EE)),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text("Right", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)

                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        if(isRightConnect != null){
                            BatteryIndicator(batteryPercentage = rightBattery, isEnabled = isRightConnect == false)
                        }
                        else{
                            Text("Loading...", fontSize = 14.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
        Box(modifier = Modifier.padding(12.dp)) {
            Column {
                Text(text = "Mode", style = TextStyle(fontSize = 16.sp), fontWeight = FontWeight.SemiBold)
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
                            containerColor = Color.White,
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
                        options.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    mode = option
                                    selectedOption = option
                                    expanded = false
                                    println("this is mode: $option")
                                    val modeValue = modesDictionary[option]
                                    val Command = "mode $modeValue;"
                                    println("this is command. : $Command")
                                    mainScope.launch{
                                        writeCommand(Command)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
        Card(
            modifier = Modifier.padding(12.dp).height(160.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White // Set the card's background color
            )
        ){
            Column(modifier = Modifier.fillMaxSize().padding(12.dp)){
                Text("Frequency", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                if (frequencyBand != null && isRightConnect != null) {
                    CommonSlider(
                        label = "Steps/min",
                        initialValue = frequencyBand!!,
                        onValueChanged = { /* Handle value change */ },
                        valueRange = 18f..120f,
                        onValueChangeFinished = { newValue ->
                            val approxFrequency = (newValue.toDouble() / 60).let {
                                (round(it * 10) / 10)
                            }
                            val command = "freq c $approxFrequency;"
                            mainScope.launch {
                                writeCommand(command)
                            }
                        },
                        isEnabled = isRightConnect == false
                    )
                } else {
                    Text("Loading...", fontSize = 14.sp, color = Color.Gray)
                }
            }
        }
        Card(
            modifier = Modifier.padding(12.dp).height(160.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White // Set the card's background color
            )
        ){
            Column(modifier = Modifier.fillMaxSize().padding(12.dp)){
                Text("Magnitude", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Row(modifier = Modifier.fillMaxWidth()){
                    Box(
                        modifier = Modifier.weight(1f)
                    ){
                        if (leftMagnitude != null) {
                            CommonSlider(
                                label = "Left",
                                initialValue = leftMagnitude!!,
                                onValueChanged = {   /* Handle value change */ },
                                valueRange = 0f..4f,
                                onValueChangeFinished = { newValue ->
                                    val command = "mag s $newValue;"
                                    mainScope.launch {
                                        writeCommand(command)
                                    }
                                }
                            )
                        }
                        else{
                            Text("Loading...", fontSize = 14.sp, color = Color.Gray)
                        }
                    }
                    Box(
                        modifier = Modifier.weight(1f)
                    ){
                        if (rightMagnitude !=null  && isRightConnect != null){
                            CommonSlider(
                                label = "Right",
                                initialValue = rightMagnitude!!,
                                onValueChanged = {   /* Handle value change */ },
                                valueRange = 0f..4f,
                                onValueChangeFinished = { newValue ->
                                    val command = "mag c $newValue;"
                                    mainScope.launch{
                                        writeCommand(command)
                                    }
                                },
                                isEnabled = isRightConnect == false
                            )
                        }
                        else{
                            Text("Loading...", fontSize = 14.sp, color = Color.Gray)
                        }

                    }
                }

            }
        }
        Card(
            modifier = Modifier.padding(12.dp).fillMaxWidth().height(50.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF960019)
            )
        ){
            Row(
                modifier = Modifier.fillMaxSize().clickable{
                    mainScope.launch{
                        disconnectDevice(navController)
                    }},
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center

            ){
                Image(
                    painter = painterResource(id = R.drawable.bluetooth_disabled),
                    contentDescription = "bluetooth disabled",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text("Disconnect", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}
