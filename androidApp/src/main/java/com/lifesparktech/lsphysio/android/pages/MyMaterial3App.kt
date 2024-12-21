package com.lifesparktech.lsphysio.android.pages

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.lsphysio.android.R
import com.lifesparktech.lsphysio.android.NavigationItem
import com.lifesparktech.lsphysio.android.components.Material3BottomNavigationBar
import com.lifesparktech.lsphysio.android.components.NavigationHost
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMaterial3App() {
    val navController = rememberNavController()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    // TODO for Refactor
    val SideItems = listOf(
        NavigationItem(
            title = "Dashboard",
            selectedIcon = painterResource(id = R.drawable.dashboard),
            route = "home"
        ),
        NavigationItem(
            title = "Your Account",
            selectedIcon = painterResource(id = R.drawable.account),
            route = "accountScreen"
        ),
        NavigationItem(
            title = "Doctor",
            selectedIcon = painterResource(id = R.drawable.doctors),
            route = "doctorScreen"
        ),
        NavigationItem(
            title = "Patient",
            selectedIcon = painterResource(id = R.drawable.patients),
            route = "patientScreen"
        ),
        NavigationItem(
            title = "Departments",
            selectedIcon = painterResource(id = R.drawable.department),
            route = "departmentScreen"
        ),
        NavigationItem(
            title = "Schedule",
            selectedIcon = painterResource(id = R.drawable.schedule),
            route = "scheduleScreen"
        ),
        NavigationItem(
            title = "Appointment",
            selectedIcon = painterResource(id = R.drawable.appointment),
            route = "appointmentScreen"
        ),
        NavigationItem(
            title = "Report",
            selectedIcon = painterResource(id = R.drawable.reports),
            route = "reportsScreen"
        ),
        NavigationItem(
            title = "Resources",
            selectedIcon = painterResource(id = R.drawable.resources),
            route = "resourceScreen"
        ),
        NavigationItem(
            title = "Receipts",
            selectedIcon = painterResource(id = R.drawable.payment),
            route = "receiptScreen"
        ),
    )
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White
            ) {
                Row(
                    modifier = Modifier.padding(12.dp)
                ){
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "logo",
                        modifier = Modifier
                            .width(200.dp)
                            .height(50.dp)
                    )
                }
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    color = Color(0xFFD6D6D6),
                    thickness = 1.dp
                )
                Spacer(modifier = Modifier.height(16.dp))
                SideItems.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            )
                            {
                                Text(text = item.title)
                            }

                        },
                        selected = index == selectedItemIndex,
                        onClick = {
                            selectedItemIndex = index
                            scope.launch {
                                drawerState.close()
                            }
                            navController.navigate(item.route)
                        },
                        icon = {
//                            Icon(
//                                imageVector =
//                                    item.selectedIcon,
//                                contentDescriptionntDescription = item.title
//                            )
                            Image(
                                painter = item.selectedIcon,
                                contentDescription = "${item.title}",
                                modifier = Modifier
                                    .width(26.dp)
                            )
                        },
                        modifier = Modifier
                            .padding(NavigationDrawerItemDefaults.ItemPadding),
                        shape = RoundedCornerShape(12.dp),
                        colors =
                            NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = Color(0xFFD6E7EE), // Background color for selected item
                                unselectedContainerColor = Color.Transparent, // Background color for unselected item
                                selectedTextColor = Color(0xFF222429), // Text color for selected item
                                // unselectedTextColor = Color.Gray, // Text color for unselected item
                                selectedIconColor = Color(0xFF222429), // Icon color for selected item
                                unselectedIconColor = Color(0xFF222429) // Icon color for unselected item
                            )
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    color = Color(0xFFD6D6D6),
                    thickness = 1.dp
                )
                NavigationDrawerItem(
                    label = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Logout")
                        }
                    },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    icon = {
                        Image(
                            painter = painterResource(id = R.drawable.logout),
                            contentDescription = "Logout",
                            modifier = Modifier
                                .width(26.dp)
                        )
                    },
                    modifier = Modifier
                        .padding(NavigationDrawerItemDefaults.ItemPadding),
                    shape = RoundedCornerShape(12.dp), // Adds rounded corners
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color(0xFFD6E7EE), // Optional: Customize colors
                        unselectedContainerColor = Color.Transparent,
                        selectedTextColor = Color(0xFF222429),
                        unselectedIconColor = Color(0xFF222429)
                    )
                )
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.padding(12.dp).fillMaxWidth().height(120.dp)
                        ){
                            OutlinedTextField(
                                value = "",
                                onValueChange = {  },
                                modifier = Modifier
                                    .focusRequester(focusRequester)
                                    .fillMaxWidth(0.5f)
                                    .onFocusChanged { focusState -> },
                                textStyle = androidx.compose.material3.LocalTextStyle.current.copy(color = Color.Black),
                                singleLine = true,
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search", modifier = Modifier.size(20.dp))
                                },
                                colors = TextFieldDefaults.colors(
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                ),
                                placeholder = {
                                    Text(text = "Search")
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)  // Circle size
                                        .clip(CircleShape)  // Makes the icon circular
                                        .background(Color(0xFFD6E7EE))  // Set background color from list
                                        .padding(8.dp),  // Padding inside the circle
                                    contentAlignment = Alignment.Center  // Center the text inside the circle
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.notification),
                                        contentDescription = "logo",
                                        modifier = Modifier
                                            .width(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("John Peters", fontSize = 14.sp, modifier = Modifier.clickable{navController.navigate("accountScreen")})
                                Spacer(modifier = Modifier.width(12.dp))
                                Image(
                                    painter = painterResource(id = R.drawable.profile_image),
                                    contentDescription = "logo",
                                    modifier = Modifier
                                        .width(32.dp).clip(CircleShape).clickable{navController.navigate("accountScreen")}
                                )
                            }

                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )
            },
            bottomBar = { Material3BottomNavigationBar(navController) }
        ) { innerPadding ->
            NavigationHost(navController = navController, modifier = Modifier.padding(innerPadding))
        }
    }
}
