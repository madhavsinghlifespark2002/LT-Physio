package com.example.lsphysio.android

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

// InputFields.kt
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InputFields() {
    var assessmentData by remember { mutableStateOf(AssessmentData()) }
    var expanded by remember { mutableStateOf(false) }
    val genderOptions = listOf("Male", "Female", "Other")

    val currentDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
    var currentTime = LocalTime.now().format(DateTimeFormatter.ISO_TIME).substring(0, 5)

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .imePadding(),
    ) {
        TextField(
            value = assessmentData.name,
            onValueChange = { assessmentData = assessmentData.copy(name = it) },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .wrapContentSize(Alignment.TopStart)
        ) {
            Text(
                text = assessmentData.gender.ifEmpty { "Select Gender" },
                modifier = Modifier.padding(16.dp),
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                genderOptions.forEach { gender ->
                    DropdownMenuItem(
                        text = { Text(gender) },
                        onClick = {
                            assessmentData = assessmentData.copy(gender = gender)
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = assessmentData.age,
            onValueChange = {
                if (it.all { char -> char.isDigit() }) {
                    assessmentData = assessmentData.copy(age = it)
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
            singleLine = true,
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = currentTime,
            onValueChange = { },
            label = { Text("Time of Assessment") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = assessmentData.primaryDiagnosis,
            onValueChange = { assessmentData = assessmentData.copy(primaryDiagnosis = it) },
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
                    "name" to assessmentData.name,
                    "gender" to assessmentData.gender,
                    "age" to assessmentData.age,
                    "dateOfAssessment" to currentDate,
                    "timeOfAssessment" to currentTime,
                    "primaryDiagnosis" to assessmentData.primaryDiagnosis
                )
                db.collection(Firebase.auth.currentUser?.email.toString())
                    .add(assessment)
                    .addOnSuccessListener { documentReference ->
                        println("DocumentSnapshot added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        println("Error adding document: $e")
                    }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }
    }
}