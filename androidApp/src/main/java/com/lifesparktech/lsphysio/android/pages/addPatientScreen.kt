package com.lifesparktech.lsphysio.android.pages


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.benasher44.uuid.uuid4
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.lifesparktech.lsphysio.android.Controller.addPatient
import com.lifesparktech.lsphysio.android.components.CommonTextField
import com.lifesparktech.lsphysio.android.models.Patient
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
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
    var condition by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var countrycode by remember { mutableStateOf("91") }
    var expanded by remember { mutableStateOf(false) }
    var expandedCountries by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("") }
    var selectedOptionCountries by remember { mutableStateOf("91") }
    val options = listOf("Male", "Female", "Other")
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
    //val textField = remember { mutableStateListOf<EditableTextField>() }

    fun validateName() {
        nameError = if (name.trim().isEmpty()) "Name is required." else ""
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
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth().background(color = Color.White),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(text = "Add New Patient")
                        IconButton(onClick = {
                            showDialog = true
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Add"
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White // Set your desired color here
                )
            )
        },
        containerColor = Color.White
    ){
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .imePadding()
//            .nestedScroll(rememberImeNestedScrollConnection())
        ) {
            item {
                Spacer(modifier = Modifier.height(50.dp))
                CommonTextField(
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
                            CommonTextField(
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
                                    colors = TextFieldDefaults.outlinedTextFieldColors(
                                        focusedBorderColor =  Color(0xFFf9f9f8),
                                        unfocusedBorderColor =  Color(0xFFf9f9f8),
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor()
                                )
                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
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
                    Box(modifier = Modifier.weight(1f)) {
                        Column {
                            CommonTextField(
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
                            CommonTextField(
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
                Spacer(modifier = Modifier.height(8.dp))

                CommonTextField(
                    value = email,
                    onValueChange = {
                        email = it.trim()
                        validateEmail()
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

            item {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(modifier = Modifier.weight(0.35f)) {
                        Column {
                            Text(text = "Countries", style = TextStyle(fontSize = 16.sp))
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
                                    colors = TextFieldDefaults.outlinedTextFieldColors(
                                        focusedBorderColor = Color(0xFFf9f9f8),
                                        unfocusedBorderColor = Color(0xFFf9f9f8),
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor()
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedCountries,
                                    onDismissRequest = { expandedCountries = false }
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
                            CommonTextField(
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
                Spacer(modifier = Modifier.height(50.dp))
                CommonTextField(
                    value = address,
                    onValueChange = {
                        address = it
                        validateName()
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
                Text(text = "Diagnosis", style = TextStyle(fontSize = 16.sp))
                Spacer(modifier = Modifier.height(8.dp))
                Column{
                    MultiSelectExposedDropdownMenu(
                        items = items,
                        selectedItems = selectedItems.value,
                        onSelectionChange = { newSelection ->
                            selectedItems.value = newSelection
                            validateCondition()
                        }
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
                textFields.forEachIndexed { index, (label, value) -> // Destructure the pair into label and value
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.weight(0.75f)) {
                            OutlinedTextField(
                                value = value,
                                onValueChange = { newText ->
                                    // Update the specific field's value
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
                        validateEmail()
                        validateContact()
                        validateWeight()
                        validateHeight()
                        validateCondition()
                        validateGender()
                        if (
                            nameError.isEmpty() && ageError.isEmpty() && emailError.isEmpty()
                            && contactError.isEmpty() && weightError.isEmpty() &&
                            heightError.isEmpty() && conditionError.isEmpty() && genderError.isEmpty()
                        ) {
                            val extraDetailsList = textFields.map { (label, value) -> "$label: $value" }
                            val patient = Patient(
                                id = uuid4().toString(),
                                clinicId = Firebase.auth.currentUser?.uid ?: "",
                                name = name,
                                age = age.toInt(),
                                gender = gender,
                                contact = "$countrycode$contact",
                                address = address,
                                email = email,
                                height = height.toInt(),
                                weight = weight.toInt(),
                                diagnostics = selectedItems.value.toList(),
                                extraDetails = extraDetailsList
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
    onSelectionChange: (Set<String>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) } // Tracks dropdown visibility

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it } // Toggles dropdown state
    ) {
        // TextField to display selected items
        OutlinedTextField(
            value = if (selectedItems.isNotEmpty()) selectedItems.joinToString(", ") else "Select Diseases",
            onValueChange = {},
            readOnly = true,
          //  label = { Text("Select items") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFFf9f9f8),
                unfocusedBorderColor = Color(0xFFf9f9f8),
            ),
        )

        // Dropdown menu
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false } // Close the menu when clicked outside
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