package com.lifesparktech.lsphysio.android.pages
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Paint.Align
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.github.barteksc.pdfviewer.PDFView
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import java.io.File
import java.io.FileOutputStream
import com.example.lsphysio.android.R
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.ListItem
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.UnitValue
import com.lifesparktech.lsphysio.android.Controller.addedGasData
import com.lifesparktech.lsphysio.android.Controller.addedMiniData
import com.lifesparktech.lsphysio.android.Controller.fetchPatients
import com.lifesparktech.lsphysio.android.data.GASResult
import com.lifesparktech.lsphysio.android.data.MiniBestResult
import com.lifesparktech.lsphysio.android.data.Patient
import com.lifesparktech.lsphysio.android.data.instructions
import com.lifesparktech.lsphysio.android.data.minibestquestions
import com.lifesparktech.lsphysio.android.data.optionsList
import java.text.SimpleDateFormat
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiniBestScreen(onPreviewPdf: (File) -> Unit, navController: NavController) {
    val patients = remember { mutableStateOf<List<Patient>>(emptyList()) }
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("") }
    var patient by remember { mutableStateOf(Patient()) }
    var pdfFile by remember { mutableStateOf<File?>(null) }
    val context = LocalContext.current
    val shareLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {}
    LaunchedEffect(Unit) {
        try{
            val result = fetchPatients()
            patients.value = result
        }
        finally {
            //isLoading = false
        }
    }


    val sections = listOf(
        Pair(0, "ANTICIPATORY" to "/6"),
        Pair(3, "REACTIVE POSTURAL CONTROL" to "/6"),
        Pair(6, "SENSORY ORIENTATION" to "/6"),
        Pair(9, "DYNAMIC GAIT" to "/10")
    )
    var answers by remember { mutableStateOf(List(14) { 2 }) }

    var trial1Time1 by remember { mutableStateOf("") }
    var question8sec by remember { mutableStateOf("") }
    var trial1Time2 by remember { mutableStateOf("") }
    var trial2Time1 by remember { mutableStateOf("") }
    var trial2Time2 by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }
    var leftScore by remember { mutableStateOf(2) }
    var rightScore by remember { mutableStateOf(2) }
    var leftScore6 by remember { mutableStateOf(2) }
    var rightScore6 by remember { mutableStateOf(2) }
    val totalScore = answers.filterIndexed { index, _ -> index != 2 && index != 5 }.sum() + minOf(leftScore, rightScore) + minOf(leftScore6, rightScore6)
