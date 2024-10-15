package com.lifesparktech.lsphysio.android

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.lifesparktech.lsphysio.FogTest
import com.lifesparktech.lsphysio.FogTest.labels
import com.lifesparktech.lsphysio.PatientData
import com.lifesparktech.lsphysio.PatientData.CurrentPatient.patientData
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FOG(navController: NavHostController) {
    FogScoringGuide(onSubmit = { navController.navigate("FogForm") })
}

@Composable
fun FogForm(navController: NavHostController) {
    var without by remember { mutableStateOf(List(labels.size) { "" }) }
    var with by remember { mutableStateOf(List(labels.size) { "" }) }
    var allCellsFilled by remember { mutableStateOf(false) }

    // Function to check if all cells are filled
    fun checkAllCellsFilled() {
        allCellsFilled = without.all { it.isNotEmpty() } && with.all { it.isNotEmpty() }
    }



    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        item {
            Text("FOG Test:", style = MaterialTheme.typography.headlineLarge)
        }
        items(labels) { label ->
            val index = labels.indexOf(label)
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = label,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                )
                TextField(colors = TextFieldDefaults.colors(
                ),
                    value = without[index],
                    onValueChange = { newValue ->
                        if (newValue.isDigitsOnly() && newValue.isNotEmpty()) {
                            if (newValue.toInt() < 4) {
                                without = without.toMutableList().apply { this[index] = newValue }
                                checkAllCellsFilled()
                            }
                        }
                        if (newValue.isEmpty()) {
                            without = without.toMutableList().apply { this[index] = newValue }
                        }
                        checkAllCellsFilled()

                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    label = { Text("Without") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                    )
                )
                TextField(value = with[index],
                    onValueChange = { newValue ->
                        if (newValue.isDigitsOnly() && newValue.isNotEmpty()) {
                            if (newValue.toInt() < 4) {
                                with = with.toMutableList().apply { this[index] = newValue }
                                checkAllCellsFilled()
                            }
                        }
                        if (newValue.isEmpty()) {
                            with = with.toMutableList().apply { this[index] = newValue }
                            checkAllCellsFilled()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    label = { Text("With") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                    )
                )
            }
        }

        item {
            Button(
                onClick = {
                    patientData.totalWithout = without.sumOf { it.toInt() }
                    patientData.totalWith = with.sumOf { it.toInt() }
                    patientData.scoreMap = with.zip(without)
                    navController.navigate("ConfirmFOG")
                }, modifier = Modifier
                    .fillMaxWidth()
                    .imePadding(), enabled = allCellsFilled
            ) {
                Text("Submit")
            }
        }
    }
}

@Composable
fun FogScoringGuide(onSubmit: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text("Scoring Guide", style = MaterialTheme.typography.headlineLarge)
        Text("0 = No Freezing")
        Text("1 = Minor festination, shuffling")
        Text("2 = FOG(trembling in place, total akinesia) overcome by patient without external help")
        Text("3 = Severe FOG (Task aborted or external interference needed)")
        Button(onClick = { onSubmit() }) {
            Text("Confirm")
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Confirm(navController: NavHostController) {
    var summary by remember { mutableStateOf("") }
    var allCellsFilled by remember { mutableStateOf(false) }
    var eligibilityExpanded by remember { mutableStateOf(false) }
    val eligibilityOptions = listOf("Eligible", "Ineligible")

    fun checkAllCellsFilled() {
        allCellsFilled = summary.isNotEmpty() && patientData.eligibility.isNotEmpty()
    }

    LazyColumn {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Total Score without device",
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                )
                Text(
                    text = patientData.totalWithout.toString(),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Total Score with device",
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                )
                Text(
                    text = patientData.totalWith.toString(),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                )
            }
        }
        item {
                Text(
                    modifier = Modifier
                        .padding(8.dp),
                    text = "Based on the patient’s H&Y ${patientData.hnyScore} and FOG Score we conclude that the patient will be ",
//                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        item{
            Row(modifier = Modifier
                .clickable { eligibilityExpanded = true }
                .wrapContentSize(Alignment.TopStart)
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.secondaryContainer)) {
                Text(
                    text = patientData.eligibility.ifEmpty { "Select Eligibility" },
                    modifier = Modifier
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,

                )
                IconButton(onClick = { eligibilityExpanded = true }) {
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = "Expand dropdown")
                }
                DropdownMenu(expanded = eligibilityExpanded,
                    onDismissRequest = { eligibilityExpanded = false }) {
                    eligibilityOptions.forEach { option ->
                        DropdownMenuItem(text = { Text(option) }, onClick = {
                            patientData.eligibility = option
                            eligibilityExpanded = false
                            checkAllCellsFilled()
                        })
                    }

                }
            }
        }
        item {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {

                Text(
                    text = " for the stimulation from the WALK Device.",
                )
            }
            Spacer(modifier =Modifier.height(8.dp))

        }
        item {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Doctor's Notes: ",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Spacer(modifier =Modifier.height(8.dp))

        }
        item {
            Row(modifier = Modifier.padding(8.dp)) {

                TextField(value = summary, onValueChange = {
                    summary = it
                    checkAllCellsFilled()
                },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        item {
            Button(
                onClick = {
                    patientData.doctorId = Firebase.auth.currentUser!!.uid
                    patientData.dateOfAssessment =
                        LocalDate.now().format(DateTimeFormatter.ISO_DATE)
                    patientData.timeOfAssessment =
                        LocalTime.now().format(DateTimeFormatter.ISO_TIME).substring(0, 5)
                    patientData.freezingWithout =
                        if (patientData.totalWithout < 12) "Minor Freezing" else if (patientData.totalWithout in 12..24) "Moderate Freezing" else "Severe Freezing"
                    patientData.freezingWith =
                        if (patientData.totalWith < 12) "Minor Freezing" else if (patientData.totalWith in 12..24) "Moderate Freezing" else "Severe Freezing"
                    patientData.summary =
                        "During  the assessment, it was observed that patient has  ${FogTest.hnyScore[patientData.hnyScore.toDouble()]}.\n FOG test was conducted with and without device and on the basis of that, we conclude that the patient will be ${patientData.eligibility} for the stimulation from the WALK Device.\n\n\n Doctor's Notes:\n $summary"

                    Firebase.firestore.collection("reports").add(patientData)
                        .addOnSuccessListener { navController.navigate("Reports") }

                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .imePadding(), enabled = allCellsFilled
            ) {
                Text("Submit")
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun DefaultPreview1() {
    MyApplicationTheme(true) {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {
            Confirm(rememberNavController())
        }
    }
}
