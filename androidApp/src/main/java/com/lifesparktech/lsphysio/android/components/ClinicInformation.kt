package com.lifesparktech.lsphysio.android.components


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Button
import android.widget.DatePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lsphysio.android.R
import com.lifesparktech.lsphysio.android.pages.MultiSelectExposedDropdownMenu
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClinicInformationCard(){
    var clinicname by remember { mutableStateOf("") }
    var numberofStaff by remember { mutableStateOf("") }
    var clinicId by remember { mutableStateOf("") }
    var clinicnameError by remember { mutableStateOf("") }
    var clinicErrorID by remember { mutableStateOf("") }
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
    val specifications = listOf( "Orthopedic Physiotherapy", "Neurological Rehabilitation","Sports Injury Management","Pediatric Physiotherapy")
    var numberofStaffError by remember { mutableStateOf("") }
    val selectedItemspecifications = remember { mutableStateOf(setOf<String>()) }
    var specificationsError by remember { mutableStateOf("") }
    fun validateClinicName() {
        clinicnameError = if (clinicname.trim().isEmpty()) "Clinic Name is required." else ""
    }
    fun validateClinicId() {
        clinicErrorID = if (clinicId.trim().isEmpty()) "Clinic ID is required." else ""
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
    fun validateNumberofStaff() {
        numberofStaffError = if (numberofStaff.trim().isEmpty()) {
            "Number of Staff is required"
        } else if (numberofStaff.toIntOrNull() == null) {
            "Please enter a number of Staff age."
        } else {
            ""
        }
    }
    fun validatespecifications() {
        specificationsError = if (selectedItemspecifications ==null) "specifications is required." else ""
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
    Column(
        modifier = Modifier.padding(12.dp)
    ){
        Text(text = "Clinic Information", fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            color = Color(0xFFD6D6D6),
            thickness = 1.dp
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Box(
                modifier = Modifier.weight(1f)
            ){
                Column{
                    CommonTextFieldgrey(
                        value = clinicname,
                        onValueChange = {
                            clinicname = it
                            validateClinicName()
                        },
                        label = "Clinic Name"
                    )
                    if (clinicnameError.isNotEmpty()) {
                        Text(
                            text = clinicnameError,
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
        ){
            Box(
                modifier = Modifier.weight(1f)
            ){
                Column{
                    CommonTextFieldgrey(
                        value = clinicId,
                        onValueChange = {
                            clinicId = it
                            validateClinicId()
                        },
                        label = "Clinic ID"
                    )
                    if (clinicErrorID.isNotEmpty()) {
                        Text(
                            text = clinicErrorID,
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
        ){
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
                        onExpandedChange = { expandedCountries = !expandedCountries }
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
        Spacer(modifier = Modifier.height(12.dp))
        Column{
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Specifications", style = TextStyle(fontSize = 16.sp), fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Column{
                MultiSelectExposedDropdownMenu(
                    items = specifications,
                    selectedItems = selectedItemspecifications.value,
                    onSelectionChange = { newSelection ->
                        selectedItemspecifications.value = newSelection
                        validatespecifications()
                    },
                    defaultSelected = "Specifications"
                )
            }
            if (specificationsError.isNotEmpty()) {
                Text(
                    text = specificationsError,
                    color = Color.Red,
                    style = TextStyle(fontSize = 14.sp)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Column{
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Established Date", style = TextStyle(fontSize = 16.sp), fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            DatePickerExample(placeholder = "Established Date")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Column {
            CommonTextFieldgrey(
                value = numberofStaff,
                onValueChange = {
                    numberofStaff = it
                    validateNumberofStaff()
                },
                label = "Number of Staff",
                isNumeric = true,
                maxLength = 2,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            if (numberofStaffError.isNotEmpty()) {
                Text(
                    text = numberofStaffError,
                    color = Color.Red,
                    style = TextStyle(fontSize = 14.sp)
                )
            }
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
@Composable
fun DatePickerExample(placeholder: String) {
    var selectedDate by remember { mutableStateOf("") }
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
            selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
        }, year, month, day
    )
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .fillMaxWidth().background(color = Color(0xFFf4f4f4))
            .padding(horizontal = 2.dp, vertical = 4.dp),
       verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = if (selectedDate.isEmpty()) "$placeholder" else "$selectedDate",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
        Button(
            onClick = {
                datePickerDialog.show()
            },
            colors = ButtonDefaults.textButtonColors(
                containerColor = Color.Transparent
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.calendar),
                contentDescription = "calendar",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
