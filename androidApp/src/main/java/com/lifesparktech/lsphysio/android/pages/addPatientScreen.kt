package com.lifesparktech.lsphysio.android.pages
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.benasher44.uuid.uuid4
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.lifesparktech.lsphysio.android.Controller.addPatient
//import com.lifesparktech.lsphysio.android.Controller.addPatient
//import com.lifesparktech.lsphysio.android.Controller.addPatient
import com.lifesparktech.lsphysio.android.components.CommonTextFieldgrey
import com.lifesparktech.lsphysio.android.data.Patient
//import com.lifesparktech.lsphysio.android.models.Patient
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlin.String

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPatientScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var Status by remember { mutableStateOf("") }
    var hepaticIssues  by remember { mutableStateOf("") }
    var relatedComplication by remember { mutableStateOf("") }
    var chronicDisease by remember { mutableStateOf("") }
    var digestiveDisorders by remember { mutableStateOf("") }
    var diabetesEmergencies by remember { mutableStateOf("") }
    var surgery by remember { mutableStateOf("") }
    var familyDisease by remember { mutableStateOf("") }
    var ALlergies by remember { mutableStateOf("") }
    var Hemoglobin by remember { mutableStateOf("") }
    var heartRate by remember { mutableStateOf("") }
    var bloodSugar by remember { mutableStateOf("") }
    var bloodPressure by remember { mutableStateOf("") }
    var bodymassindex by remember { mutableStateOf("") }
    var bloodGroup by remember { mutableStateOf("") }
    var condition by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var expandedBloodGroup by remember { mutableStateOf(false) }
    var selectedBloodGroup by remember { mutableStateOf("A+") }
    var countrycode by remember { mutableStateOf("91") }
    var expanded by remember { mutableStateOf(false) }
    var expandedStatus by remember { mutableStateOf(false) }
    var expandedCountries by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("") }
    var selectedOptionStatus by remember { mutableStateOf("") }
    var selectedOptionCountries by remember { mutableStateOf("91") }
    val options = listOf("Male", "Female", "Other")
    val Statuss = listOf("Active", "Inactive")
    val countries = listOf("91", "44", "01")
    val scope = MainScope()
    val scrollState = rememberScrollState()
    var nameError by remember { mutableStateOf("") }
    var addressError by remember { mutableStateOf("") }
    var ageError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var weightError by remember { mutableStateOf("") }
    var heightError by remember { mutableStateOf("") }
    var contactError by remember { mutableStateOf("") }
    var conditionError by remember { mutableStateOf("") }
    var genderError by remember { mutableStateOf("") }
    var customFields by remember { mutableStateOf(mutableListOf<Pair<String, String>>()) }
    var textFieldCount by remember { mutableStateOf(0) }
    // Ensure textFields is a list of pairs (String, String)
    val textFields = remember { mutableStateListOf<Pair<String, String>>() }
    var showDialog by remember { mutableStateOf(false) }
    var newLabel by remember { mutableStateOf("") }
    var newText by remember { mutableStateOf("") }
    val items = listOf(
        "Fever", "Cancer", "Parkinson's", "Sugar", "Diabetes",
    )
    val selectedItems = remember { mutableStateOf(setOf<String>()) }
    fun validateName() {
        nameError = if (name.trim().isEmpty()) "Name is required." else ""
    }
    fun validateAddress() {
        addressError = if (address.trim().isEmpty()) "Address is required." else ""
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

    fun validateWeight() {
        weightError = if (weight.trim().toIntOrNull() == null) {
            "Weight is required."
        } else {
            ""
        }
    }
    fun validateHeight() {
        heightError = if (height.trim().isEmpty() || height.trim().toIntOrNull() == null) {
            "Height is required."
        } else {
            ""
        }
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
    fun validateCondition() {
        conditionError = if (selectedItems ==null) "Diagnosis is required." else ""
    }
    fun validateGender() {
        genderError = if (gender.trim().isEmpty()) "Gender is required." else ""
    }
    Card(
        modifier = Modifier.padding(12.dp),//.height(700.dp).fillMaxWidth(0.35f),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White // Set the card's background color
        )
    ){
        LazyColumn(
            modifier = Modifier
                // .weight(1f)
                .padding(16.dp)
                .imePadding()
        ) {
            item{
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.clickable{navController.popBackStack() }
                    )
                    Text(text = "Add New Patient", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF222429))
                    IconButton(onClick = {
                        showDialog = true
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add"
                        )
                    }
                }
            }
            item{
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    color = Color(0xFFD6D6D6),
                    thickness = 1.dp
                )
            }
            item {
                //  Spacer(modifier = Modifier.height(50.dp))
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

            item {
                Spacer(modifier = Modifier.height(8.dp))
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
                            Text(text = "Gender", style = TextStyle(fontSize = 16.sp), fontWeight = FontWeight.SemiBold)
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
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Weight Field
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        Column {
                            CommonTextFieldgrey(
                                value = weight,
                                onValueChange = {
                                    weight = it
                                    validateWeight()
                                },
                                label = "Weight (kg)",
                                isNumeric = true,
                                maxLength = 3,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                            if (weightError.isNotEmpty()) {
                                Text(
                                    text = weightError,
                                    color = Color.Red,
                                    style = TextStyle(fontSize = 13.sp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))

                    // Height Field
                    Box(modifier = Modifier.weight(1f)) {
                        Column {
                            CommonTextFieldgrey(
                                value = height,
                                onValueChange = {
                                    height = it
                                    validateHeight()
                                },
                                label = "Height (cm)",
                                isNumeric = true,
                                maxLength = 3,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                            if (heightError.isNotEmpty()) {
                                Text(
                                    text = heightError,
                                    color = Color.Red,
                                    style = TextStyle(fontSize = 13.sp)
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Box(modifier = Modifier.weight(1f)){
                        CommonTextFieldgrey(
                            value = email,
                            onValueChange = {
                                email = it.trim()
                              //  validateEmail()
                            },
                            label = "Email",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )
                        if (emailError.isNotEmpty()) {
                            Text(
                                text = emailError,
                                color = Color.Red,
                                style = TextStyle(fontSize = 14.sp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        Column {
                            Text(text = "Status", style = TextStyle(fontSize = 16.sp), fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(8.dp))
                            ExposedDropdownMenuBox(
                                expanded = expandedStatus,
                                onExpandedChange = { expandedStatus = !expandedStatus }
                            ) {
                                OutlinedTextField(
                                    value = selectedOptionStatus,
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStatus)
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
                                    expanded = expandedStatus,
                                    onDismissRequest = { expandedStatus = false },
                                    modifier = Modifier.background(color = Color(0xFFf2f4f5))
                                ) {
                                    Statuss.forEach { option ->
                                        DropdownMenuItem(
                                            text = { Text(option) },
                                            onClick = {
                                                Status = option
                                                selectedOptionStatus = option
                                                expandedStatus = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                }

            }

            item {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(modifier = Modifier.weight(0.2f)) {
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
            }
            item {
                Spacer(modifier = Modifier.height(12.dp))
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
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Diagnosis", style = TextStyle(fontSize = 16.sp), fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                Column{
                    MultiSelectExposedDropdownMenu(
                        items = items,
                        selectedItems = selectedItems.value,
                        onSelectionChange = { newSelection ->
                            selectedItems.value = newSelection
                            validateCondition()
                        },
                        defaultSelected = "Diseases"
                    )
                }
                if (conditionError.isNotEmpty()) {
                    Text(
                        text = conditionError,
                        color = Color.Red,
                        style = TextStyle(fontSize = 14.sp)
                    )
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween // Adjust space between items as needed
                ) {
                    CommonTextFieldgrey(
                        value = hepaticIssues,
                        onValueChange = {
                            hepaticIssues = it
                        },
                        label = "Hepatic Issues",
                        modifier = Modifier.weight(1f) // Use weight to ensure both fields share space equally
                    )

                    Spacer(modifier = Modifier.width(16.dp)) // Space between the two text fields

                    CommonTextFieldgrey(
                        value = relatedComplication,
                        onValueChange = {
                            relatedComplication = it
                        },
                        label = "Related Complication",
                        modifier = Modifier.weight(1f) // Use weight to ensure both fields share space equally
                    )
                }
            }


            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween // Adjust space between items as needed
                ) {
                    // First pair of text fields
                    CommonTextFieldgrey(
                        value = hepaticIssues,
                        onValueChange = {
                            hepaticIssues = it
                        },
                        label = "Hepatic Issues",
                        modifier = Modifier.weight(1f) // Ensure equal space for each field
                    )

                    Spacer(modifier = Modifier.width(16.dp)) // Space between the first and second text fields

                    CommonTextFieldgrey(
                        value = relatedComplication,
                        onValueChange = {
                            relatedComplication = it
                        },
                        label = "Related Complication",
                        modifier = Modifier.weight(1f) // Ensure equal space for each field
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween // Adjust space between items as needed
                ) {
                    // Second pair of text fields
                    CommonTextFieldgrey(
                        value = chronicDisease,
                        onValueChange = {
                            chronicDisease = it
                        },
                        label = "Chronic Disease",
                        modifier = Modifier.weight(1f) // Ensure equal space for each field
                    )

                    Spacer(modifier = Modifier.width(16.dp)) // Space between the first and second text fields

                    CommonTextFieldgrey(
                        value = digestiveDisorders,
                        onValueChange = {
                            digestiveDisorders = it
                        },
                        label = "Digestive Disorders",
                        modifier = Modifier.weight(1f) // Ensure equal space for each field
                    )
                }
            }


            item {
                CommonTextFieldgrey(
                    value = diabetesEmergencies,
                    onValueChange = {
                        diabetesEmergencies = it
                    },
                    label = "Diabetes Emergencies"
                )
            }

            item {
                CommonTextFieldgrey(
                    value = surgery,
                    onValueChange = {
                        surgery = it
                    },
                    label = "Surgery"
                )
            }

            item {
                CommonTextFieldgrey(
                    value = familyDisease,
                    onValueChange = {
                        familyDisease = it
                    },
                    label = "Family Disease"
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Blood Group dropdown
                    Box(modifier = Modifier.weight(0.5f)) {
                        Column {
                            Text(
                                text = "Blood Group",
                                style = TextStyle(fontSize = 16.sp),
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            ExposedDropdownMenuBox(
                                expanded = expandedBloodGroup,
                                onExpandedChange = { expandedBloodGroup = !expandedBloodGroup }
                            ) {
                                OutlinedTextField(
                                    value = selectedBloodGroup,
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedBloodGroup)
                                    },
                                    colors = TextFieldDefaults.textFieldColors(
                                        containerColor = Color(0xFFf2f4f5),
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor()
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedBloodGroup,
                                    onDismissRequest = { expandedBloodGroup = false },
                                    modifier = Modifier.background(color = Color(0xFFf2f4f5))
                                ) {
                                    val bloodGroupOptions = listOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-")
                                    bloodGroupOptions.forEach { option ->
                                        DropdownMenuItem(
                                            text = { Text(option) },
                                            onClick = {
                                                selectedBloodGroup = option
                                                expandedBloodGroup = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp)) // Space between the fields

                    // Allergies text field
                    Box(modifier = Modifier.weight(0.5f)) {
                        CommonTextFieldgrey(
                            value = ALlergies,
                            onValueChange = {
                                ALlergies = it
                            },
                            label = "Allergies"
                        )
                    }
                }
            }


            item {
                CommonTextFieldgrey(
                    value = Hemoglobin,
                    onValueChange = {
                        Hemoglobin = it
                    },
                    label = "Hemoglobin"
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween // Adjust space between items as needed
                ) {
                    // Heart Rate field
                    CommonTextFieldgrey(
                        value = heartRate,
                        onValueChange = {
                            heartRate = it
                        },
                        label = "Heart Rate",
                        modifier = Modifier.weight(1f) // Ensure equal space for each field
                    )

                    Spacer(modifier = Modifier.width(16.dp)) // Space between the fields

                    // Blood Sugar field
                    CommonTextFieldgrey(
                        value = bloodSugar,
                        onValueChange = {
                            bloodSugar = it
                        },
                        label = "Blood Sugar",
                        modifier = Modifier.weight(1f) // Ensure equal space for each field
                    )
                }
            }


            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween // Adjust space between items as needed
                ) {
                    // Blood Pressure field
                    CommonTextFieldgrey(
                        value = bloodPressure,
                        onValueChange = {
                            // Validate blood pressure input, e.g., "120/80"
                            if (it.matches(Regex("^\\d{0,3}/?\\d{0,3}\$"))) {
                                bloodPressure = it
                            } else {
                                println("enter in correct format eg: 120/80")
                            }
                        },
                        label = "Blood Pressure",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f) // Ensure equal space for each field
                    )

                    Spacer(modifier = Modifier.width(16.dp)) // Space between the fields

                    // Body Mass Index field
                    CommonTextFieldgrey(
                        value = bodymassindex,
                        onValueChange = {
                            bodymassindex = it
                        },
                        label = "Body Mass Index",
                        modifier = Modifier.weight(1f) // Ensure equal space for each field
                    )
                }
            }


            item {
                CommonTextFieldgrey(
                    value = condition,
                    onValueChange = {
                        condition = it
                    },
                    label = "Condition"
                )
            }

            item {
                textFields.forEachIndexed { index, (label, value) ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.weight(0.75f)) {
                            OutlinedTextField(
                                value = value,
                                onValueChange = { newText ->
                                    textFields[index] = textFields[index].copy(second = newText)
                                },
                                label = { Text(label) }, // Show the corresponding label
                                modifier = Modifier,
                                maxLines =1

                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = { textFields.removeAt(index) },  // Remove the specific text field pair
                            modifier = Modifier.weight(0.1f)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Delete",
                                tint = Color(0XFFA91101)
                            )
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        validateName()
                        validateAge()
                        //validateEmail()
                        validateContact()
                        validateWeight()
                        validateHeight()
                        validateCondition()
                        validateGender()
                        if (
                            nameError.isEmpty() && ageError.isEmpty()
                            && contactError.isEmpty() && weightError.isEmpty() &&
                            heightError.isEmpty() && conditionError.isEmpty() && genderError.isEmpty()
                        ) {
                            val extraDetailsList = textFields.map { (label, value) -> "$label: $value" }
                            val patient = Patient(
                                serialNo = uuid4().toString(),
                                clinicId = Firebase.auth.currentUser?.uid ?: "",
                                name = name,
                                age = age.toInt(),
                                gender = gender,
                                phone = "$countrycode$contact",
                                address = address,
                                email = email,
                                height = height.toInt(),
                                weight = weight.toInt(),
                                diagnostics = selectedItems.value.toList(),
                               // stsTest = emptyList(),
                                extraDetails = extraDetailsList,
                                bloodGroup = selectedBloodGroup,
                                allergies = ALlergies,
                                bodymassindex = bodymassindex.toDoubleOrNull() ?: 0.0,
                                bloodPressure = bloodPressure,
                                diabetesemergencies = diabetesEmergencies,
                                chronicdisease= chronicDisease,
                                surgery = surgery,
                                hepaticIssues = hepaticIssues,
                                familydisease = familyDisease,
                                relatedcomplication = relatedComplication,
                                hemoglobin = Hemoglobin,
                                bloodsugar = bloodSugar,
                                heartrate = heartRate
                            )

                            scope.launch {
                                addPatient(patient)
                                navController.popBackStack()
                            }


                        }
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFF43958F)),
                    modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .imePadding(),
                    shape = RoundedCornerShape(4.dp),
                ) {
                    Text(text = "Add")
                }
            }
        }

        @Composable
        fun LabelAndTextField(
            newLabel: String,
            onLabelChange: (String) -> Unit,
            newText: String,
            onTextChange: (String) -> Unit
        ) {
            Column {
                TextField(
                    value = newLabel,
                    onValueChange = onLabelChange,
                    label = { Text("Label") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = newText,
                    onValueChange = onTextChange,
                    label = { Text("Text") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines=1,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                )
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            // Add new text field to the mutable list
                            textFields.add(Pair(newLabel, newText))
                            newLabel = ""
                            newText = ""
                            showDialog = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF43958F))
                            .then(Modifier) // Ensure proper chaining
                    ) {
                        Text("Add", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDialog = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .border(
                                width = 1.dp,
                                color = Color(0xFF43958F),
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        Text("Cancel", color = Color(0xFF43958F))
                    }
                },
                containerColor = Color(0xFFF4F4F4),
                title = { Text("Add New Text Field") },
                text = {
                    LabelAndTextField(
                        newLabel = newLabel,
                        onLabelChange = { newLabel = it },
                        newText = newText,
                        onTextChange = { newText = it }
                    )
                }
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiSelectExposedDropdownMenu(
    items: List<String>,
    selectedItems: Set<String>,
    onSelectionChange: (Set<String>) -> Unit,
    defaultSelected: String = ""
) {
    var expanded by remember { mutableStateOf(false) } // Tracks dropdown visibility

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(color = Color(0xFFf2f4f5))
    ) {
        // TextField to display selected items
        OutlinedTextField(
            value = if (selectedItems.isNotEmpty()) selectedItems.joinToString(", ") else "Select $defaultSelected",
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            colors =  TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFf2f4f5),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(color = Color(0xFFf2f4f5))
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        val newSelection = selectedItems.toMutableSet()
                        if (newSelection.contains(item)) {
                            newSelection.remove(item) // Remove item if already selected
                        } else {
                            newSelection.add(item) // Add item if not selected
                        }
                        onSelectionChange(newSelection) // Trigger selection change callback
                    },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Checkbox(
                                checked = selectedItems.contains(item),
                                onCheckedChange = null // Checkbox is controlled by the Row's click
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = item)
                        }
                    }

                )
            }
        }
    }
}

fun generateRandomStringId(length: Int = 20): String {
    val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}