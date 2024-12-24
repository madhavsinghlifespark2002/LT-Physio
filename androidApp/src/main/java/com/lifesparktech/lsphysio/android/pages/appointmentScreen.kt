package com.lifesparktech.lsphysio.android.pages
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lsphysio.android.R
import com.lifesparktech.lsphysio.android.data.sampleAppointments

@Composable
fun AppointmentScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFf4f4f4)),
    ) {
        SimpleTableAppointment(navController)
    }
}

@Composable
fun SimpleTableAppointment(navController: NavController) {
    Card(
        modifier = Modifier.padding(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8FAFB) // Set the card's background color
        )
    ) {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp
        Column{
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(text = "Appointments", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                Row{
                    Box(
                        modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(color = Color(0xFFD6E7EE)).padding(12.dp)
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.sorted),
                            contentDescription = "sorted",
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(
                        modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(color = Color(0xFFD6E7EE)).padding(12.dp)
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.filter),
                            contentDescription = "filter",
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(
                        modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(color = Color(0xFFD6E7EE)).padding(12.dp).clickable{navController.navigate("addpatientscreen")}
                    ){
                        Row(
                           // modifier = Modifier.width(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround
                        ){
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Add Appointment")
                        }

                    }
                }

            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                color = Color(0xFFD6D6D6),
                thickness = 1.dp
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                TableCell(text = "No.", modifier = Modifier.weight(0.3f),fontWeight = FontWeight.Bold)
                TableCell(text = "DOCTOR", modifier = Modifier.weight(0.5f),fontWeight = FontWeight.Bold)
                TableCell(text = "DEPARTMENT", modifier = Modifier.weight(0.6f),fontWeight = FontWeight.Bold)
                TableCell(text = "Timings", modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold)
                TableCell(text = "PATIENT", modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold)
                TableCell(text = "STATUS", modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold)
            }
            Box(
                modifier = Modifier.height( if(screenWidth <= 800.0.dp ) { 800.dp } else { 400.dp } )
            ){
                LazyColumn(modifier = Modifier) {
                    item{
                        sampleAppointments.forEachIndexed { index, appointment ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                                    .background(if (index % 2 == 0) Color.White else Color(0xFFF8FAFB))
                            ) {
                                TableCell(text = "${appointment.appointmentId}", modifier = Modifier.weight(0.3f))
                                TableCell(text = "${appointment.doctorName}", modifier = Modifier.weight(0.5f))
                                TableCell(text = "${appointment.department}", modifier = Modifier.weight(0.6f))
                                TableCell(text = "${appointment.appointmentDate} \n ${appointment.appointmentTime}", modifier = Modifier.weight(0.5f))
                                TableCell(text = "${appointment.patientName}", modifier = Modifier.weight(0.5f))
                                Box(
                                    modifier = Modifier.weight(0.5f).clip(RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ){
                                    TableCellBadgeSchedule(
                                        text = appointment.status,
                                        textColor =
                                            if (appointment.status == "Booked" || appointment.status == "Completed")
                                                Color(0xFF0F5132)
                                            else
                                                Color(0xFFD0312D),
                                        modifier = Modifier.background(color = Color(0xFFE0F2EE)),
                                        backgroundColor =
                                            if (appointment.status == "Booked" || appointment.status == "Completed")
                                                Color(0xFFD6E7EE)
                                            else
                                                Color(0xFFFFCACA)
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                        }
                    }
                }
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                color = Color(0xFFD6D6D6),
                thickness = 1.dp
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Button(
                    border = BorderStroke(width = 1.dp, color = Color(0xFFD6D6D6)),
                    onClick = {},
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = Color.White
                    ) //222429
                ){
                    Text(text = "Previous", color = Color(0xFF222429))
                }
                Text("Page 1 of 12")
                Button(
                    border = BorderStroke(width = 1.dp, color = Color(0xFFD6D6D6)),
                    onClick = {},
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = Color.White
                    ) //222429
                ){
                    Text(text = "Next", color = Color(0xFF222429))
                }
            }
        }
    }
}
