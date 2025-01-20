package com.lifesparktech.lsphysio.android.pages
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun sampleTest(navController: NavController, sharedViewModel: SharedViewModel) {
    val selectedOption by sharedViewModel.selectedOption // Observe the shared state
    println("initial value : $selectedOption")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Screen 1: Select an Option",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Options
        (0..2).forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        sharedViewModel.selectedOption.value = option
                    }
            ) {
                RadioButton(
                    selected = selectedOption == option,
                    onClick = {
                        sharedViewModel.selectedOption.value = option
                        println("This is selectedOption.value: ${sharedViewModel.selectedOption.value}")
                    },
                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF005749))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Option $option",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                navController.navigate("sampleTest2")
            },
            colors = ButtonDefaults.textButtonColors(
                containerColor = Color(0xFF005749),
                contentColor = Color.White
            )
        ) {
            Text("Next", color = Color.White)
        }
    }
}
@Composable
fun sampleTest2(navController: NavController, sharedViewModel: SharedViewModel) {
    val selectedOption by sharedViewModel.selectedOption // Observe shared state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Screen 2: Modify Selection",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Options
        (0..2).forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        sharedViewModel.selectedOption.value = option // Update shared state
                    }
            ) {
                RadioButton(
                    selected = selectedOption == option,
                    onClick = {
                        sharedViewModel.selectedOption.value = option
                        println("This is selectedOption.value: ${sharedViewModel.selectedOption.value}")
                    },
                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF005749))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Option $option",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                sharedViewModel.selectedOption.value = selectedOption
                navController.popBackStack() // Navigate back to Screen 1
            },
            colors = ButtonDefaults.textButtonColors(
                containerColor = Color(0xFF005749),
                contentColor = Color.White
            )
        ) {
            Text("Submit and Go Back", color = Color.White)
        }
    }
}
class SharedViewModel : ViewModel() {
    var selectedOption = mutableStateOf(2)
}