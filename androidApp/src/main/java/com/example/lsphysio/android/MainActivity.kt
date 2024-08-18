package com.example.lsphysio.android

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lsphysio.getPlatform
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.functions.functions

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavigation(navController)
                }
            }
        }
    }

    fun setName(name: String) {
        getPlatform().name = name
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(navController: NavHostController) {
    var StartDestination = "login"
    if (Firebase.auth.currentUser != null) {
        StartDestination = "Home"
    }
    NavHost(navController, startDestination = StartDestination) {
        composable("login") { LoginScreen(onLoginSuccess = { navController.navigate("Home") }) }
        composable("inputFields") { InputFields(onSubmit = { navController.navigate("TestSelection") }) }
        composable("Home") { Home(navController) }
        composable("TestSelection") { TestSelection(navController) }
        composable("Reports") { Reports(navController) }
        composable("FOG Test") { FOG() }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(navController: NavHostController) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text("Home")
        Button(onClick = {
            navController.navigate("inputFields")
        }, modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()) {
            Text("New Test", modifier = Modifier.padding(8.dp))
        }
        Button(onClick = {
            navController.navigate("Reports")
        }, modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()) {
            Text("Reports", modifier = Modifier.padding(8.dp))
        }
    }
}

@Composable
fun TestSelection(navController: NavHostController) {
    //List of tests
    val tests = listOf("FOG Test")
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text("Select a test")
        tests.forEach {
            Button(onClick = {
                //Navigate to the test
                navController.navigate(it)
            }, modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()) {
                Text(it, modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun Reports(navController: NavHostController) {
    var reports by remember { mutableStateOf<List<DocumentSnapshot>>(emptyList()) }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).get()
            .addOnSuccessListener { querySnapshot ->
                reports = querySnapshot.documents
            }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text("Reports")
        reports.forEach { report ->
            Button(onClick = {
                Firebase.functions.getHttpsCallable("getReport").call(report.id)
                // Navigate to the test
                // navController.navigate("Report")
            }, modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()) {
                Text(report.data?.get("name").toString()+ " "+report.data?.get("date").toString(), modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme(true) {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {
            Reports(navController = rememberNavController())
//                Keyboard()
        }
    }
}

data class PatientData(
    var name: String = "",
    var gender: String = "",
    var age: String = "",
    var dateOfAssessment: String = "",
    var timeOfAssessment: String = "",
    var primaryDiagnosis: String = ""
)

