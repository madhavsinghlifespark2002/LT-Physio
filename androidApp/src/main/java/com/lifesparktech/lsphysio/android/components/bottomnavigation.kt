package com.lifesparktech.lsphysio.android.components
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.lifesparktech.lsphysio.PeripheralManager
import com.lifesparktech.lsphysio.android.pages.AccountScreen
import com.lifesparktech.lsphysio.android.pages.AddPatientScreen
import com.lifesparktech.lsphysio.android.pages.AppointmentScreen
import com.lifesparktech.lsphysio.android.pages.DepartmentScreen
import com.lifesparktech.lsphysio.android.pages.DeviceConnectionScreen
import com.lifesparktech.lsphysio.android.pages.DeviceControlScreen
import com.lifesparktech.lsphysio.android.pages.DoctorScreen
import com.lifesparktech.lsphysio.android.pages.GamesScreen
import com.lifesparktech.lsphysio.android.pages.HomeScreen
import com.lifesparktech.lsphysio.android.pages.PatientDetail
import com.lifesparktech.lsphysio.android.pages.PatientModifiedScreen
import com.lifesparktech.lsphysio.android.pages.PatientScreen
import com.lifesparktech.lsphysio.android.pages.ReceiptScreen
import com.lifesparktech.lsphysio.android.pages.ReportsScreen
import com.lifesparktech.lsphysio.android.pages.ResourceScreen
import com.lifesparktech.lsphysio.android.pages.ScheduleScreen
import com.lifesparktech.lsphysio.android.pages.TestScreen
import com.lifesparktech.lsphysio.android.pages.UpdatedPatientScreen

@Composable
fun Material3BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        Screen.Home,
        Screen.Games,
        Screen.Tests,
        Screen.DeviceConnectionScreen
    )
    Surface(
        shadowElevation = 8.dp,
        tonalElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface // Use a surface color
    ) {
        NavigationBar(
            containerColor = Color.White,
            tonalElevation = 12.dp,
        ) {
            val currentRoute = currentRoute(navController)
            items.forEach { screen ->
                NavigationBarItem(
                    icon = {
                        if (screen.icon != null) {
                            Icon(screen.icon, contentDescription = screen.title, modifier = Modifier.size(28.dp))
                        } else {
                            Icon(painter = painterResource(id = screen.customIcon), contentDescription = screen.title, modifier = Modifier.size(28.dp))
                        }
                    },
                    selected = currentRoute == screen.route,
                    onClick = {
                        navController.navigate(screen.route) {
                            if (screen.route == Screen.Home.route){
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                            else{
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF43958F),
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = Color(0xFF181719),
                    ),
                    interactionSource = remember { MutableInteractionSource() }, // Disable hover effect
                )
            }
        }
    }
}
sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null, val customIcon: Int = 0) {
    object Home : Screen("home", "Home", customIcon = com.example.lsphysio.android.R.drawable.house)
    object Games : Screen("Games", "Games", customIcon = com.example.lsphysio.android.R.drawable.games)
    object Tests : Screen("Tests", "Tests", customIcon = com.example.lsphysio.android.R.drawable.testss)
    object DeviceConnectionScreen : Screen("DeviceConnectionScreen", "DeviceConnectionScreen", customIcon = com.example.lsphysio.android.R.drawable.device_control)
}
@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = Screen.Home.route, modifier = modifier) {
        composable("addpatientscreen") { AddPatientScreen(navController) }
        composable("accountScreen"){AccountScreen()}
        composable("patientmodifiedScreen"){PatientModifiedScreen()}
        composable("patientScreen"){PatientScreen(navController)}
        composable("doctorScreen"){DoctorScreen(navController)}
        composable("departmentScreen"){DepartmentScreen(navController)}
        composable("scheduleScreen"){ScheduleScreen(navController)}
        composable("appointmentScreen"){AppointmentScreen(navController)}
        composable("reportsScreen"){ ReportsScreen(navController) }
        composable("resourceScreen"){ ResourceScreen(navController) }
        composable("receiptScreen"){ ReceiptScreen(navController) }
        composable("DeviceControlScreen") { DeviceControlScreen(navController) }
        composable("PatientDetail/{patientId}"){ backStackEntry ->
            val PatientId = backStackEntry.arguments?.getString("patientId")
            if (PatientId != null) {
                PatientDetail(navController, patientId = PatientId)
            } else {
                Text(text = "Invalid Patient ID")
            }
        }
        composable("updatedPatientScreen/{patientId}"){ backStackEntry ->
            val PatientId = backStackEntry.arguments?.getString("patientId")
            if (PatientId != null) {
                UpdatedPatientScreen(navController, patientId = PatientId)
            } else {
                Text(text = "Invalid Patient ID")
            }
        }
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Games.route) { GamesScreen() }
        composable(Screen.Tests.route) { TestScreen() }
        composable(Screen.DeviceConnectionScreen.route) {
            if (PeripheralManager.peripheral != null) {
                // Peripheral is already connected, navigate to DeviceControlScreen
                LaunchedEffect(Unit) {
                    navController.navigate("DeviceControlScreen") {
                        popUpTo(Screen.DeviceConnectionScreen.route) { inclusive = true }
                    }
                }
            } else {
                // Render the DeviceConnectionScreen
                DeviceConnectionScreen(navController = navController)
            }
        }
    }
}