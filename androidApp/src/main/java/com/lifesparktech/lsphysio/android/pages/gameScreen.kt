package com.lifesparktech.lsphysio.android.pages
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.example.lsphysio.android.R
import com.lifesparktech.lsphysio.PeripheralManager
import com.lifesparktech.lsphysio.PeripheralManager.mainScope
import com.lifesparktech.lsphysio.android.components.readCommand
import com.unity3d.player.UnityPlayer
import com.unity3d.player.UnityPlayerActivity
import kotlinx.coroutines.launch
import kotlin.jvm.java
@Composable
fun GamesScreen() {
    var isDeviceConnected by remember { mutableStateOf(false) }
    LaunchedEffect(Unit){
        if (PeripheralManager.peripheral !=null){
            isDeviceConnected = true
        }
    }
    BackHandler {
        println("back button pressed.")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFf4f4f4))
    ) {
        Text(
            text = "GamesScreen",
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(16.dp)
        )
        GameCard(
            R.drawable.fishgame1,
            "CatchFish",
            "Helps in reducing pain",
            65,
            "SceneController",
            "FishingGame",
            isDeviceConnected
        )
        GameCard(
            R.drawable.footballgame,
            "Football",
            "Helps in reducing pain",
            73,
            "SceneController",
            "BallGame",
            isDeviceConnected
        )
        GameCard(
            R.drawable.swinggame,
            "Swing",
            "Helps in reducing pain",
            32,
            "SceneController",
            "SwingGame",
            isDeviceConnected
        )
    }
}

@Composable
fun GameCard(
    gameImage: Int,
    gameName: String,
    helpFeature: String,
    highestScore: Int,
    unityObject: String,
    unityMethod: String,
    isDeviceConnected: Boolean
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Game image on the left
            Image(
                painter = painterResource(id = gameImage),
                contentDescription = "$gameName Image",
                modifier = Modifier
                    .size(150.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Crop
            )

            // Game details and Play button
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
                            append("$gameName\n\n")
                        }
                        append("Helps Patients: $helpFeature\n")
                        append("Highest Score: $highestScore")
                    },
                    modifier = Modifier.padding(8.dp),
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        mainScope.launch{
                            launchUnity(context, unityObject, unityMethod)
                        }
                    },
                    modifier = Modifier.align(Alignment.End),
                    enabled = isDeviceConnected
                ) {
                    Text(text = "Play $gameName")
                }
            }
        }
    }
}
suspend fun launchUnity(context: Context, unityObject: String, unityMethod: String) {
    val intent = Intent(context, UnityPlayerActivity::class.java)
    context.startActivity(intent)
    Handler(Looper.getMainLooper()).postDelayed({
        UnityPlayer.UnitySendMessage(unityObject, unityMethod, "")
    }, 1000)
    readCommand(unityObject,unityMethod)
}
