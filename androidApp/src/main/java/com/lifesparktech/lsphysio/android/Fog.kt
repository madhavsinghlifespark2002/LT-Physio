package com.lifesparktech.lsphysio.android

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.lifesparktech.lsphysio.FogTest.labels
import com.lifesparktech.lsphysio.PatientData.CurrentPatient.patientData


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FOG(navController: NavHostController) {

    var without by remember { mutableStateOf(List(labels.size) { "" }) }
    var with by remember { mutableStateOf(List(labels.size) { "" }) }
    var summary by remember { mutableStateOf("") }
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
                    navController.navigate("Confirm")
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
fun Confirm()
{
    var summary by remember { mutableStateOf("") }
    var allCellsFilled by remember { mutableStateOf(false) }
    fun checkAllCellsFilled() {
        allCellsFilled = patientData.summary.isNotEmpty()
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
            TextField(value = summary, onValueChange = {
                patientData.summary = it
                summary = it
                checkAllCellsFilled()
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp).height(100.dp)
                , label = { Text("Summary") })
        }
        item {
            Button(
                onClick = {
                }, modifier = Modifier
                    .fillMaxWidth()
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
            Confirm()
        }
    }
}
