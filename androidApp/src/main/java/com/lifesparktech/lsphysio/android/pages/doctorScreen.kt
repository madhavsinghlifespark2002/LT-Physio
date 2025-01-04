package com.lifesparktech.lsphysio.android.pages
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lsphysio.android.R
import com.lifesparktech.lsphysio.android.data.sampleDoctors

@Composable
fun DoctorScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFf4f4f4)),
    ) {
        SimpleTableDoctor(navController)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SimpleTableDoctor(navController: NavController) {
    Card(
        modifier = Modifier.padding(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8FAFB) // Set the card's background color
        )
    ) {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp
        println("ScreenWidth: $screenWidth")
        Column{
            FlowRow(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
               // verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(text = "Doctor List", fontWeight = FontWeight.Bold, fontSize = 24.sp)
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
                            Text("Add Doctor")
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
                verticalAlignment = Alignment.CenterVertically,
                modifier =
                    if(screenWidth >=800.dp){
                        Modifier
                            .fillMaxWidth()
                    }else{

                        Modifier.horizontalScroll(rememberScrollState()).fillMaxWidth()
                    }
            ){
                if(screenWidth >= 800.0.dp){
                    TableCell(text = "NO.", modifier = Modifier.weight(0.3f),fontWeight = FontWeight.Bold)
                    TableCell(text = "NAME", modifier = Modifier.weight(0.5f),fontWeight = FontWeight.Bold)
                    TableCell(text = "CONTACT", modifier = Modifier.weight(0.75f),fontWeight = FontWeight.Bold)
                    TableCell(text = "WORKING DAYS", modifier = Modifier.weight(0.8f), fontWeight = FontWeight.Bold)
                    TableCell(text = "DEPARTMENT", modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold)
                }
                else{
                    TableCell(text = "NO.", fontWeight = FontWeight.Bold)
                    TableCell(text = "NAME", fontWeight = FontWeight.Bold)
                    TableCell(text = "CONTACT",fontWeight = FontWeight.Bold)
                    TableCell(text = "WORKING DAYS", fontWeight = FontWeight.Bold)
                    TableCell(text = "DEPARTMENT", fontWeight = FontWeight.Bold)
                }

            }
            Box(
                modifier = Modifier.fillMaxHeight(0.85f)
            ){
                LazyColumn(
                    modifier = Modifier.fillMaxHeight() // Enable horizontal scrolling
                ) {
                    item{
                        FlowRow{
                            sampleDoctors.forEachIndexed { index, doctor ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier =
                                        if(screenWidth >= 800.0.dp){
                                            println("inside not scroll")
                                            Modifier.fillMaxWidth()
                                                .background(if (index % 2 == 0) Color.White else Color(0xFFF8FAFB))
                                        }
                                        else{
                                            println("inside scroll")
                                            Modifier.horizontalScroll(rememberScrollState()).fillMaxWidth()
                                                .background(if (index % 2 == 0) Color.White else Color(0xFFF8FAFB))

                                        }

                                ) {
                                    if(screenWidth >= 800.0.dp ){
                                        TableCell(text = "${doctor.serialNo}", modifier = Modifier.weight(0.3f))
                                        TableCell(text = "${doctor.name}", modifier = Modifier.weight(0.5f))
                                        TableCell(text = "${doctor.phone}\n ${doctor.email}", modifier = Modifier.weight(0.75f))
                                        TableCellBadgeDoctor(modifier = Modifier.weight(0.8f), workingDays = doctor.workingDays)
                                        TableCell(text = "${doctor.department}", modifier = Modifier.weight(0.5f))
                                        Spacer(modifier = Modifier.width(4.dp))
                                    }
                                    else{
                                        TableCell(text = "${doctor.serialNo}")
                                        TableCell(text = "${doctor.name}")
                                        TableCell(text = "${doctor.phone}\n ${doctor.email}")
                                        TableCellBadgeDoctor( workingDays = doctor.workingDays)
                                        TableCell(text = "${doctor.department}")
                                        Spacer(modifier = Modifier.width(4.dp))
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
    }
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

@Composable
fun TableCellBadgeDoctor( modifier: Modifier = Modifier, workingDays: List<String>) {
    val allDays = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
    Row(
        modifier = modifier
            .border(width = 1.dp, color = Color.Transparent),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        allDays.forEach { day ->
            val backgroundColor = if (workingDays.contains(day)) Color(0xFFD6E7EE) else Color(0xFFf4f4f4)
            Text(
                text = day.take(1), // Take the first letter of the day (e.g., "S", "M")
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color = backgroundColor)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}
