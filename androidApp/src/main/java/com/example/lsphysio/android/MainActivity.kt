package com.example.lsphysio.android

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
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
import com.google.firebase.storage.storage
import java.io.File

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
        composable("FOG Test") { FOG(navController) }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(navController: NavHostController) {
    BackHandler(true) {

    }
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
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("reports").whereEqualTo("clinic", Firebase.auth.currentUser?.uid).get()
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
                report.data?.let {
                    var file=Firebase.storage.getReferenceFromUrl("gs://lsphysio-f00ba.appspot.com/reports/${report.id}.pdf")

                    val localFile = File.createTempFile("report", ".pdf")
                    file.getFile(localFile).addOnSuccessListener {
                        //show the pdf
                        println(localFile.path)
                        val uri: Uri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.provider",
                            localFile
                        )
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(uri, "application/pdf")
                            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                        }
                        context.startActivity(intent)
//                        navController.navigate("pdfView")
                    }.addOnFailureListener {
                    }

                }
            }, modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()) {
                Text(report.data?.get("Name").toString()+ " "+report.data?.get("date").toString(), modifier = Modifier.padding(8.dp))
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

