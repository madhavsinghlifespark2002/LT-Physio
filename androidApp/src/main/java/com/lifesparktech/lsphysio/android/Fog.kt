package com.lifesparktech.lsphysio.android

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import com.lifesparktech.lsphysio.Greeting
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FOG(navController: NavHostController) {
    val labels = listOf(
        "Sit to Stand Transition",
        "4m straight walk",
        "360 degree turn clockwise",
        "540 degree turn anticlockwise",
        "2 rounds in cluttered maze",
        "Passing through door",
        "(Dual task) Passing through door",
        "(Dual task) 2 rounds in cluttered maze",
        "(Dual task) 540 degree turn clockwise",
        "(Dual task) 360 degree turn anti clockwise",
        "(Dual task) 4m straight walk",
        "(Dual task) Stand to Sit Transition"
    )
    var without by remember { mutableStateOf(List(labels.size) { "" }) }
    var with by remember { mutableStateOf(List(labels.size) { "" }) }
    var allCellsFilled by remember { mutableStateOf(false) }
    var summary by remember { mutableStateOf("") }

    // Function to check if all cells are filled
    fun checkAllCellsFilled() {
        allCellsFilled = without.all { it.isNotEmpty() } && with.all { it.isNotEmpty() } && summary.isNotEmpty()
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
                    text = label, modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )
                TextField(
                    value = without[index],
                    onValueChange = { newValue ->
                        // Update the `without` list
                        if(newValue.isDigitsOnly()&&newValue.isNotEmpty()) {
                            if (newValue.toInt()  <5) {
                                without = without.toMutableList().apply { this[index] = newValue }
                                checkAllCellsFilled()
                            }
                        }
                        if(newValue.isEmpty()) {
                            without = without.toMutableList().apply { this[index] = newValue }
                            checkAllCellsFilled()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    label = { Text("Without") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                    )
                )
                TextField(
                    value = with[index],
                    onValueChange = { newValue ->
                        // Update the `with` list
                        if(newValue.isDigitsOnly()&&newValue.isNotEmpty()) {
                            if (newValue.toInt()  <5) {

                                with = with.toMutableList().apply { this[index] = newValue }
                                checkAllCellsFilled()
                            }
                        }
                        if(newValue.isEmpty()) {
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
            TextField(
                value = summary,
                onValueChange = { summary = it
                    checkAllCellsFilled()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Summary") }
            )
        }
        item {
            Button(
                onClick = {
                    // Handle form submission
                    val db = Firebase.firestore
                    // Hash map of [label, without, with]
                    val patientData = hashMapOf<String, Any>()
                    var counter=0;
                    var total_without=0;
                    var total_with=0;
                    for (i in labels.indices) {
                        patientData[i.toString()] = hashMapOf(
                            "without" to without[i], "with" to with[i]
                        )
                        if(without[i].isNotEmpty()&&with[i].isNotEmpty()){
                            total_without+=without[i].toInt()
                            total_with+=with[i].toInt()
                        }
                        counter++;
                    }

                    patientData["total_without"]=total_without
                        patientData["total_with"]=total_with
                        patientData["summary"] = summary
                    db.collection("reports").document(Greeting.name)
                        .update(patientData)
                        .addOnSuccessListener {
                            navController.navigate("Home")
                        }.addOnFailureListener { e ->
                            println("Error adding document: $e")
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding(),
                enabled = allCellsFilled
            ) {
                Text("Submit")
            }
        }
    }
}

@Preview
@Composable
fun DefaultPreview1() {
    MyApplicationTheme(false) {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {
//            FOG()
        }
    }
}
