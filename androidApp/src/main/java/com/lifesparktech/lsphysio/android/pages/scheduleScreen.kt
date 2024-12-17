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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lsphysio.android.R
import com.lifesparktech.lsphysio.android.data.sampleSchedules

@Composable
fun ScheduleScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFf4f4f4)),
    ) {
        SimpleTableSchedule(navController)
    }
}
@Composable
fun SimpleTableSchedule(navController: NavController) {
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
                Text(text = "Schedule", fontWeight = FontWeight.Bold, fontSize = 24.sp)
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
                            modifier = Modifier.width(110.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround
                        ){
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                            Text("Add Schedule")
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
                TableCell(text = "DATE", modifier = Modifier.weight(0.4f),fontWeight = FontWeight.Bold)
                TableCell(text = "START -\n END TIME", modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold)
                TableCell(text = "PATIENT", modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold)
                TableCell(text = "STATUS", modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold)
            }
            Box(
                modifier = Modifier.height( if(screenWidth <= 800.0.dp ) { 800.dp } else { 400.dp } )
            ){
                LazyColumn(modifier = Modifier) {
                    item{
                        sampleSchedules.forEachIndexed { index, schedule ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                                    .background(if (index % 2 == 0) Color.White else Color(0xFFF8FAFB))
                            ) {
                                TableCell(text = "${schedule.scheduleId}", modifier = Modifier.weight(0.3f))
                                TableCell(text = "${schedule.doctorName}", modifier = Modifier.weight(0.5f))
                                TableCell(text = "${schedule.department}", modifier = Modifier.weight(0.6f))
                                TableCell(text = "${schedule.date}", modifier = Modifier.weight(0.4f))
                                TableCell(text = "${schedule.startTime} -\n ${schedule.endTime}", modifier = Modifier.weight(0.5f))
                                TableCell(text = "${schedule.patientName}", modifier = Modifier.weight(0.5f))
                                Box(
                                    modifier = Modifier.weight(0.5f).clip(RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ){
                                    TableCellBadgeSchedule(
                                        text = schedule.status,
                                        textColor =
                                            if (schedule.status == "Available" || schedule.status == "Completed")
                                                Color(0xFF0F5132)
                                            else if (schedule.status == "Booked")
                                                Color(0xFF222429)
                                            else
                                                Color(0xFFD0312D), // Conditional color
                                        modifier = Modifier.background(color = Color(0xFFE0F2EE)),
                                        backgroundColor =
                                            if (schedule.status == "Available" || schedule.status == "Completed")
                                                Color(0xFFD6E7EE)
                                            else if (schedule.status == "Booked")
                                                Color(0xFFfff6e7)
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

// TODO FOR refactor
@Composable
fun TableCellBadgeSchedule(text: String, textColor: Color = MaterialTheme.colorScheme.onSurface, backgroundColor: Color = Color.Transparent, fontWeight: FontWeight = FontWeight.Normal, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            //.width(100.dp)
            .background(
                backgroundColor
            ).padding(horizontal = 12.dp, vertical = 8.dp).clip(RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ){
        Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = fontWeight,
                color = textColor
            )
        }
}
