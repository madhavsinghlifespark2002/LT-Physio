package com.lifesparktech.lsphysio.android.pages

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lsphysio.android.R
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.lifesparktech.lsphysio.android.components.BarChartCard
import com.lifesparktech.lsphysio.android.components.CalendarApp
@Composable
fun HomeScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFF4F4F4) // Use `containerColor` for the background
    ) { innerPadding ->
        LazyColumn{
            item{
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding),
                    mainAxisSpacing = 8.dp,
                    crossAxisSpacing = 8.dp,
                    mainAxisAlignment = MainAxisAlignment.Center
                ) {
                    DashboardCard(
                        label = "Visitors",
                        subContent = "4,592",
                        sideContent = "↑ +15.9%",
                        content = "Stay informed with real-time data to enhance \n patient care and visitor management.",
                        res = R.drawable.family,
                        backgroundColor = Color(0xFFDFF3FE),
                        //backgroundColor = Color.White
                    )
                    DashboardCard(
                        label = "physiotherapist",
                        subContent = "42",
                        sideContent = "↑ +4.9%",
                        content = "Stay updated with essential details\n to streamline medical support and management.",
                        res = R.drawable.doctors,
                        backgroundColor = Color(0xFFEBE1FE),
//                        backgroundColor = Color.White
                    )
                    DashboardCard(
                        label = "Patient",
                        subContent = "540",
                        sideContent = "↓ -0.9%",
                        content = "Keep track of patient information at a glance, \n with easy access to key details for personalized care.",
                        res = R.drawable.patients,
                        backgroundColor = Color(0xFFFFF0D1),
//                        backgroundColor = Color.White
                    )
                    DashboardCard(
                        label = "Total Sessions",
                        subContent = "540",
                        sideContent = "↓ -0.9%",
                        content = "Keep track of patient information at a glance, \n with easy access to key details for personalized care.",
                        res = R.drawable.appointment,
                        backgroundColor = Color(0xFFFFF8F4)
                       // backgroundColor = Color.White
                    )
                }
            }
            item{
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                ){
                    Card(
                        modifier = Modifier.padding(12.dp).height(275.dp).fillMaxWidth(0.6f),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White // Set the card's background color
                        )
                    ){
                        BarChartCard()
                    }
                    Card(
                        modifier = Modifier.padding(12.dp).height(275.dp).fillMaxWidth(0.9f),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White // Set the card's background color
                        )
                    ){
                        CalendarApp()
                    }
                }
            }
            item{
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Card(
                        modifier = Modifier.padding(12.dp).height(300.dp).fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White // Set the card's background color
                        )
                    ){

                    }
                }
            }
        }
    }
}

@Composable
fun DashboardCard(label: String, subContent: String, sideContent: String, content: String, @DrawableRes res: Int, backgroundColor: Color){
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    println("screenWidth $screenWidth")
    Card(
        modifier = Modifier
            .width(if(screenWidth <= 800.0.dp ){ 375.dp } else{ 300.dp})
            //.width(300.dp)
            .height(200.dp)
            .padding(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor // Set the card's background color
        ),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp).fillMaxSize()
        ){
            Row(
                // modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)  // Circle size
                        .clip(CircleShape)  // Makes the icon circular
                        .background(Color.White)  // Set background color from list
                        .padding(8.dp),  // Padding inside the circle
                    contentAlignment = Alignment.Center  // Center the text inside the circle
                ) {
                    Image(
                        painter = painterResource(id = res),
                        contentDescription = "logo",
                        modifier = Modifier
                            .size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "${label}",
                    //fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = Color(0xFF222429)

                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                 modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                //horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${subContent}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 28.sp,
                    color = Color(0xFF222429)

                )
                Spacer(modifier = Modifier.width(8.dp))
                Card(
                    modifier =
                        Modifier
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                           // .clip(RoundedCornerShape(2.dp))
                    ,
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF2F2F2) // Set the card's background color
                    ),
                    elevation = CardDefaults.cardElevation(2.dp)
                ){
                    Text(
                        text = "${sideContent}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Color(0xFF2e6930),
                       modifier = Modifier.padding(vertical = 4.dp, horizontal = 12.dp)
                    )
                }

            }
            Spacer(modifier = Modifier.height(22.dp))
            Row(
                // modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "${content}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = Color.Gray

                )
            }
        }
    }
}