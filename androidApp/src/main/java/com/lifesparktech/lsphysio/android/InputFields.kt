package com.lifesparktech.lsphysio.android

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lifesparktech.lsphysio.Greeting
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InputFields(onSubmit: () -> Unit) {
    var patientData by remember { mutableStateOf(PatientData()) }
    var expanded by remember { mutableStateOf(false) }
    var allFieldsFilled by remember { mutableStateOf(false) }
    var genderSearch by remember { mutableStateOf("") }
    val genderOptions = listOf("Male", "Female", "Other")
    val currentDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
    var currentTime = LocalTime.now().format(DateTimeFormatter.ISO_TIME).substring(0, 5)

    fun checkAllFieldsFilled() {
        allFieldsFilled = patientData.name.isNotEmpty() &&
                patientData.gender.isNotEmpty() &&
                patientData.age.isNotEmpty() &&
                patientData.primaryDiagnosis.isNotEmpty()
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .imePadding(),
    ) {
        TextField(
            value = patientData.name,
            onValueChange = {
                patientData = patientData.copy(name = it)
                checkAllFieldsFilled()
            },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
//                .fillMaxWidth()
                .clickable { expanded = true }
                .wrapContentSize(Alignment.TopStart)
                .background(MaterialTheme.colorScheme.secondaryContainer)
        )
        {
            Text(
                text = genderSearch.ifEmpty { "Select Gender" },
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            DropdownMenu(

                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                genderOptions.forEach { gender ->
                    DropdownMenuItem(
                        text = { Text(gender) },
                        onClick = {
                            genderSearch =  gender
                            expanded = false
                        }
                    )
                }
            }
            IconButton(
                onClick = { expanded = true },
//                modifier = Modifier.wrapContentSize(Alignment.)
            ) {
                Icon(Icons.Filled.ArrowDropDown, contentDescription = "Expand dropdown")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = patientData.age,
            onValueChange = {
                if (it.all { char -> char.isDigit() }) {
                    patientData = patientData.copy(age = it)
                    checkAllFieldsFilled()
                }
            },
            label = { Text("Age") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = currentDate,
            onValueChange = { },
            label = { Text("Date of Assessment") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            colors = TextFieldDefaults.colors(
                unfocusedLabelColor = Color.Gray,
                unfocusedTextColor = Color.Gray,
                unfocusedContainerColor = Color.LightGray,
                focusedContainerColor = Color.LightGray,
                focusedTextColor = Color.Gray,
            ),
            singleLine = true,

        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = currentTime,
            onValueChange = { },
            label = { Text("Time of Assessment") },
            colors = TextFieldDefaults.colors(
                unfocusedLabelColor = Color.Gray,
                unfocusedTextColor = Color.Gray,
                unfocusedContainerColor = Color.LightGray,
                focusedContainerColor = Color.LightGray,
                focusedTextColor = Color.Gray,

            ),
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = patientData.primaryDiagnosis,
            onValueChange = {
                patientData = patientData.copy(primaryDiagnosis = it)
                checkAllFieldsFilled()
            },
            label = { Text("Primary Diagnosis") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Handle form submission
                val db = Firebase.firestore
                val assessment = hashMapOf(
                    "Name" to patientData.name,
                    "gender" to patientData.gender,
                    "age" to patientData.age,
                    "date" to currentDate,
                    "time" to currentTime,
                    "primary" to patientData.primaryDiagnosis,
                    "clinic" to Firebase.auth.currentUser?.uid.toString()
                )

                db.collection("reports")
                    .add(assessment)
                    .addOnSuccessListener { documentReference ->
                        println("DocumentSnapshot added with ID: ${documentReference.id}")
                        Greeting.name = documentReference.id
                        onSubmit()
                    }
                    .addOnFailureListener { e ->
                        println("Error adding document: $e")
                    }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = allFieldsFilled
        ) {
            Text("Submit")
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun DefaultPreview2() {
    MyApplicationTheme(true) {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {
            InputFields {

            }
//                Keyboard()
        }
    }
}