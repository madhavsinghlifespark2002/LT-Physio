package com.lifesparktech.lsphysio.android
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.composable
import com.lifesparktech.lsphysio.android.components.Material3BottomNavigationBar
import com.lifesparktech.lsphysio.android.components.NavigationHost
import com.lifesparktech.lsphysio.android.pages.AddedScreen
import com.lifesparktech.lsphysio.android.pages.HomeScreen
import com.lifesparktech.lsphysio.android.pages.ProfileScreen
import com.lifesparktech.lsphysio.android.pages.ReportScreen
import com.lifesparktech.lsphysio.android.pages.SettingsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyMaterial3App()
        }
    }
}

@Composable
fun MyMaterial3App() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { Material3BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavigationHost(navController = navController, modifier = Modifier.padding(innerPadding))
    }
}



@Preview(showBackground = true)
@Composable
fun Material3AppPreview() {
    MyMaterial3App()
}
