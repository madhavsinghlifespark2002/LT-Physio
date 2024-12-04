package com.lifesparktech.lsphysio.android.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.lifesparktech.lsphysio.android.Controller.fetchPatients
import com.lifesparktech.lsphysio.android.models.Patient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


@Composable
fun ProfileScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val iconColors =
        listOf(Color(0xFF008000), Color(0xFF991f00), Color(0xFF1f3d7a), Color(0xFFe68a00), Color(0xFFffbb33))
    val scope = MainScope()
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var patients by remember { mutableStateOf<List<Patient>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            patients = fetchPatients()
            isLoading = false
        }
    }
    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF4F4F4)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { newValue -> searchQuery = newValue },
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .weight(0.8f)
                        .onFocusChanged { focusState -> },
                    textStyle = androidx.compose.material3.LocalTextStyle.current.copy(color = Color.Black),
                    singleLine = true,
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                    ),
                    placeholder = {
                        Text(text = "Search Patients")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {navController.navigate("addpatientscreen") },
                    modifier = Modifier
                        .height(56.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF43958F))
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                }
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(3), // 3 columns
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp) // Add padding around the grid
            ) {
                items(patients.size) { index ->
                    PatientItem(
                        patient = patients[index],
                        navController = navController
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PatientItem(patient: Patient, navController: NavController){
    Card(
        modifier = Modifier.padding(12.dp).clickable {
        },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White // Set the card's background color
        )
    ) {
        Column(
            modifier = Modifier
                .height(275.dp)
                .fillMaxWidth()
                .background(color = Color.White)
                .clickable {
                    println("onclick")
                    navController.navigate("PatientDetail/${patient.id}")
                }.padding(12.dp).padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                text = "${patient.name}, ${patient.age}",
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
            )
            Spacer(modifier = Modifier.height(2.dp))
//            Text(
//                text = "${patient.email}",
//                style = TextStyle(fontSize = 16.sp),
//                modifier = Modifier.weight(1f)
//            )
            Spacer(modifier = Modifier.height(2.dp))
            FlowRow(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                patient.diagnostics.take(3).forEach { diagnostic ->
                    Box(
                        modifier = Modifier
                            .background(color = Color(0xFFf9f9f8))
                            .padding(12.dp)
                            //.padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = diagnostic,
                            style = TextStyle(fontSize = 16.sp)
                        )
                    }
                }
            }
        }
    }
}
