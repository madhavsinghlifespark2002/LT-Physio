package com.lifesparktech.lsphysio.android.pages
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lifesparktech.lsphysio.PeripheralManager.mainScope
import com.lifesparktech.lsphysio.android.components.testCommand
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
@Composable
fun SittoStandScreen() {
    val command = "mode 4;"
    val clientData = remember { mutableStateOf("") }
    val serverData = remember { mutableStateOf("") }
    val timerMillis = remember { mutableStateOf(0L) }
    val timerRunning = remember { mutableStateOf(false) }
    val timeTaken = remember { mutableStateOf<Long?>(null) }
    var collectionJob by remember { mutableStateOf<Job?>(null) }

    // Timer effect
    LaunchedEffect(timerRunning.value) {
        val startTime = System.currentTimeMillis()
        while (timerRunning.value) {
            kotlinx.coroutines.delay(10) // Update every 10 ms
            timerMillis.value = System.currentTimeMillis() - startTime
        }
    }

    Column(
        modifier = Modifier.padding(12.dp)
    ) {
        Text(text = "Sit to Stand")

        // Start button
        Button(
            onClick = {
                if (collectionJob == null) { // Start the process only if not already running
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
                                timeTaken.value = timerMillis.value // Record the time taken
                                collectionJob?.cancel() // Stop collecting
                                collectionJob = null // Reset the job
                            }
                        }
                    }
                }
            },
           // enabled = !timerRunning.value,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (timerRunning.value) Color(0xFF960019) else Color(0xFF005749)
            )
        ) {
            Text(text = if (timerRunning.value) "Stop" else "Start test")
        }
        Text(text = if (timerRunning.value) "Running..." else "Start test")
        // Determine the status
        val status = when {
            clientData.value.isEmpty() && serverData.value.isEmpty() -> "Start the test"
            clientData.value == "0" && serverData.value == "0" -> "Sitting"
            clientData.value == "1" && serverData.value == "1" -> "Standing"
            else -> "No value"
        }

        Text(text = if (status.isEmpty()) "" else "Status: $status")

        // Display the timer value while running
        if (timerRunning.value) {
            val seconds = (timerMillis.value / 1000)
            val milliseconds = (timerMillis.value % 1000)
            Text(text = "Timer: ${seconds}s ${milliseconds}ms")
        }

        // Display the time taken when the process stops
        timeTaken.value?.let {
            val seconds = (it / 1000)
            val milliseconds = (it % 1000)
            Text(text = "Time taken from Sitting to Standing: ${seconds}s ${milliseconds}ms")
        }
    }
}
