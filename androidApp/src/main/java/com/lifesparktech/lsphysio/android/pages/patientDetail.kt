package com.lifesparktech.lsphysio.android.pages
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults.contentColor
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lsphysio.android.R
import com.lifesparktech.lsphysio.android.Controller.fetchPatientById
import com.lifesparktech.lsphysio.android.data.Patient
import java.nio.file.WatchEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDetail(navController: NavController, patientId: String) {
    var patient by remember { mutableStateOf<Patient?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(patientId) {
        try {
            isLoading = true
            patient = fetchPatientById(patientId)
        } finally {
            isLoading = false
        }
    }
    Scaffold(
        topBar = {
        },
        containerColor = Color.White
    ) { contentPadding ->
        if (isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(modifier = Modifier.size(40.dp), color = Color.Black)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().background(color = Color(0xFFF4F4F4))
            ) {
                item {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Card(
                            modifier = Modifier.padding(12.dp).height(725.dp)
                                .fillMaxWidth(0.35f),
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
                                Text(
                                    text = "${patient?.name}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp
                                )
                                Text(
                                    text = "Age:",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.sp,
                                    color = Color.Gray
                                )
                                Column(modifier = Modifier.fillMaxHeight().padding(12.dp)) {
                                    Text(
                                        text = "Basic Informational",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 16.sp,
                                        color = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    PatientItemDetail(
                                        res = R.drawable.gender,
                                        label = "Gender",
                                        content = patient!!.gender
                                    )
                                    PatientItemDetail(
                                        res = R.drawable.dob,
                                        label = "Date of birth",
                                        content = "${patient?.gender}"
                                    )
                                    PatientItemDetail(
                                        res = R.drawable.phone,
                                        label = "Phone Number",
                                        content = patient!!.phone
                                    )
                                    PatientItemDetail(
                                        res = R.drawable.mail,
                                        label = "Email",
                                        content = patient!!.email
                                    )
                                    PatientItemDetail(
                                        res = R.drawable.location,
                                        label = "Address",
                                        content = patient!!.address
                                    )
                                    Box{
                                        Button(onClick = { navController.navigate("updatedPatientScreen/${patientId}") }) {
                                            Text("Edit")
                                        }
                                    }
                                }

                            }
                        }
                        Card(
                            modifier = Modifier.padding(12.dp).height(725.dp),
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
                                Column(modifier = Modifier.fillMaxHeight().padding(12.dp)) {
                                    Text(
                                        text = "Medical History",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        color = Color.Black
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    PatientItemDetail(
                                        res = R.drawable.ecg_heart,
                                        label = "Chronic disease",
                                        content = patient!!.chronicdisease
                                    )
                                    PatientItemDetail(
                                        res = R.drawable.healing,
                                        label = "Diabetes Emergencies",
                                        content = patient!!.diabetesemergencies
                                    )
                                    PatientItemDetail(
                                        res = R.drawable.vaccines,
                                        label = "Surgery",
                                        content = patient!!.surgery
                                    )
                                    PatientItemDetail(
                                        res = R.drawable.family,
                                        label = "Family Disease",
                                        content = patient!!.familydisease
                                    )
                                    PatientItemDetail(
                                        res = R.drawable.medical,
                                        label = "Related Complication",
                                        content = patient!!.relatedcomplication
                                    )
                                    PatientItemDetail(
                                        res = R.drawable.liver,
                                        label = "Hepatic Issues",
                                        content = patient!!.hepaticIssues
                                    )
                                    PatientItemDetail(
                                        res = R.drawable.stomach,
                                        label = "Digestive Disorders",
                                        content = "GERD, Irritable Bowel Syndrome"
                                    )
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
                            Spacer(modifier = Modifier.height(12.dp))
                            Column(modifier = Modifier.fillMaxHeight().padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Medical Info",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        color = Color.Black
                                    )
                                    Row {
                                        Text(
                                            text = "Last Updated on ",
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 14.sp,



                                            color = Color.Gray
                                        )
                                        Text(
                                            text = "15 Jun 2024, 10:45 AM",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = Color.DarkGray
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(
                                        // modifier = Modifier.fillMaxWidth(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        PatientItemDetailInfo(
                                            res = R.drawable.height,
                                            label = "Body Height",
                                            content = "${patient!!.height}cm"
                                        )
                                        Spacer(modifier = Modifier.height(14.dp))
                                        PatientItemDetailInfo(
                                            res = R.drawable.bloodtype,
                                            label = "Blood Group",
                                            content = patient!!.bloodGroup
                                        )
                                        Spacer(modifier = Modifier.height(14.dp))
                                        PatientItemDetailInfo(
                                            res = R.drawable.ecg_heart,
                                            label = "Heart Rate",
                                            content = "${patient!!.heartrate}"
                                        )

                                    }
                                    Column(
                                        // modifier = Modifier.fillMaxWidth(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        PatientItemDetailInfo(
                                            res = R.drawable.monitor_weight,
                                            label = "Body Weight",
                                            content = "${patient!!.weight}"
                                        )

                                        Spacer(modifier = Modifier.height(14.dp))
                                        PatientItemDetailInfo(
                                            res = R.drawable.blood_pressure,
                                            label = "Blood Pressure",
                                            content = "${patient!!.bloodPressure}"
                                        )
                                        Spacer(modifier = Modifier.height(14.dp))
                                        PatientItemDetailInfo(
                                            res = R.drawable.allergies,
                                            label = "Allergies",
                                            content = patient!!.allergies
                                        )
                                    }
                                    Column(
                                        // modifier = Modifier.fillMaxWidth(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        PatientItemDetailInfo(
                                            res = R.drawable.monitor_weight,
                                            label = "Body Mass index",
                                            content = "${patient!!.bodymassindex}"
                                        )
                                        Spacer(modifier = Modifier.height(14.dp))
                                        PatientItemDetailInfo(
                                            res = R.drawable.glucose,
                                            label = "Blood Sugar",
                                            content = "${patient!!.bloodsugar}"
                                        )
                                        Spacer(modifier = Modifier.height(14.dp))
                                        PatientItemDetailInfo(
                                            res = R.drawable.humidity_low,
                                            label = "Hemoglobin",
                                            content = "${patient!!.hemoglobin}"
                                        )
                                    }
                                }

                            }

                        }
                    }
                    Row {
                        Card(
                            modifier = Modifier.padding(12.dp).height(660.dp)
                                .fillMaxWidth(0.45f),
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
                                Column(modifier = Modifier.fillMaxHeight().padding(12.dp)) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "Appointments",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 22.sp,
                                        color = Color.Black
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                    AppointmentsDetail(
                                        res = R.drawable.radio_button,
                                        label = "Post-Surgical Care",
                                        date = "12 Oct 2024",
                                        content = "Drg.Marvin McKinney"
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    AppointmentsDetail(
                                        res = R.drawable.radio_button,
                                        label = "Post-Surgical Care",
                                        date = "22 Sept 2024",
                                        content = "Drg.Marvin McKinney"
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    AppointmentsDetail(
                                        res = R.drawable.radio_button,
                                        label = "Post-Surgical Care",
                                        date = "2 Aug 2024",
                                        content = "Drg.Marvin McKinney"
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    AppointmentsDetail(
                                        res = R.drawable.radio_button,
                                        label = "Post-Surgical Care",
                                        date = "14 July 2024",
                                        content = "Drg.Marvin McKinney"
                                    )
                                    Spacer(modifier = Modifier.height(22.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Text(
                                            "See All",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFF43958F)
                                        )
                                    }

                                }
                            }
                        }
                        Card(
                            modifier = Modifier.padding(12.dp).height(660.dp),
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
                                Column(modifier = Modifier.fillMaxHeight().padding(12.dp)) {
                                    Text(
                                        text = "Reports",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 22.sp,
                                        color = Color.Black
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    patient?.stsTest?.forEach{test ->
                                        ReportItemDetailInfo(
                                            res = R.drawable.report_zip,
                                            label = test.toString(),
                                            date = "12 Oct 2024"
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Text(
                                            "See All",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFF43958F)
                                        )
                                    }

                                }
                            }
                        }
                    }

                }

            }
        }
    }

}
@Composable
fun PatientItemDetail(
    @DrawableRes res: Int,
    label: String,
    content: String,
    isfullwidth: Boolean = true
) {
    Spacer(modifier = Modifier.height(12.dp))
    Row(
        modifier = if (isfullwidth == true) Modifier.fillMaxWidth() else Modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = res),
            contentDescription = "$label",
            modifier = Modifier
                .width(26.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "${label}",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
    Spacer(modifier = Modifier.height(4.dp))
    Row {
        Spacer(modifier = Modifier.width(36.dp))
        Text(text = "${content}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun AppointmentsDetail(@DrawableRes res: Int, date: String, label: String, content: String) {
    Column(
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = res),
                contentDescription = "$label",
                modifier = Modifier
                    .width(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${date}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row {
            Spacer(modifier = Modifier.width(26.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                //elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF2F2F2) // Set the card's background color
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(text = "${label}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "${content}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray
                    )
                }
            }

        }

    }
}

@Composable
fun PatientItemDetailInfo(@DrawableRes res: Int, label: String, content: String) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    println("the ScreenWidth: $screenWidth")
    Column {
        Spacer(modifier = Modifier.height(12.dp))
        Row(
//        modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
                    .background(color = Color(0xFFD6E7EE))
                    .padding(12.dp)
            ) {
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
            ) {
                Text(
                    text = "${label}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "${content}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

    }
}

@Composable
fun ReportItemDetailInfo(@DrawableRes res: Int, date: String, label: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        //elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF2F2F2) // Set the card's background color
        )
    ) {
        Row( // Wrap content in a Row for proper layout
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = res),
                contentDescription = label,
                modifier = Modifier
                    .width(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = label,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = date,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )
            }
        }
    }
}