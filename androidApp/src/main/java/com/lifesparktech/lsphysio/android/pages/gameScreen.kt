package com.lifesparktech.lsphysio.android.pages
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.lifesparktech.lsphysio.R
@Composable
fun GamesScreen() {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFf4f4f4)),
    ) {

        Text(text ="GamesScreen")
        GameCard(com.example.lsphysio.android.R.drawable.fishgame1, "CatchFish", "Helps in reducing pain", 65)
        GameCard(com.example.lsphysio.android.R.drawable.footballgame, "Football", "Helps in reducing pain", 73)
        GameCard(com.example.lsphysio.android.R.drawable.swinggame, "Swing", "Helps in reducing pain", 32)
    }
}
@Composable
fun GameCard(
    gameImage: Int,
    gameName: String,
    helpFeature: String,
    highestScore: Int
) {
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

            // Game details on the right
            Column(
                modifier = Modifier.fillMaxWidth()
            )
            {
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

            }
        }
    }
}