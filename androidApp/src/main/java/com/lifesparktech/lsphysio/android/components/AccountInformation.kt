package com.lifesparktech.lsphysio.android.components

import android.widget.Button
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lsphysio.android.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountInformationCard(){
    var text by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var contactError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var expandedCountries by remember { mutableStateOf(false) }
    var selectedOptionCountries by remember { mutableStateOf("91") }
    val countries = listOf("91", "44", "01")
    var countrycode by remember { mutableStateOf("91") }
    var address by remember { mutableStateOf("") }
    var addressError by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var ageError by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    val options = listOf("Male", "Female", "Other")
    var genderError by remember { mutableStateOf("") }
    fun validateName() {
        nameError = if (name.trim().isEmpty()) "Name is required." else ""
    }
    fun validateAddress() {
        addressError = if (address.trim().isEmpty()) "Address is required." else ""
    }
    fun validateContact() {
        contactError = if (contact.trim().isEmpty()) {
            "Number is required"
        } else if (contact.trim().length != 10) {
            "Please enter a valid contact number (10 digits)."
        } else {
            ""
        }
    }
    fun validateAge() {
        ageError = if (age.trim().isEmpty()) {
            "Age is required"
        } else if (age.toIntOrNull() == null) {
            "Please enter a valid age."
        } else {
            ""
        }
    }
    fun validateEmail() {
        val emailPattern =
            "^[a-zA-Z0-9.a-zA-Z0-9.!#\$%&'*+-/=?^_`{|}~][^ @]+@[a-zA-Z0-9]+\\.[a-zA-Z]+\$".toRegex()
        emailError = when {
            email.trim().isEmpty() -> "Email is required."
            !email.matches(emailPattern) -> "Please enter a valid email."
            else -> ""
        }
    }
    fun validateGender() {
        genderError = if (gender.trim().isEmpty()) "Gender is required." else ""
    }
    Column(
            modifier = Modifier.padding(12.dp)
        ){
            Text(text = "Account Information", fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                color = Color(0xFFD6D6D6),
                thickness = 1.dp
            )
            Row(
                modifier = Modifier.padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    painter = painterResource(id = R.drawable.personimage),
                    contentDescription = "logo",
                    modifier = Modifier
                        .size(75.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = Color(0xFFD6E7EE)
                    )
                ){
                    Text(text = "Upload", color = Color(0xFF1d4240))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Menu",
                    tint = Color(0xFFD21404)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Box(
                    modifier = Modifier.weight(1f)
                ){
                    Column{
                        CommonTextFieldgrey(
                            value = name,
                            onValueChange = {
                                name = it
                                validateName()
                            },
                            label = "Name"
                        )
                        if (nameError.isNotEmpty()) {
                            Text(
                                text = nameError,
                                color = Color.Red,
                                style = TextStyle(fontSize = 14.sp)
                            )
                        }
                    }

                }
                Spacer(
                    modifier = Modifier.width(12.dp)
                )
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    Column {
                        CommonTextFieldgrey(
                            value = email,
                            onValueChange = {
                                email = it
                                validateEmail()
                            },
                            label = "Email"
                        )
                        if (emailError.isNotEmpty()) {
                            Text(
                                text = emailError,
                                color = Color.Red,
                                style = TextStyle(fontSize = 14.sp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    Column {
                        CommonTextFieldgrey(
                            value = age,
                            onValueChange = {
                                age = it
                                validateAge()
                            },
                            label = "Age",
                            isNumeric = true,
                            maxLength = 2,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        if (ageError.isNotEmpty()) {
                            Text(
                                text = ageError,
                                color = Color.Red,
                                style = TextStyle(fontSize = 14.sp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Box(modifier = Modifier.weight(1f)) {
                    Column {
                        Text(text = "Gender", style = TextStyle(fontSize = 16.sp))
                        Spacer(modifier = Modifier.height(8.dp))
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            OutlinedTextField(
                                value = selectedOption,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                },
                                colors =  TextFieldDefaults.textFieldColors(
                                    containerColor = Color(0xFFf2f4f5),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.background(color = Color(0xFFf2f4f5))
                            ) {
                                options.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            gender = option
                                            selectedOption = option
                                            expanded = false
                                            validateGender()
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(modifier = Modifier.weight(0.35f)) {
                    Column {
                        Text(text = "Countries", style = TextStyle(fontSize = 16.sp), fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(8.dp))
                        ExposedDropdownMenuBox(
                            expanded = expandedCountries,
                            onExpandedChange = { expandedCountries = !expandedCountries },
                            modifier = Modifier.background(color = Color(0xFFf2f4f5))
                        ) {
                            OutlinedTextField(
                                value = "+$selectedOptionCountries",
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCountries)
                                },
                                colors =  TextFieldDefaults.textFieldColors(
                                    containerColor = Color(0xFFf2f4f5),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = expandedCountries,
                                onDismissRequest = { expandedCountries = false },
                                modifier = Modifier.background(color = Color(0xFFf2f4f5))
                            ) {
                                countries.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text("+$option") },
                                        onClick = {
                                            countrycode = option
                                            selectedOptionCountries = option
                                            expandedCountries = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Box(modifier = Modifier.weight(0.75f)) {
                    Column {
                        CommonTextFieldgrey(
                            value = contact,
                            onValueChange = {
                                contact = it
                                validateContact()
                            },
                            label = "Contact",
                            isNumeric = true,
                            maxLength = 10,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                        )
                        if (contactError.isNotEmpty()) {
                            Text(
                                text = contactError,
                                color = Color.Red,
                                style = TextStyle(fontSize = 14.sp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            CommonTextFieldgrey(
                value = "OWNER",
                isRead = true,
                onValueChange = {
                    address = it
                    validateAddress()
                },
                label = "Access Permission"
            )
            Spacer(modifier = Modifier.height(16.dp))
            CommonTextFieldgrey(
                value = address,
                onValueChange = {
                    address = it
                    validateAddress()
                },
                label = "Address"
            )

            if (addressError.isNotEmpty()) {
                Text(
                    text = addressError,
                    color = Color.Red,
                    style = TextStyle(fontSize = 14.sp)
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                color = Color(0xFFD6D6D6),
                thickness = 1.dp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ){
                Button(
                    border = BorderStroke(width = 1.dp, color = Color(0xFFD6E7EE)),
                    onClick = {},
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = Color.Transparent
                    )
                ){
                    Text(text = "Cancel", fontWeight = FontWeight.Bold, color = Color(0xFF1d4240))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = Color(0xFFD6E7EE)
                    )
                ){
                    Text(text = "Update", color = Color(0xFF1d4240))
                }
            }
        }
}