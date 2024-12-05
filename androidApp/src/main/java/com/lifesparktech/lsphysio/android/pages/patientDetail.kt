package com.lifesparktech.lsphysio.android.pages

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lsphysio.android.R
import com.lifesparktech.lsphysio.android.Controller.fetchPatientById
import com.lifesparktech.lsphysio.android.models.Patient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDetail(navController: NavController, patientId: String) {
    var patient by remember { mutableStateOf<Patient?>(null) }

    // Fetch patient data when the composable is launched
    LaunchedEffect(patientId) {
        val loadedPatient = fetchPatientById(patientId) // fetchPatientById is not a Flow
        patient = loadedPatient
    }
//    if (patient != null) {
//        Text(text = "Name: ${patient!!.name}")
//        Text(text = "Age: ${patient!!.age}")
//        Text(text = "Gender: ${patient!!.gender}")
//        Text(text = "Contact: ${patient!!.contact}")
//    } else {
//        Text(text = "Loading patient details...")
//    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth().background(color = Color.White),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "logo",
                            modifier = Modifier
                                .width(200.dp)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White // Set your desired color here
                )
            )
        },
        containerColor = Color.White
    ){
        LazyColumn(modifier = Modifier.fillMaxSize().background(color = Color(0xFFF4F4F4))){
            item{
                Spacer(modifier = Modifier.height(70.dp))
            }
            item {
                Row(modifier = Modifier.fillMaxWidth()){
                    Card(
                        modifier = Modifier.padding(12.dp).height(700.dp).fillMaxWidth(0.35f),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White // Set the card's background color
                        )
                    ) {
                        Column(
                            // verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(12.dp).fillMaxSize()
                        ) {
                            Spacer(modifier = Modifier.height(32.dp))
                            Box(
                                modifier = Modifier
                                    .size(100.dp)  // Circle size
                                    .clip(CircleShape)  // Makes the icon circular
                                    .background(Color(0xFF3A3840))  // Set background color from list
                                    .padding(8.dp),  // Padding inside the circle
                                contentAlignment = Alignment.Center  // Center the text inside the circle
                            ) {
                                Text(
                                    text = "${patient?.name?.toUpperCase()[0]}"
                                        .toString(),  // First character of the patient's name
                                    color = Color.White,  // Text color inside the circle
                                    style = TextStyle(fontSize = 45.sp)  // Text style
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(text = "${patient?.name}", fontWeight = FontWeight.Bold, fontSize = 22.sp)
                            Text(text = "Age: ${patient?.age}", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = Color.Gray)
                            Column(modifier = Modifier.fillMaxHeight().padding(12.dp)){
                                Text(text = "Basic Informational", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.Gray)
                                Spacer(modifier = Modifier.height(12.dp))
                                PatientItemDetail(res = R.drawable.gender, label = "Gender", content = "${patient?.gender}")
                                PatientItemDetail(res = R.drawable.dob, label = "Date of birth", content = "${patient?.gender}")
                                PatientItemDetail(res = R.drawable.phone, label = "Phone Number", content = patient?.contact?.let {
                                    "+${it.substring(0, 2)}-${it.substring(2)}"
                                }.toString())
                                PatientItemDetail(res = R.drawable.mail, label = "Email", content = "${patient?.email}")
                                PatientItemDetail(res = R.drawable.location, label = "Address", content = "${patient?.address}")

                            }

                        }
                    }
                    Card(
                        modifier = Modifier.padding(12.dp).height(700.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White // Set the card's background color
                        )
                    ) {
                        Column(
                            // verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(12.dp).fillMaxSize()
                        ) {
                            Spacer(modifier = Modifier.height(18.dp))
                            Column(modifier = Modifier.fillMaxHeight().padding(12.dp)){
                                Text(text = "Medical History", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
                                Spacer(modifier = Modifier.height(12.dp))
                                PatientItemDetail(res = R.drawable.heart, label = "Chronic disease", content = "IHD Obesity, Chronic thyriod disorder")
                                PatientItemDetail(res = R.drawable.healing, label = "Diabetes Emergencies", content = "Diabetic Ketoacidosis")
                                PatientItemDetail(res = R.drawable.vaccines, label = "Sugery", content = "Liposuction")
                                PatientItemDetail(res = R.drawable.family, label = "Family Disease", content = "Obesity (Father)")
                                PatientItemDetail(res = R.drawable.medical, label = "Related Complication", content = "Nephropathy, Neuropathy, Retinopathy, Diabetic Foot")
                                PatientItemDetail(res = R.drawable.liver, label = "Hepatic Issues", content = "Hepatitis B, Fatty Liver Disease")
                                PatientItemDetail(res = R.drawable.stomach, label = "Digestive Disorders", content = "GERD, Irritable Bowel Syndrome")
                            }

                        }
                    }
                }
                Card(
                    modifier = Modifier.padding(12.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White // Set the card's background color
                    )
                ) {
                    Column(
                        // verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(12.dp).fillMaxSize()
                    ) {
                        Spacer(modifier = Modifier.height(18.dp))
                        Column(modifier = Modifier.fillMaxHeight().padding(12.dp)){
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ){
                                Text(text = "Medical Info", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
                                Row{
                                    Text(text = "Last Updated on ", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.Gray)
                                    Text(text = "15 Jun 2024, 10:45 AM", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Column(
                               // modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){
                                PatientItemDetailInfo(res = R.drawable.heart, label = "Body Height", content = "${patient?.height} CM")
                                PatientItemDetailInfo(res = R.drawable.healing, label = "Body Weight", content = "70 KG")
                                PatientItemDetailInfo(res = R.drawable.vaccines, label = "Body Mass index", content = "135 lbs")
                            }
                            Column(
                                // modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){
                                PatientItemDetailInfo(res = R.drawable.family, label = "Blood Group", content = "A+")
                                PatientItemDetailInfo(res = R.drawable.medical, label = "Blood Pressure", content = "120/80 mmHG")
                                PatientItemDetailInfo(res = R.drawable.liver, label = "Blood Sugar", content = "90 mg/dL")
                            }
                            Column(
                                // modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){
                                PatientItemDetailInfo(res = R.drawable.stomach, label = "Heart Rate", content = "72 bpm")
                                PatientItemDetailInfo(res = R.drawable.stomach, label = "Allergies", content = "Food Allergies")
                                PatientItemDetailInfo(res = R.drawable.stomach, label = "14 g/dL", content = "Homoglobin")
                            }
                        }

                    }
                }
            }
        }
    }

}
@Composable
fun PatientItemDetail(@DrawableRes res: Int, label: String, content: String, isfullwidth: Boolean = true){
    Spacer(modifier = Modifier.height(12.dp))
    Row(
        modifier = if(isfullwidth == true) Modifier.fillMaxWidth() else Modifier,
        verticalAlignment = Alignment.CenterVertically,
    ){
        Image(
            painter = painterResource(id = res),
            contentDescription = "$label",
            modifier = Modifier
                .width(26.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = "${label}", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.Gray)
    }
    Spacer(modifier = Modifier.height(4.dp))
    Row{
        Spacer(modifier = Modifier.width(36.dp))
        Text(text = "${content}",  fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun PatientItemDetailInfo(@DrawableRes res: Int, label: String, content: String){
    Column{
        Spacer(modifier = Modifier.height(12.dp))
        Row(
//        modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ){
            Box(
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
                .background(color = Color(0xFFD6E7EE))
                .padding(12.dp)
            ){
                Image(
                    painter = painterResource(id = res),
                    contentDescription = "$label",
                    modifier = Modifier
                        .width(32.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ){
                Text(text = "${label}", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "${content}",  fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

    }
}
