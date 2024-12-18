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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.lifesparktech.lsphysio.android.Controller.fetchPatients
import com.lifesparktech.lsphysio.android.data.Patient
//import com.lifesparktech.lsphysio.android.data.samplePatients
@Composable
fun PatientScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFf4f4f4)),
    ) {
        SimpleTable(navController)
    }
}

@Composable
fun SimpleTable(navController: NavController) {
    val patients = remember { mutableStateOf<List<Patient>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(Unit){
        try{
            isLoading = true
            val result = fetchPatients()
            patients.value = result
        }
        finally {
            isLoading = false
        }
    }
    if (isLoading){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            CircularProgressIndicator(modifier = Modifier.size(40.dp), color = Color.Black)
        }
    }
    else{
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
                    Text(text = "Patients", fontWeight = FontWeight.Bold, fontSize = 24.sp)
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
                                Text("Add Patient")
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
                    TableCell(text = "NAME", modifier = Modifier.weight(0.5f),fontWeight = FontWeight.Bold)
                    TableCell(text = "AGE", modifier = Modifier.weight(0.3f),fontWeight = FontWeight.Bold)
                    TableCell(text = "EMAIL", modifier = Modifier.weight(0.5f),fontWeight = FontWeight.Bold)
                    TableCell(text = "PHONE", modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold)
                    TableCell(text = "STATUS", modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier.height( if(screenWidth <= 800.0.dp ) { 800.dp } else { 400.dp } )
                ){
                    LazyColumn(modifier = Modifier) {
                        item{
                            patients.value.forEachIndexed{ index, patient ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth().clickable{navController.navigate("PatientDetail/${patient.serialNo}")}
                                        .background(if (index % 2 == 0) Color.White else Color(0xFFF8FAFB))
                                ) {
                                    TableCell(text = "${index + 1}", modifier = Modifier.weight(0.3f))
                                    TableCell(text = "${patient.name}", modifier = Modifier.weight(0.5f))
                                    TableCell(text = "${patient.age}", modifier = Modifier.weight(0.3f))
                                    TableCell(text = "${patient.email}", modifier = Modifier.weight(0.5f))
                                    TableCell(text = "${patient.phone}", modifier = Modifier.weight(0.5f))
                                    Box(
                                        modifier = Modifier.weight(0.5f).clip(RoundedCornerShape(8.dp)),
                                        contentAlignment = Alignment.Center
                                    ){
                                        TableCellBadge(
                                            text = patient.status,
                                            textColor = if (patient.status == "Active") Color(0xFF0F5132) else Color(0xFFD0312D), // Conditional color
                                            modifier = Modifier.background(color = Color(0xFFE0F2EE)),
                                            backgroundColor = if (patient.status == "Active") Color(0xFFD6E7EE) else  Color(0xFFFFCACA)
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

}

@Composable
fun TableCell(text: String, textColor: Color = MaterialTheme.colorScheme.onSurface, fontWeight: FontWeight = FontWeight.Normal, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            //.border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
            .padding(8.dp),
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = fontWeight,
        color = textColor
    )
}

@Composable
fun TableCellBadge(text: String, textColor: Color = MaterialTheme.colorScheme.onSurface, backgroundColor: Color = Color.Transparent, fontWeight: FontWeight = FontWeight.Normal, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(120.dp)
            .background(
                // Color(0xFFE3F2EE)
                backgroundColor
            ).padding(horizontal = 12.dp, vertical = 8.dp).clip(RoundedCornerShape(12.dp)),
    ){
        Row(
            // modifier.clip())
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = painterResource(id = if(text =="Active"){ R.drawable.radiogreen } else{ R.drawable.radiored }),
                contentDescription = "",
                modifier = Modifier
                    .size(14.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = fontWeight,
                color = textColor
            )
        }

    }
}
