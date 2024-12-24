package com.lifesparktech.lsphysio.android.components

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import com.benasher44.uuid.uuidFrom
import com.juul.kable.Advertisement
import com.juul.kable.AndroidPeripheral
import com.juul.kable.ConnectionLostException
import com.juul.kable.peripheral
import com.lifesparktech.lsphysio.PeripheralManager
import com.lifesparktech.lsphysio.PeripheralManager.mainScope
import com.lifesparktech.lsphysio.PeripheralManager.peripheral
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
fun ConnectDeviced(
    context: Context,
    navController: NavController,
    deviceName: Advertisement
) {
    mainScope.launch {
        try {
            val advertisement = deviceName
            val peripheral = mainScope.peripheral(advertisement)
            peripheral.connect()
            val androidPeripheral = peripheral as AndroidPeripheral
            val service = peripheral.services?.find {
                it.serviceUuid == uuidFrom("0000abf0-0000-1000-8000-00805f9b34fb")
            } ?: throw Exception("Service not found for device")

            val char = service.characteristics.find {
                it.characteristicUuid == uuidFrom("0000abf1-0000-1000-8000-00805f9b34fb")
            } ?: throw Exception("Read characteristic not found")

            val charWrite = service.characteristics.find {
                it.characteristicUuid == uuidFrom("0000abf1-0000-1000-8000-00805f9b34fb")
            } ?: throw Exception("Write characteristic not found")
            androidPeripheral.requestMtu(512)
            PeripheralManager.peripheral = peripheral
            PeripheralManager.charWrite = charWrite
            navController.navigate("DeviceControlScreen")
        } catch (e: ConnectionLostException) {
            println("Connection lost: ${e.message}")
            Toast.makeText(context, "Failed to connect", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            println("Error: ${e.message}")
            Toast.makeText(context, "Failed to connect", Toast.LENGTH_LONG).show()
        } finally {
            println("Cleaning up resources.")
        }
    }
}


suspend fun writeCommand(command: String) {
    val peripheral = PeripheralManager.peripheral
    val charWrite = PeripheralManager.charWrite

    if (peripheral != null && charWrite != null) {
        try {
            print("this is command: $command")
            peripheral.write(charWrite, command.encodeToByteArray())
            println("Command sent: $command")
        } catch (e: Exception) {
            println("Error writing command: ${e.message}")
        }
    } else {
        println("Peripheral or characteristic not initialized.")
    }
}

val BATTERY_PERCENTAGE_CLIENT_UUID = uuidFrom("0000aef3-0000-1000-8000-00805f9b34fb")
val BATTERY_PERCENTAGE_SERVER_UUID = uuidFrom("0000adf3-0000-1000-8000-00805f9b34fb")

suspend fun getBatteryPercentage(): Pair<String, String>? {
    return try {
        val clientCharacteristic =  peripheral?.services!!
            .flatMap { it.characteristics }
            .firstOrNull { it.characteristicUuid == BATTERY_PERCENTAGE_CLIENT_UUID }
            ?: error("Client battery characteristic not found")
        val serverCharacteristic =  peripheral?.services!!
            .flatMap { it.characteristics }
            .firstOrNull { it.characteristicUuid == BATTERY_PERCENTAGE_SERVER_UUID }
            ?: error("Server battery characteristic not found")
        val clientResponse = peripheral?.read(clientCharacteristic)
        val serverResponse = peripheral?.read(serverCharacteristic)
        val rightbattery = clientResponse!!.decodeToString()
        val leftbattery = serverResponse!!.decodeToString()
        println("right Battery Response: ${rightbattery}")
        println("left Battery Response: ${leftbattery}")

        // Return the pair of battery values as strings
        Pair(leftbattery, rightbattery)
    } catch (e: Exception) {
        println("Error while getting battery values: ${e.message}")
        null // Return null to indicate failure
    }
}

suspend fun getFrequency(): String? {
    return try {
        val clientCharacteristic =  peripheral?.services!!
            .flatMap { it.characteristics }
            .firstOrNull { it.characteristicUuid == BATTERY_PERCENTAGE_CLIENT_UUID }
            ?: error("Client Frequency characteristic not found")
        val serverCharacteristic =  peripheral?.services!!
            .flatMap { it.characteristics }
            .firstOrNull { it.characteristicUuid == BATTERY_PERCENTAGE_SERVER_UUID }
            ?: error("Server Frequency characteristic not found")

        // Read the values from both characteristics
        val clientResponse = peripheral?.read(clientCharacteristic)
        val serverResponse = peripheral?.read(serverCharacteristic)
        val rightbattery = clientResponse!!.decodeToString()
        val leftbattery = serverResponse!!.decodeToString()

        // Log the responses (optional)
        println("right Battery Response: ${rightbattery}")
        println("left Battery Response: ${leftbattery}")

        // Return the pair of battery values as strings
       leftbattery
    } catch (e: Exception) {
        println("Error while getting battery values: ${e.message}")
        null // Return null to indicate failure
    }
}
val modesDictionary = mapOf(
    "High Freq" to "-1",
    "Swing phase continuous" to "2",
    "Swing phase burst" to "3",
    "Stance phase continuous" to "0",
    "Stance phase burst" to "1",
    "Open loop" to "4"
)
suspend fun disconnectDevice(navController: NavController) {
    val peripheral = PeripheralManager.peripheral

    if (peripheral != null) {
        try {
            println("Attempting to disconnect from device...")
            peripheral.disconnect()
            navController.popBackStack()
            println("Device disconnected successfully.")
        } catch (e: Exception) {
            println("Error disconnecting device: ${e.message}")
        } finally {
            // Clean up resources after disconnecting
            PeripheralManager.peripheral = null
            PeripheralManager.charWrite = null
            println("Resources cleaned up after disconnection.")
        }
    } else {
        println("No connected peripheral to disconnect.")
    }
}
// 0000aef9-0000-1000-8000-00805f9b34fb
// 0000adf9-0000-1000-8000-00805f9b34fb