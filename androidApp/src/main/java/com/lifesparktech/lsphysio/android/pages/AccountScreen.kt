package com.lifesparktech.lsphysio.android.pages

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lsphysio.android.R
import com.lifesparktech.lsphysio.android.components.AccountInfoItem
import com.lifesparktech.lsphysio.android.components.AccountInformationCard
import com.lifesparktech.lsphysio.android.components.ClinicInformationCard
import com.lifesparktech.lsphysio.android.components.CommonTextFieldgrey
import com.lifesparktech.lsphysio.android.components.PaymentInformationCard
import com.lifesparktech.lsphysio.android.components.ProfessionalInformationCard
import com.lifesparktech.lsphysio.android.components.SecurityInformationCard

@Composable
fun AccountScreen() {
    var selectedItem by remember { mutableStateOf<String?>("Account Information") }
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(color = Color(0xFFf4f4f4)),
        contentPadding = PaddingValues(12.dp)
    ){
        item{
            Row {
                Text(
                    text = "Your Account",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xff222429),
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                // Left Panel with Account Info Items
                Card(
                    modifier = Modifier.fillMaxWidth(0.35f),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                    ) {
                        AccountInfoItem(
                            res = R.drawable.accountinfo,
                            label = "Account Information",
                            isSelected = selectedItem == "Account Information"
                        ) {
                            selectedItem = "Account Information"
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        AccountInfoItem(
                            res = R.drawable.clinicinfo,
                            label = "Clinic Information",
                            isSelected = selectedItem == "Clinic Information"
                        ) {
                            selectedItem = "Clinic Information"
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        AccountInfoItem(
                            res = R.drawable.professional,
                            label = "Professional Details",
                            isSelected = selectedItem == "Professional Details"
                        ) {
                            selectedItem = "Professional Details"
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        AccountInfoItem(
                            res = R.drawable.securityinfo,
                            label = "Account Security",
                            isSelected = selectedItem == "Account Security"
                        ) {
                            selectedItem = "Account Security"
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        AccountInfoItem(
                            res = R.drawable.payment,
                            label = "Payment and Billing",
                            isSelected = selectedItem == "Payment and Billing"
                        ) {
                            selectedItem = "Payment and Billing"
                        }
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Right Panel with Selected Item Details
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        when (selectedItem) {
                            "Account Information" -> AccountInformationCard()
                            "Clinic Information" -> ClinicInformationCard()
                            "Professional Details" -> ProfessionalInformationCard()
                            "Account Security" -> SecurityInformationCard()
                            "Payment and Billing" -> PaymentInformationCard()
                            else -> AccountInformationCard()
                        }
                    }
                }
            }
        }
    }
}
