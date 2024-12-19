package com.lifesparktech.lsphysio.android.pages
import android.content.Context
import android.os.Build
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
import com.juul.kable.AndroidPeripheral
import com.juul.kable.ConnectionLostException
import com.juul.kable.Filter
import com.juul.kable.Scanner
import com.juul.kable.peripheral
import com.lifesparktech.lsphysio.PeripheralManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DeviceConnectionScreen(navController: NavController) {
      val scope = rememberCoroutineScope()
      Column(
            modifier = Modifier.background(color = Color(0xFFf4f4f4)).fillMaxSize().padding(12.dp)
        ){
          val configuration = LocalConfiguration.current
          val screenWidth = configuration.screenWidthDp.dp
          var context = LocalContext.current
          Column{
              Row(
                  modifier = Modifier.padding(16.dp).fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
              ){
                  Column{
                      Text(text = "Device Connection", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                  }
                  Box(
                      modifier = Modifier
                          .clip(RoundedCornerShape(12.dp))
                          .background(color = Color(0xFFD6E7EE))
                          .padding(12.dp)
                          .clickable{}
                  ){
                      Row(
                          modifier = Modifier.width(110.dp),
                          verticalAlignment = Alignment.CenterVertically,
                          horizontalArrangement = Arrangement.SpaceAround
                      ){
                          Image(
                              painter = painterResource(id = R.drawable.device_connection),
                              contentDescription = "logo",
                              modifier = Modifier
                                  .size(24.dp)
                          )
                          Text("Scanned")
                      }
                  }
              }
              Spacer(modifier = Modifier.height(12.dp))
              Text(text = "Available device",
                  fontWeight = FontWeight.SemiBold,
                  fontSize = 16.sp,
                  color = Color(0xFF474747),
                  modifier = Modifier.padding(start = 12.dp))
              Divider(
                  modifier = Modifier
                      .fillMaxWidth()
                      .padding(vertical = 4.dp, horizontal = 8.dp),
                  color = Color(0xFFD6D6D6),
                  thickness = 1.dp
              )
              Box(
                  modifier = Modifier.height( if(screenWidth <= 800.0.dp ) { 800.dp } else { 400.dp } )
              ){
                  LazyColumn(modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp)) {
                      item{
                          Card(
                              modifier = Modifier.width(250.dp).height(100.dp),
                              elevation = CardDefaults.cardElevation(4.dp),
                              colors = CardDefaults.cardColors(
                                  containerColor = Color.White // Set the card's background color
                              )
                          ) {
                              Row(
                                  modifier = Modifier.fillMaxSize().padding(12.dp),
                                  verticalAlignment = Alignment.CenterVertically,
                                  horizontalArrangement = Arrangement.SpaceBetween
                              ){
                                  Text(text = "TEST_2407")
                                  Spacer(modifier = Modifier.height(12.dp))
                                  Button(
                                      onClick = {
                                          ConnectDeviced(scope,navController, context)
                                      },
                                      shape = RoundedCornerShape(8.dp),
                                      colors = ButtonDefaults.textButtonColors(
                                          containerColor = Color(0xFF005749)
                                      )
                                  ) {
                                      Text(text = "Connect", color = Color.White)
                                  }
                              }
                          }
                      }
                  }
              }
          }
        }
}
@RequiresApi(Build.VERSION_CODES.O)
fun ConnectDeviced(
    scope: CoroutineScope,
    navController: NavController,
    context: Context
) {
    val scanner = Scanner {
        filters = listOf(Filter.Name("TEST_2407"))
    }
    scope.launch {
        try {
            val advertisement = scanner.advertisements.onEach { println(it) }.first()
            val peripheral = scope.peripheral(advertisement)
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
//    "High Freq" to "-1",
//    "Swing phase continuous" to "2",
//    "Swing phase burst" to "3",
//    "Stance phase continuous" to "0",
//    "Stance phase burst" to "1",
//    "Open loop" to "4",
//    "Custom" to "14"