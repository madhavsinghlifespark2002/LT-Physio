package com.lifesparktech.lsphysio.android.components

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.lifesparktech.lsphysio.android.models.Patient
import com.lifesparktech.lsphysio.android.pages.AccountScreen
import com.lifesparktech.lsphysio.android.pages.AddPatientScreen
import com.lifesparktech.lsphysio.android.pages.AddedScreen
import com.lifesparktech.lsphysio.android.pages.HomeScreen
import com.lifesparktech.lsphysio.android.pages.PatientDetail
import com.lifesparktech.lsphysio.android.pages.PatientScreen
import com.lifesparktech.lsphysio.android.pages.ProfileScreen
import com.lifesparktech.lsphysio.android.pages.ReportScreen
import com.lifesparktech.lsphysio.android.pages.SettingsScreen

@Composable
fun Material3BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        Screen.Home,
        Screen.Report,
        Screen.Added,
        Screen.Settings,
        Screen.Profile,
    )
    Surface(
        shadowElevation = 8.dp,
        tonalElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface // Use a surface color
    ) {
        NavigationBar(
            containerColor = Color.White,
            tonalElevation = 12.dp,
//            tonalElevation = 80.dp // Add shadow for elevation
        ) {
            val currentRoute = currentRoute(navController)
            items.forEach { screen ->
                NavigationBarItem(
                    icon = {
                        if (screen.icon != null) {
                            Icon(screen.icon, contentDescription = screen.title, modifier = Modifier.size(28.dp))
                        } else {
                            // Use custom drawable icon
                            Icon(painter = painterResource(id = screen.customIcon), contentDescription = screen.title, modifier = Modifier.size(28.dp))
                        }
                    },
                    selected = currentRoute == screen.route,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
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
    object Profile : Screen("profile", "Profile", customIcon = com.example.lsphysio.android.R.drawable.person)
    object Settings : Screen("settings", "Settings", customIcon = com.example.lsphysio.android.R.drawable.settings)
    object Report : Screen("report", "Report", customIcon = com.example.lsphysio.android.R.drawable.report)
    object Added : Screen("added", "Added", customIcon = com.example.lsphysio.android.R.drawable.added)
}
@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = Screen.Home.route, modifier = modifier) {
        composable("addpatientscreen") { AddPatientScreen(navController) }
        composable("accountScreen"){AccountScreen()}
        composable("patientScreen"){PatientScreen(navController)}
        composable("PatientDetail/{patientId}"){ backStackEntry ->
            val PatientId = backStackEntry.arguments?.getString("patientId")
            if (PatientId != null) {
                PatientDetail(navController, patientId = PatientId)
            } else {
                Text(text = "Invalid Patient ID")
            }
            //PatientDetail()
        }
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Profile.route) { ProfileScreen(navController) }
        composable(Screen.Settings.route) { SettingsScreen() }
        composable(Screen.Report.route) { ReportScreen() }
        composable(Screen.Added.route) { AddedScreen() }
    }
}