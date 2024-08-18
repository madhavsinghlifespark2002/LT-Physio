package com.example.lsphysio.android

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                    
                ) {
                    InputFields()
                }
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InputFields() {
    var assessmentData by remember { mutableStateOf(AssessmentData()) }
    var expanded by remember { mutableStateOf(false) }
    val genderOptions = listOf("Male", "Female", "Other")

    val currentDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
    val currentTime = LocalTime.now().format(DateTimeFormatter.ISO_TIME)

    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize(),
    ) {
        TextField(

            value = assessmentData.name,
            onValueChange = { assessmentData = assessmentData.copy(name = it) },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
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
                color = Color.Black
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = currentDate,
            onValueChange = { },
            label = { Text("Date of Assessment") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
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
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                // Handle form submission
//                val db=Firebase.firestore;
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit", color = MaterialTheme.colorScheme.tertiary)
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background

        ) {
            InputFields()
        }
    }
}
data class AssessmentData(
    var name: String = "",
    var gender: String = "",
    var age: String = "",
    var dateOfAssessment: String = "",
    var timeOfAssessment: String = "",
    var primaryDiagnosis: String = ""
)
