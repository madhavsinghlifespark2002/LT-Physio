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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavigation(navController)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(navController: NavHostController) {
    var StartDestination = "login"
    if(Firebase.auth.currentUser != null) {
        StartDestination="TestSelection"
    }
    NavHost(navController, startDestination = StartDestination) {
        composable("login") { LoginScreen(onLoginSuccess = { navController.navigate("inputFields") }) }
        composable("inputFields") { InputFields() }
        composable("TestSelection") { TestSelection(navController) }
        composable("FOG Test") { FOG(navController) }
    }
}

@Composable
fun TestSelection(navController: NavHostController) {
    //List of tests
    val tests = listOf("FOG Test");
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text("Select a test")
        tests.forEach {
            Button(onClick = {
                //Navigate to the test
                navController.navigate(it);
            }, modifier = Modifier.padding(8.dp).fillMaxWidth()) {
                Text(it, modifier = Modifier.padding(8.dp))
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
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background

            ) {
//                TestSelection(navController)
//                Keyboard()
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

