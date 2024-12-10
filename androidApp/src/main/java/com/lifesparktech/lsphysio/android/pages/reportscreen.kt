package com.lifesparktech.lsphysio.android.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ListItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.firestore.admin.v1.Index
import com.lifesparktech.lsphysio.android.data.Patient
import com.lifesparktech.lsphysio.android.data.samplePatients

@Composable
fun ReportScreen() {
    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF4F4F4)) {
        MainScreen()
    }
}

@Composable
fun MainScreen() {
    PatientList(patients = samplePatients)
}

@Composable
fun PatientList(patients: List<Patient>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
            ) {
                Text(text = "No.")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                   // horizontalArrangement = Arrangement.SpaceAround
                ) {
                   // Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Name", modifier = Modifier.padding(start = 16.dp))
                  //  Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Age", modifier = Modifier.padding(start = 24.dp))
                   // Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "DateOfBirth}", modifier = Modifier.padding(start = 24.dp))
                   // Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Status", modifier = Modifier.padding(start = 24.dp))
                   // Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Email", modifier = Modifier.padding(start = 24.dp))
                   // Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Phone",modifier = Modifier.padding(start = 24.dp))
                }
            }
        }
        itemsIndexed(patients){ index, patient ->
            PatientListTile(patient = patient, index = index + 1)
            // patients.forEach{ index, patient -> PatientListTile(patient)}
        }
    }
}

@Composable
fun PatientListTile(patient: Patient, index: Int) {
    ListItem(
        headlineContent = {},
        supportingContent = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = patient.name)
                Text(text = "${patient.age}")
                Text(text = "${patient.dateOfBirth}")
                Text(text = "${patient.status}")
                Text(text = "${patient.email}")
                Text(text = "${patient.phone}")
            }
        },
        leadingContent = {
            Text(text = "$index. ")

        },
        colors = ListItemDefaults.colors(
            containerColor = Color.White
        ),
        shadowElevation = 12.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

@Preview
@Composable
fun PreviewReportScreen() {
    ReportScreen()
}
