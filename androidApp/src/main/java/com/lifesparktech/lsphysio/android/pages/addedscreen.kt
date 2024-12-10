package com.lifesparktech.lsphysio.android.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import com.example.lsphysio.android.R
import com.lifesparktech.lsphysio.android.data.samplePatients


@Composable
fun AddedScreen() {
    Scaffold(modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFf4f4f4)
       // color = Color(0xFFf4f4f4)
    ) {
        SimpleTable()
    }
}
@Composable
fun SimpleTable() {
    Card(
        modifier = Modifier.padding(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8FAFB) // Set the card's background color
        )
    ) {
        LazyColumn(modifier = Modifier) {
            item{
                Row(modifier = Modifier.fillMaxWidth()) {
                    TableCell(text = "No.", modifier = Modifier.weight(0.3f),fontWeight = FontWeight.Bold)
                    TableCell(text = "Name", modifier = Modifier.weight(0.7f),fontWeight = FontWeight.Bold)
                    TableCell(text = "Age", modifier = Modifier.weight(0.3f),fontWeight = FontWeight.Bold)
                    TableCell(text = "Email", modifier = Modifier.weight(0.8f),fontWeight = FontWeight.Bold)
                    TableCell(text = "Phone", modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold)
                    TableCell(text = "Status", modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold)
                }
            }
            item{
                samplePatients.forEachIndexed { index, patient ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                                .background(if (index % 2 == 0) Color.White else Color(0xFFF8FAFB))
//                                border(width = 1.dp,
//                                    color = Color.Gray
//                                )
                        ) {
                            TableCell(text = "${patient.serialNo}", modifier = Modifier.weight(0.3f))
                            TableCell(text = "${patient.name}", modifier = Modifier.weight(0.7f))
                            TableCell(text = "${patient.age}", modifier = Modifier.weight(0.3f))
                            TableCell(text = "${patient.email}", modifier = Modifier.weight(0.8f))
                            TableCell(text = "${patient.phone}", modifier = Modifier.weight(0.5f))
                            Box(
                                modifier = Modifier.weight(0.5f).clip(RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            //    modifier = Modifier).border(width = 1.dp, color = Color.Black),
                               // contentAlignment = Alignment.Center
                            ){
                                TableCellBadge(
                                    text = patient.status,
                                    textColor = if (patient.status == "Active") Color(0xFF0F5132) else Color(0xFFD0312D), // Conditional color
                                    modifier = Modifier.background(color = Color(0xFFE0F2EE)),
                                    backgroundColor = if (patient.status == "Active") Color(0xFFD6E7EE) else  Color(0xFFFFCACA)
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                    }
                }
            }
        }

}

@Composable
fun TableCell(text: String, textColor: Color = MaterialTheme.colorScheme.onSurface, fontWeight: FontWeight = FontWeight.Normal, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            //.border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
            .padding(8.dp),
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = fontWeight,
        color = textColor
    )
}

@Composable
fun TableCellBadge(text: String, textColor: Color = MaterialTheme.colorScheme.onSurface, backgroundColor: Color = Color.Transparent, fontWeight: FontWeight = FontWeight.Normal, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(120.dp)
            .background(
               // Color(0xFFE3F2EE)
                        backgroundColor
            ).padding(horizontal = 12.dp, vertical = 8.dp).clip(RoundedCornerShape(12.dp)),
    ){
        Row(
           // modifier.clip())
        ){
            Image(
                painter = painterResource(id = R.drawable.radio_button),
                contentDescription = "Logout",
                modifier = Modifier
                    .size(20.dp)
            )
           Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = fontWeight,
                color = textColor
            )
        }

    }
}
