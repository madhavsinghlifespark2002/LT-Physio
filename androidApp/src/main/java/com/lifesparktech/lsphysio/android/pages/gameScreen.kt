package com.lifesparktech.lsphysio.android.pages

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.core.os.postDelayed
import com.example.lsphysio.android.R
import com.unity3d.player.UnityPlayer
import com.unity3d.player.UnityPlayerActivity
import kotlin.jvm.java

@Composable
fun GamesScreen() {
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
            "FishingGame"
        )
        GameCard(
            R.drawable.footballgame,
            "Football",
            "Helps in reducing pain",
            73,
            "SceneController",
            "BallGame"
        )
        GameCard(
            R.drawable.swinggame,
            "Swing",
            "Helps in reducing pain",
            32,
            "SceneController",
            "SwingGame"
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
    unityMethod: String
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

                // Play button to launch Unity and send the message
                Button(
                    onClick = {
                        launchUnity(context, unityObject, unityMethod)
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Play $gameName")
                }
            }
        }
    }
}

fun launchUnity(context: Context, unityObject: String, unityMethod: String) {
    // Launch Unity activity
    val intent = Intent(context, UnityPlayerActivity::class.java)
    context.startActivity(intent)
    Handler(Looper.getMainLooper()).postDelayed({
        UnityPlayer.UnitySendMessage(unityObject, unityMethod, "")
    }, 1000)
    // Delay the message slightly to ensure UnityPlayer is ready
   // UnityPlayer.UnitySendMessage(unityObject, unityMethod, "")
}