//    var totalScore = answers.sum() + minOf(leftScore, rightScore) + minOf(leftScore6, rightScore6)
    var selectedScore by remember { mutableStateOf(2) }
    var selectedScoreRight by remember { mutableStateOf(2) }
    var selectedScore6 by remember { mutableStateOf(2) }
    var selectedScoreRight6 by remember { mutableStateOf(2) }
    var anticipatory by remember { mutableStateOf(0) }
    var reactive_postural_control by remember { mutableStateOf(0) }
    var sensory_orientation by remember { mutableStateOf(0) }
    var dynamic_gait by remember { mutableStateOf(0) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val subscores = sections.map { (startIndex, _) ->
        when (startIndex) {
            0 -> {
                val score = answers.subList(startIndex, startIndex + 3)
                    .filterIndexed { index, _ -> index != 2 } // Exclude index 2
                    .sum() + minOf(leftScore, rightScore)
                anticipatory = score
                score
            }
            3 -> {
                val score = answers.subList(startIndex, startIndex + 3)
                    .filterIndexed { index, _ -> index != 2 } // Exclude index 5
                    .sum() + minOf(leftScore6, rightScore6)
                reactive_postural_control = score
                score
            }
            9 -> {
                val score = answers.subList(startIndex, startIndex + 5).sum()
                sensory_orientation = score
                score
            }
            else -> {
                val endIndex = startIndex + 3
                val score = answers.subList(startIndex, endIndex).sum()
                dynamic_gait = score
                score
            }
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item{
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.clickable{navController.popBackStack() }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Mini-BESTest:", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(text = " Balance Evaluation Systems Test", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Normal)
                }
                Button(
                    onClick = {
                        answers = List(14) { 2 }
                        isSubmitted = false
                        leftScore = 2
                        rightScore = 2
                        leftScore6 = 2
                        rightScore6 = 2
                        selectedScore = 2
                        selectedScoreRight = 2
                        selectedScore6 = 2
                        selectedScoreRight6 = 2
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = Color(0xFFD6E7EE)
                    )
                ) {
                    Text("Reset Answers", color = Color.Black)
                }
            }
        }
        item{
            Spacer(modifier = Modifier.height(8.dp))
        }
        item{
            Column(
                modifier = Modifier.padding(horizontal = 12.dp)
            ){
                Text(text = "Enter a patient", style = TextStyle(fontSize = 16.sp), fontWeight = FontWeight.SemiBold)
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
                            containerColor = Color(0xffEBEBEB),
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
                        patients.value.forEach{option ->
                            DropdownMenuItem(
                                text = { Text(option.name) },
                                onClick = {
                                    patient = option
                                    selectedOption = option.name
                                    expanded = false
                                }
                            )

                        }
                    }
                }
            }
        }
        item{
            Spacer(modifier = Modifier.height(12.dp))
        }
        minibestquestions.zip(instructions).zip(optionsList).forEachIndexed { index, (questionAndInstruction, options) ->
            val (question, instruction) = questionAndInstruction
            sections.find { it.first == index }?.let { (_, titleAndScore) ->
                val (title, maxScore) = titleAndScore
                val sectionIndex = sections.indexOfFirst { it.first == index }
                val subScore = subscores[sectionIndex]
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = title,
                            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "SUB SCORE: $subScore$maxScore",
                            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            if (index == 2) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White // Set the card's background color
                        )
                    ){
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                "3. STAND ON ONE LEG: ",
                                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Instruction: Look straight ahead. Keep your hands on your hips. Lift your leg off of the ground behind you without touching or resting your raised leg upon your other standing leg. Stay standing on one leg as long as you can. Look straight ahead. Lift now.",
                                fontSize = 14.sp
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text(
                                    text = "Left:",
                                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Time in Seconds Trial 1: ", style = TextStyle(fontSize = 14.sp))
                                Spacer(modifier = Modifier.width(4.dp))
                                BasicTextField(
                                    value = trial1Time1,
                                    onValueChange = {
                                        if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                                            trial1Time1 = it
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done // Handles "Done" action
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            focusManager.clearFocus() // Dismiss the keyboard when "Done" is pressed
                                        }
                                    ),
                                    textStyle = TextStyle(
                                        fontSize = 12.sp,
                                        color = Color.Black
                                    ),
                                    singleLine = true,
                                    modifier = Modifier
                                        .width(30.dp)
                                        .background(Color.Transparent)
                                        .padding(4.dp)
                                        .focusRequester(focusRequester)
                                        .drawBehind {
                                            val strokeWidth = 1.dp.toPx()
                                            val y = size.height - strokeWidth / 2
                                            drawLine(
                                                color = Color.Black,
                                                start = Offset(0f, y),
                                                end = Offset(size.width, y),
                                                strokeWidth = strokeWidth
                                            )
                                        }
                                        .clickable { focusRequester.requestFocus() }
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text("Sec", style = TextStyle(fontSize = 14.sp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Trial 2: ", style = TextStyle(fontSize = 14.sp))
                                Spacer(modifier = Modifier.width(2.dp))
                                BasicTextField(
                                    value = trial1Time1,
                                    onValueChange = {
                                        if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                                            trial1Time1 = it
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done // Handles "Done" action
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            focusManager.clearFocus() // Dismiss the keyboard when "Done" is pressed
                                        }
                                    ),
                                    textStyle = TextStyle(
                                        fontSize = 12.sp,
                                        color = Color.Black
                                    ),
                                    singleLine = true,
                                    modifier = Modifier
                                        .width(30.dp)
                                        .background(Color.Transparent)
                                        .padding(4.dp)
                                        .focusRequester(focusRequester)
                                        .drawBehind {
                                            val strokeWidth = 1.dp.toPx()
                                            val y = size.height - strokeWidth / 2
                                            drawLine(
                                                color = Color.Black,
                                                start = Offset(0f, y),
                                                end = Offset(size.width, y),
                                                strokeWidth = strokeWidth
                                            )
                                        }
                                        .clickable { focusRequester.requestFocus() }
                                )
                                Text("Sec", style = TextStyle(fontSize = 14.sp))
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text(
                                    text = "Right:",
                                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Time in Seconds Trial 1: ", style = TextStyle(fontSize = 14.sp))
                                Spacer(modifier = Modifier.width(4.dp))
                                BasicTextField(
                                    value = trial1Time1,
                                    onValueChange = {
                                        if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                                            trial1Time1 = it
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done // Handles "Done" action
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            focusManager.clearFocus() // Dismiss the keyboard when "Done" is pressed
                                        }
                                    ),
                                    textStyle = TextStyle(
                                        fontSize = 12.sp,
                                        color = Color.Black
                                    ),
                                    singleLine = true,
                                    modifier = Modifier
                                        .width(30.dp)
                                        .background(Color.Transparent)
                                        .padding(4.dp)
                                        .focusRequester(focusRequester)
                                        .drawBehind {
                                            val strokeWidth = 1.dp.toPx()
                                            val y = size.height - strokeWidth / 2
                                            drawLine(
                                                color = Color.Black,
                                                start = Offset(0f, y),
                                                end = Offset(size.width, y),
                                                strokeWidth = strokeWidth
                                            )
                                        }
                                        .clickable { focusRequester.requestFocus() }
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Sec", style = TextStyle(fontSize = 14.sp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Trial 2: ", style = TextStyle(fontSize = 14.sp))
                                Spacer(modifier = Modifier.width(4.dp))
                                BasicTextField(
                                    value = trial1Time1,
                                    onValueChange = {
                                        if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                                            trial1Time1 = it
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done // Handles "Done" action
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            focusManager.clearFocus() // Dismiss the keyboard when "Done" is pressed
                                        }
                                    ),
                                    textStyle = TextStyle(
                                        fontSize = 12.sp,
                                        color = Color.Black
                                    ),
                                    singleLine = true,
                                    modifier = Modifier
                                        .width(30.dp)
                                        .background(Color.Transparent)
                                        .padding(4.dp)
                                        .focusRequester(focusRequester)
                                        .drawBehind {
                                            val strokeWidth = 1.dp.toPx()
                                            val y = size.height - strokeWidth / 2
                                            drawLine(
                                                color = Color.Black,
                                                start = Offset(0f, y),
                                                end = Offset(size.width, y),
                                                strokeWidth = strokeWidth
                                            )
                                        }
                                        .clickable { focusRequester.requestFocus() }
                                )
                                Text("Sec", style = TextStyle(fontSize = 14.sp))
                            }
                        }
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth()
                        ){
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(text = "Left: ", fontSize = 14.sp, fontWeight= FontWeight.Bold, modifier = Modifier.padding(start = 12.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = selectedScore == 0,
                                        onClick = {
                                            selectedScore = 0
                                            leftScore = selectedScore
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = Color(0xFF005749)
                                        )
                                    )
                                    Text(text = "(0) Severe: Unable.", fontSize = 14.sp)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = selectedScore == 1,
                                        onClick = {
                                            selectedScore = 1
                                            leftScore = selectedScore
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = Color(0xFF005749)
                                        )

                                    )
                                    Text(text = "(1) Moderate: < 20 s.", fontSize = 14.sp)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = selectedScore == 2,
                                        onClick = {
                                            selectedScore = 2
                                            leftScore = selectedScore
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = Color(0xFF005749)
                                        )
                                    )
                                    Text(text = "(2) Normal: 20 s.", fontSize = 14.sp)
                                }
                            }
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(text = "Right: ", fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(start = 12.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = selectedScoreRight == 0,
                                        onClick = {
                                            selectedScoreRight = 0
                                            rightScore = selectedScoreRight
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = Color(0xFF005749)
                                        )
                                    )
                                    Text(text = "(0) Severe: Unable.", fontSize = 14.sp)
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = selectedScoreRight == 1,
                                        onClick = {
                                            selectedScoreRight = 1
                                            rightScore = selectedScoreRight
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = Color(0xFF005749)
                                        )
                                    )
                                    Text(text = "(1) Moderate: < 20 s.", fontSize = 14.sp)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = selectedScoreRight == 2,
                                        onClick = {
                                            selectedScoreRight = 2
                                            rightScore = selectedScoreRight
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = Color(0xFF005749)
                                        )
                                    )
                                    Text(text = "(2) Normal: 20 s.", fontSize = 14.sp)
                                }
                            }
                        }
                        Text("To score each side separately use the trial with the longest time.\n" +
                                "To calculate the sub-score and total score use the side [left or right] with the lowest numerical score [i.e. the worse side].",
                            fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(12.dp))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            else if(index == 5){
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White // Set the card's background color
                        )
                    ){
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "6. COMPENSATORY STEPPING CORRECTION- LATERAL",
                                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            )

                            Text(
                                text = "Instruction: Stand with your feet together, arms down at your sides. Lean into my hand beyond your sideways limit. When I let go, do whatever is necessary, including taking a step, to avoid a fall.",
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth()
                        ){
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(text = "Left: ", fontSize = 16.sp, modifier = Modifier.padding(start = 12.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = selectedScore6 == 0,
                                        onClick = {
                                            selectedScore6 = 0
                                            leftScore6 = selectedScore6
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = Color(0xFF005749)
                                        )

                                    )
                                    Text(text = "Severe (0): Falls, or cannot step.", fontSize = 14.sp)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = selectedScore6 == 1,
                                        onClick = {
                                            selectedScore6 = 1
                                            leftScore6 = selectedScore6
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = Color(0xFF005749)
                                        )
                                    )
                                    Text(text = "Moderate (1): Several steps to recover equilibrium.", fontSize = 14.sp)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = selectedScore6 == 2,
                                        onClick = {
                                            selectedScore6 = 2
                                            leftScore6 = selectedScore6
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = Color(0xFF005749)
                                        )
                                    )
                                    Text(text = "Normal (2): Recovers independently with 1 step\n (crossover or lateral OK).", fontSize = 14.sp)
                                }
                            }
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(text = "Right: ", fontSize = 16.sp, modifier = Modifier.padding(start = 12.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = selectedScoreRight6 == 0,
                                        onClick = {
                                            selectedScoreRight6 = 0
                                            rightScore6 = selectedScoreRight6
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = Color(0xFF005749)
                                        )
                                    )
                                    Text(text = "Severe (0): Falls, or cannot step.", fontSize = 14.sp)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = selectedScoreRight6 == 1,
                                        onClick = {
                                            selectedScoreRight6 = 1
                                            rightScore6 = selectedScoreRight6
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = Color(0xFF005749)
                                        )
                                    )
                                    Text(text = "Moderate (1): Several steps to recover equilibrium.", fontSize = 14.sp)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = selectedScoreRight6 == 2,
                                        onClick = {
                                            selectedScoreRight6 = 2
                                            rightScore6 = selectedScoreRight6
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = Color(0xFF005749)
                                        )
                                    )
                                    Text(text = "Normal (2): Recovers independently with 1 step (crossover or lateral OK).", fontSize = 14.sp)
                                }
                            }
                        }
                        Text(
                            text = "Use the side with the lowest score to calculate sub-score and total score.",
                            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            else if(index == 6){
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White // Set the card's background color
                        )
                    ){
                        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                            Text(
                                text = "${index + 1}. $question",
                                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Instruction: $instruction",
                                style = TextStyle(fontSize = 14.sp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text("Time in Seconds: ", style = TextStyle(fontSize = 14.sp), fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.width(4.dp))
                                BasicTextField(
                                    value = question8sec,
                                    onValueChange = {
                                        if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                                            trial1Time1 = it
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done // Handles "Done" action
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            focusManager.clearFocus() // Dismiss the keyboard when "Done" is pressed
                                        }
                                    ),
                                    textStyle = TextStyle(
                                        fontSize = 12.sp,
                                        color = Color.Black
                                    ),
                                    singleLine = true,
                                    modifier = Modifier
                                        .width(30.dp)
                                        .background(Color.Transparent)
                                        .padding(4.dp)
                                        .focusRequester(focusRequester)
                                        .drawBehind {
                                            val strokeWidth = 1.dp.toPx()
                                            val y = size.height - strokeWidth / 2
                                            drawLine(
                                                color = Color.Black,
                                                start = Offset(0f, y),
                                                end = Offset(size.width, y),
                                                strokeWidth = strokeWidth
                                            )
                                        }
                                        .clickable { focusRequester.requestFocus() }
                                )
                                Text("Sec", style = TextStyle(fontSize = 14.sp), fontWeight = FontWeight.SemiBold)
                            }
                        }
                        Column{
                            options.take(7).forEachIndexed { optionIndex, option ->
                                Row(
                                    // modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = answers[index] == optionIndex,
                                        onClick = {
                                            answers = answers.toMutableList().apply { set(index, optionIndex) }
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = Color(0xFF005749)
                                        )
                                    )
                                    Text(text = option, style = TextStyle(fontSize = 14.sp))
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            else if(index == 7){
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White // Set the card's background color
                        )
                    ){
                        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                            Text(
                                text = "${index + 1}. $question",
                                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Instruction: $instruction",
                                style = TextStyle(fontSize = 14.sp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text("Time in Seconds: ", style = TextStyle(fontSize = 14.sp), fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.width(4.dp))
                                BasicTextField(
                                    value = question8sec,
                                    onValueChange = {
                                        if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                                            trial1Time1 = it
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done // Handles "Done" action
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            focusManager.clearFocus() // Dismiss the keyboard when "Done" is pressed
                                        }
                                    ),
                                    textStyle = TextStyle(
                                        fontSize = 12.sp,
                                        color = Color.Black
                                    ),
                                    singleLine = true,
                                    modifier = Modifier
                                        .width(30.dp)
                                        .background(Color.Transparent)
                                        .padding(4.dp)
                                        .focusRequester(focusRequester)
                                        .drawBehind {
                                            val strokeWidth = 1.dp.toPx()
                                            val y = size.height - strokeWidth / 2
                                            drawLine(
                                                color = Color.Black,
                                                start = Offset(0f, y),
                                                end = Offset(size.width, y),
                                                strokeWidth = strokeWidth
                                            )
                                        }
                                        .clickable { focusRequester.requestFocus() }
                                )
                                Text("Sec", style = TextStyle(fontSize = 14.sp), fontWeight = FontWeight.SemiBold)
                            }
                        }
                        Column{
                            options.take(8).forEachIndexed { optionIndex, option ->
                                Row(
                                    // modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = answers[index] == optionIndex,
                                        onClick = {
                                            answers = answers.toMutableList().apply { set(index, optionIndex) }
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = Color(0xFF005749)
                                        )
                                    )
                                    Text(text = option, style = TextStyle(fontSize = 14.sp))
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            else if(index == 8){
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White // Set the card's background color
                        )
                    ){
                        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                            Text(
                                text = "${index + 1}. $question",
                                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Instruction: $instruction",
                                style = TextStyle(fontSize = 14.sp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text("Time in Seconds: ", style = TextStyle(fontSize = 14.sp), fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.width(4.dp))
                                BasicTextField(
                                    value = trial1Time1,
                                    onValueChange = {
                                        if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                                            trial1Time1 = it
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done // Handles "Done" action
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            focusManager.clearFocus() // Dismiss the keyboard when "Done" is pressed
                                        }
                                    ),
                                    textStyle = TextStyle(
                                        fontSize = 12.sp,
                                        color = Color.Black
                                    ),
                                    singleLine = true,
                                    modifier = Modifier
                                        .width(30.dp)
                                        .background(Color.Transparent)
                                        .padding(4.dp)
                                        .focusRequester(focusRequester)
                                        .drawBehind {
                                            val strokeWidth = 1.dp.toPx()
                                            val y = size.height - strokeWidth / 2
                                            drawLine(
                                                color = Color.Black,
                                                start = Offset(0f, y),
                                                end = Offset(size.width, y),
                                                strokeWidth = strokeWidth
                                            )
                                        }
                                        .clickable { focusRequester.requestFocus() }
                                )
                                Text("Sec", style = TextStyle(fontSize = 14.sp), fontWeight = FontWeight.SemiBold)
                            }
                        }
                        Column{
                            options.take(9).forEachIndexed { optionIndex, option ->
                                Row(
                                    // modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = answers[index] == optionIndex,
                                        onClick = {
                                            answers = answers.toMutableList().apply { set(index, optionIndex) }
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = Color(0xFF005749)
                                        )
                                    )
                                    Text(text = option, style = TextStyle(fontSize = 14.sp))
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            else if(index == 13){
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White // Set the card's background color
                        )
                    ){
                        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                            Text(
                                text = "${index + 1}. $question",
                                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Column{
                                Text("Instruction TUG: “When I say ‘Go’, stand up from chair, walk at your normal speed across the tape on the floor, turn around, and come back to sit in the chair. Instruction TUG with Dual Task: “Count backwards by threes starting at")
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.Bottom
                                ){
                                    Text("Instruction TUG with Dual Task:")
                                    Text("Count backwards by threes starting at: ", fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.width(2.dp))
                                    BasicTextField(
                                        value = trial1Time1,
                                        onValueChange = {
                                            if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                                                trial1Time1 = it
                                            }
                                        },
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            keyboardType = KeyboardType.Number,
                                            imeAction = ImeAction.Done // Handles "Done" action
                                        ),
                                        keyboardActions = KeyboardActions(
                                            onDone = {
                                                focusManager.clearFocus() // Dismiss the keyboard when "Done" is pressed
                                            }
                                        ),
                                        textStyle = TextStyle(
                                            fontSize = 12.sp,
                                            color = Color.Black
                                        ),
                                        singleLine = true,
                                        modifier = Modifier
                                            .width(30.dp)
                                            .background(Color.Transparent)
                                            .padding(4.dp)
                                            .focusRequester(focusRequester)
                                            .drawBehind {
                                                val strokeWidth = 1.dp.toPx()
                                                val y = size.height - strokeWidth / 2
                                                drawLine(
                                                    color = Color.Black,
                                                    start = Offset(0f, y),
                                                    end = Offset(size.width, y),
                                                    strokeWidth = strokeWidth
                                                )
                                            }
                                            .clickable { focusRequester.requestFocus() }
                                    )
                                }
                                Text("When I say ‘Go’, stand up from chair, walk at your normal speed across the tape on the floor, turn around, and come back to sit in the chair. Continue counting backwards the entire time.")
                            }
                            Column(
                            ){
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.Bottom
                                ){
                                    Text(
                                        text = "TUG:",
                                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    BasicTextField(
                                        value = trial1Time1,
                                        onValueChange = {
                                            if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                                                trial1Time1 = it
                                            }
                                        },
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            keyboardType = KeyboardType.Number,
                                            imeAction = ImeAction.Done // Handles "Done" action
                                        ),
                                        keyboardActions = KeyboardActions(
                                            onDone = {
                                                focusManager.clearFocus() // Dismiss the keyboard when "Done" is pressed
                                            }
                                        ),
                                        textStyle = TextStyle(
                                            fontSize = 12.sp,
                                            color = Color.Black
                                        ),
                                        singleLine = true,
                                        modifier = Modifier
                                            .width(30.dp)
                                            .background(Color.Transparent)
                                            .padding(4.dp)
                                            .focusRequester(focusRequester)
                                            .drawBehind {
                                                val strokeWidth = 1.dp.toPx()
                                                val y = size.height - strokeWidth / 2
                                                drawLine(
                                                    color = Color.Black,
                                                    start = Offset(0f, y),
                                                    end = Offset(size.width, y),
                                                    strokeWidth = strokeWidth
                                                )
                                            }
                                            .clickable { focusRequester.requestFocus() }
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Seconds:",
                                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Dual Task TUG: ", style = TextStyle(fontSize = 14.sp),fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    BasicTextField(
                                        value = trial1Time1,
                                        onValueChange = {
                                            if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                                                trial1Time1 = it
                                            }
                                        },
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            keyboardType = KeyboardType.Number,
                                            imeAction = ImeAction.Done // Handles "Done" action
                                        ),
                                        keyboardActions = KeyboardActions(
                                            onDone = {
                                                focusManager.clearFocus() // Dismiss the keyboard when "Done" is pressed
                                            }
                                        ),
                                        textStyle = TextStyle(
                                            fontSize = 12.sp,
                                            color = Color.Black
                                        ),
                                        singleLine = true,
                                        modifier = Modifier
                                            .width(30.dp)
                                            .background(Color.Transparent)
                                            .padding(4.dp)
                                            .focusRequester(focusRequester)
                                            .drawBehind {
                                                val strokeWidth = 1.dp.toPx()
                                                val y = size.height - strokeWidth / 2
                                                drawLine(
                                                    color = Color.Black,
                                                    start = Offset(0f, y),
                                                    end = Offset(size.width, y),
                                                    strokeWidth = strokeWidth
                                                )
                                            }
                                            .clickable { focusRequester.requestFocus() }
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Seconds", style = TextStyle(fontSize = 14.sp), fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ){
                            Column{
                                options.take(13).forEachIndexed { optionIndex, option ->
                                    Row(
                                        // modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = answers[index] == optionIndex,
                                            onClick = {
                                                answers = answers.toMutableList().apply { set(index, optionIndex) }
                                            },
                                            colors = RadioButtonDefaults.colors(
                                                selectedColor = Color(0xFF005749)
                                            )
                                        )
                                        Text(text = option, style = TextStyle(fontSize = 14.sp))
                                    }
                                }
                            }
                        }
                        Text(
                            text = "When scoring item 14, if subject’s gait speed slows more than 10% between the TUG without and with a Dual Task the score should be decreased by a point.",
                            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            else {
                item {
                    QuestionCardMinibest(
                        question = "${index + 1}. $question",
                        instruction = instruction,
                        options = options,
                        selectedOption = answers[index],
                        onOptionSelected = { selectedAnswer ->
                            answers = answers.toMutableList().apply { set(index, selectedAnswer) }
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }

        item{
            Spacer(modifier = Modifier.height(6.dp))
        }
        item{
            Text(
                text = "Total Score:  $totalScore/28",
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )
        }
        item{
            Spacer(modifier = Modifier.height(6.dp))
        }
        item{
            Button(
                onClick = {
                    if (selectedOption.isEmpty()) {
                        Toast.makeText(context, "Please select a patient", Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    isSubmitted = true
                    val result = MiniBestResult(
                        totalScore = totalScore,
                        anticipatory = anticipatory,
                        reactive_postural_control = reactive_postural_control,
                        sensory_orientation = sensory_orientation,
                        dynamic_gait = dynamic_gait
                    )
                    val dateFormatter = SimpleDateFormat("dd MMM yy - hh.mm a", Locale.getDefault())
                    val currentTimestamp = dateFormatter.format(System.currentTimeMillis())
                    val pdfName = "${patient.name} - $currentTimestamp.pdf"
                    addedMiniData(patient = patient, result = result)
                    pdfFile = savePdfToDocumentsUsingMediaStoreMinibest(result, pdfName, patient, context)
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color(0xFF005749), // Normal state color
                    contentColor = Color.White, // Normal text color
                ),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
            ) {
                Text("Generate Report")
            }
        }
        item{
            Spacer(modifier = Modifier.height(8.dp))
        }
        item{
            pdfFile?.let { file ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    // horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Button(
                        onClick = {
                            val uri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.provider", file
                            )
                            pdfFile?.let { file ->
                                val shareIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    type = "application/pdf"
                                    putExtra(Intent.EXTRA_STREAM, uri) // Use the URI from MediaStore
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                shareLauncher.launch(Intent.createChooser(shareIntent, "Share PDF"))
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = Color(0xFF005749), // Normal state color
                            contentColor = Color.White, // Normal text color
                        ),
                        modifier = Modifier.fillMaxWidth(0.5f)
                    ) {
                        Text("Share PDF")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = {
                            pdfFile?.let { file ->
                                navController.navigate("PreviewPdfScreen/${Uri.encode(file.path)}")
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = Color(0xFF005749), // Normal state color
                            contentColor = Color.White, // Normal text color
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Preview PDF")
                    }
                }
            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.Q)
fun savePdfToDocumentsUsingMediaStoreMinibest(content: MiniBestResult, patientName: String, patientDetail: Patient, context: Context): File? {
    val resolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, patientName)
        put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
    }

    val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
    if (uri != null) {
        resolver.openOutputStream(uri)?.use { outputStream ->
            val writer = PdfWriter(outputStream)
            val pdfDoc = PdfDocument(writer)
            val document = Document(pdfDoc)
            val title = Paragraph("Assessment Report")
                .setFontSize(18f)
                .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.LEFT)
                .setBold()
            val logoBitmap = getLogoBitmap(context) // Get your logo as Bitmap
            if (logoBitmap != null) {
                val logoStream = logoBitmapToByteArray(logoBitmap)
                val imageData = com.itextpdf.io.image.ImageDataFactory.create(logoStream)
                val image = com.itextpdf.layout.element.Image(imageData).setHeight(50f)
                document.add(image)
            }
            document.add(title)
            val nameParagraph = Paragraph("Name: ")
                .setBold()
                .add(Paragraph(patientDetail.name?.toString() ?: "N/A").setFontSize(12f)) // Detail without bold
                .setFontSize(12f)

            val ageParagraph = Paragraph("Age: ")
                .setBold()
                .add(Paragraph(patientDetail.age?.toString() ?: "N/A").setFontSize(12f)) // Detail without bold
                .setFontSize(12f)

            val genderParagraph = Paragraph("Gender: ")
                .setBold()
                .add(Paragraph(patientDetail.gender ?: "N/A").setFontSize(12f)) // Detail without bold
                .setFontSize(12f)

            document.add(nameParagraph)
            document.add(ageParagraph)
            document.add(genderParagraph)
            val bulletList = com.itextpdf.layout.element.List()
                .setSymbolIndent(12f)
                .setFontSize(12f)

            bulletList.add(ListItem("Anticipatory: ${content.anticipatory}"))
            bulletList.add(ListItem("Reactive postural control: ${content.reactive_postural_control}"))
            bulletList.add(ListItem("Sensory orientation: ${content.sensory_orientation}"))
            bulletList.add(ListItem("Dynamic gait: ${content.dynamic_gait}"))
            val table2 = Table(floatArrayOf(3f, 3f, 4f)).setMarginBottom(20f) // Three columns: Scale, Normative Value, Report
            table2.setWidth(UnitValue.createPercentValue(100f)) // Table width is 100% of the page
            val headerBackgroundColor = com.itextpdf.kernel.colors.DeviceRgb(0, 87, 73)
            val fontBackgroundColor = com.itextpdf.kernel.colors.ColorConstants.WHITE

            val scaleHeaderCell = Cell().add(Paragraph("Scale").setBold())
            scaleHeaderCell.setBackgroundColor(headerBackgroundColor).setFontColor(fontBackgroundColor)
            table2.addCell(scaleHeaderCell)

            val normativeValueHeaderCell = Cell().add(Paragraph("Session Score").setBold())
            normativeValueHeaderCell.setBackgroundColor(headerBackgroundColor).setFontColor(fontBackgroundColor)
            table2.addCell(normativeValueHeaderCell)

            val reportHeaderCell = Cell().add(Paragraph("Normative Value").setBold())
            reportHeaderCell.setBackgroundColor(headerBackgroundColor).setFontColor(fontBackgroundColor)
            table2.addCell(reportHeaderCell)
            table2.addCell("Minibesttest")
            table2.addCell(content.totalScore.toString())
            table2.addCell("16-28")
            val summaryTitle = Paragraph("Summary")
                .setFontSize(12f)
                .setBold()
                .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.LEFT)
                .setMarginTop(5f)
            val Interpretation = Paragraph("Interpretation of Mini-Best scoring on different measurements used:")
                .setFontSize(12f)
                .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.LEFT)
                .setMarginTop(5f)
                .setBold()
            document.add(table2)
            document.add(Interpretation)
            document.add(bulletList)
            document.add(summaryTitle)
            document.close()
        }

        val tempFile = File(context.cacheDir, patientName)
        resolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(tempFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return tempFile
    }
    return null
}

@Composable
fun QuestionCardMinibest(
    question: String,
    instruction: String,
    options: List<String>,
    selectedOption: Int,
    onOptionSelected: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White // Set the card's background color
        )
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = question, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Instruction: $instruction", fontSize = 14.sp)
        }
        options.chunked(4).forEach { optionPair ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                optionPair.forEachIndexed { index, option ->
                    OptionRowminibest(
                        option = option,
                        isSelected = selectedOption == index,
                        onClick = { onOptionSelected(index) }
                    )
                }
            }
        }
    }
}
@Composable
fun OptionRowminibest(option: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .clickable { onClick() },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF005749)
            )
        )
        Text(text = option, fontSize = 14.sp)
    }
}