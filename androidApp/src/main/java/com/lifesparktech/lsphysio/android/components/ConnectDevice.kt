package com.lifesparktech.lsphysio.android.components

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import com.benasher44.uuid.uuidFrom
import com.juul.kable.AndroidPeripheral
import com.juul.kable.ConnectionLostException
import com.juul.kable.Filter
import com.juul.kable.Scanner
import com.juul.kable.peripheral
import com.lifesparktech.lsphysio.PeripheralManager
import com.lifesparktech.lsphysio.PeripheralManager.mainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
fun ConnectDeviced(
    navController: NavController,
    context: Context,
    deviceName: String
) {
    val scanner = Scanner {
        filters = listOf(Filter.Name(deviceName))
    }
    mainScope.launch {
        try {
            val advertisement = scanner.advertisements.onEach { println(it) }.first()
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
            // Show Toast for unsuccessful connection
            Toast.makeText(context, "Failed to connect: ${e.message}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            println("Error: ${e.message}")
            Toast.makeText(context, "Failed to connect: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            println("Cleaning up resources.")
        }
    }
}
