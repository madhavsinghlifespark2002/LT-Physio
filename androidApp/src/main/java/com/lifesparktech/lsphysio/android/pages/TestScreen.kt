package com.lifesparktech.lsphysio.android.pages
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.lsphysio.android.R
import com.lifesparktech.lsphysio.PeripheralManager
import com.lifesparktech.lsphysio.PeripheralManager.gameContext
import com.lifesparktech.lsphysio.PeripheralManager.mainScope
import kotlinx.coroutines.launch

@Composable
fun TestScreen(navController: NavController) {
    var isDeviceConnected by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (PeripheralManager.peripheral != null) {
            isDeviceConnected = true
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFf4f4f4))
    ) {
        item{
            Text(
                text = "Tests",
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(16.dp)
            )
        }
        item{
            TestCard(
                R.drawable.testimg,
                "TUG",
                "Helps in reducing pain",
                isDeviceConnected,
                "tugscreen",
                navController
            )
        }
        item{
            TestCard(
                R.drawable.testimg,
                "SWAY",
                "Helps in reducing pain",
                isDeviceConnected,
                "tug",
                navController
            )
        }
        item{
            TestCard(
                R.drawable.testimg,
                "SIT TO STAND",
                "Helps in reducing pain",
                isDeviceConnected,
                "SittoStandScreen",
                navController
            )
        }
        item{
            TestCard(
                R.drawable.testimg,
                "STEP LENGTH",
                "Helps in reducing pain",
                isDeviceConnected,
                "tug",
                navController
            )
        }
        item{
            TestCard(
                R.drawable.testimg,
                "FOG",
                "Helps in reducing pain",
                isDeviceConnected,
                "tug",
                navController
            )
        }
    }
}

@Composable
fun TestCard(
    gameImage: Int,
    testName: String,
    helpFeature: String,
    isDeviceConnected: Boolean,
    testpage: String = "tug",
    navController: NavController
) {
    val context = LocalContext.current
    gameContext = context
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White // Set the card's background color
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = gameImage),
                contentDescription = "$testName Image",
                modifier = Modifier
                    .size(150.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color.Black
                            )
                        ) {
                            append("$testName\n\n")
                        }
                        append("$helpFeature\n")
                    },
                    modifier = Modifier.padding(8.dp),
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        navController.navigate("$testpage")
                    },
                    modifier = Modifier.align(Alignment.End),
                    enabled = isDeviceConnected,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = Color(0xFF005749), // Normal state color
                        contentColor = Color.White, // Normal text color
                        disabledContainerColor = Color(0xFFCCCCCC), // Background color when disabled
                        disabledContentColor = Color.Gray // Text color when disabled
                    )
                ) {
                    Text(text = "Start", color = Color.White)
                }
            }
        }
    }
}
