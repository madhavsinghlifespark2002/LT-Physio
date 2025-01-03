package com.lifesparktech.lsphysio.android.pages
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults.contentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.benasher44.uuid.uuid4
import com.example.lsphysio.android.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.lifesparktech.lsphysio.android.Controller.addPatient
import com.lifesparktech.lsphysio.android.Controller.fetchPatientById
import com.lifesparktech.lsphysio.android.Controller.updatePatient
import com.lifesparktech.lsphysio.android.components.CommonTextFieldgrey
import com.lifesparktech.lsphysio.android.data.Patient
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatedPatientScreen(navController: NavController, patientId: String) {
    var patient by remember { mutableStateOf<Patient?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = MainScope()
    var name by remember { mutableStateOf("") }
    LaunchedEffect(patientId) {
        try {
            isLoading = true
            patient = fetchPatientById(patientId)
            name = patient!!.name
        } finally {
            isLoading = false
        }
    }
    Card(
        modifier = Modifier.padding(12.dp),//.height(700.dp).fillMaxWidth(0.35f),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White // Set the card's background color
        )
    ) {
        LazyColumn(
            modifier = Modifier
                // .weight(1f)
                .padding(16.dp)
                .imePadding()
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.clickable { navController.popBackStack() }
                    )
                    Text(
                        text = "Update  Patient",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF222429)
                    )
                }
            }
            item {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    color = Color(0xFFD6D6D6),
                    thickness = 1.dp
                )
            }
            item {
                //  Spacer(modifier = Modifier.height(50.dp))
                CommonTextFieldgrey(
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    label = "Name"
                )
            }


            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val patient = Patient(
                            serialNo = uuid4().toString(),
                            clinicId = Firebase.auth.currentUser?.uid ?: "",
                            name = name,
                            age = 0,
                            gender = "gender",
                            phone = "",
                            address = "",
                            email = "",
                            height = 0,
                            weight = 0,
                            diagnostics = emptyList(),
                            extraDetails = emptyList(),
                            bloodGroup = "",
                            allergies = "",
                            bodymassindex = 0.0,
                            bloodPressure = "",
                            diabetesemergencies = "",
                            chronicdisease = "",
                            surgery = "",
                            hepaticIssues = "",
                            familydisease = "",
                            relatedcomplication = "",
                            hemoglobin = "",
                            bloodsugar = "",
                            heartrate = "",
                        )
                        scope.launch {
                            updatePatient(patientId, patient)
                            navController.popBackStack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFF43958F)),
                    modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .imePadding(),
                    shape = RoundedCornerShape(4.dp),
                ) {
                    Text(text = "Update")
                }
            }
        }
    }
}
