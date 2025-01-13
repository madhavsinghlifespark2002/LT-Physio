package com.lifesparktech.lsphysio.android.pages
import TimeInputSection
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
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
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.UnitValue
import com.lifesparktech.lsphysio.android.Controller.addedGasData
import com.lifesparktech.lsphysio.android.Controller.fetchPatients
import com.lifesparktech.lsphysio.android.data.GASResult
import com.lifesparktech.lsphysio.android.data.Patient
import org.bouncycastle.math.raw.Mod
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
    val questions = listOf(
        "SIT TO STAND",
        "RISE TO TOES",
        "STAND ON ONE LEG",
        "COMPENSATORY STEPPING CORRECTION- FORWARD",
        "COMPENSATORY STEPPING CORRECTION- BACKWARD",
        "COMPENSATORY STEPPING CORRECTION- LATERAL",
        "STANCE (FEET TOGETHER); EYES OPEN, FIRM SURFACE",
        "STANCE (FEET TOGETHER); EYES CLOSED, FOAM SURFACE",
        "INCLINE- EYES CLOSED",
        "CHANGE IN GAIT SPEED",
        "WALK WITH HEAD TURNS – HORIZONTAL",
        "WALK WITH PIVOT TURNS",
        "STEP OVER OBSTACLES",
        "TIMED UP & GO WITH DUAL TASK [3 METER WALK]"
    )
    val instructions = listOf(
        "Cross your arms across your chest. Try not to use your hands unless you must. Do not let your legs lean against the back of the chair when you stand. Please stand up now.",
        "Place your feet shoulder width apart. Place your hands on your hips. Try to rise as high as you can onto your toes. I will count out loud to 3 seconds. Try to hold this pose for at least 3 seconds. Look straight ahead. Rise now.",
        "Look straight ahead. Keep your hands on your hips. Lift your leg off of the ground behind you without touching or resting your raised leg upon your other standing leg. Stay standing on one leg as long as you can. Look straight ahead. Lift now.",
        "Stand with your feet shoulder width apart, arms at your sides. Lean forward against my hands beyond your forward limits. When I let go, do whatever is necessary, including taking a step, to avoid a fall.",
        "Stand with your feet shoulder width apart, arms at your sides. Lean backward against my hands beyond your backward limits. When I let go, do whatever is necessary, including taking a step, to avoid a fall.",
        "Stand with your feet together, arms down at your sides. Lean into my hand beyond your sideways limit. When I let go, do whatever is necessary, including taking a step, to avoid a fall.",
        "Place your hands on your hips. Place your feet together until almost touching. Look straight ahead. Be as stable and still as possible, until I say stop.",
        "Step onto the foam. Place your hands on your hips. Place your feet together until almost touching. Be as stable and still as possible, until I say stop. I will start timing when you close your eyes.",
        "Step onto the incline ramp. Please stand on the incline ramp with your toes toward the top. Place your feet shoulder width apart and have your arms down at your sides. I will start timing when you close your eyes.",
        "Begin walking at your normal speed, when I tell you ‘fast’, walk as fast as you can. When I say ‘slow’, walk very slowly.",
        "Begin walking at your normal speed, when I say “right”, turn your head and look to the right. When I say “left turn your head and look to the left. Try to keep yourself walking in a straight line.",
        "Begin walking at your normal speed. When I tell you to ‘turn and stop’, turn as quickly as you can, face the opposite direction, and stop. After the turn, your feet should be close together.",
        "Begin walking at your normal speed. When you get to the box, step over it, not around it and keep walking.",
        "When I say ‘Go’, stand up from chair, walk at your normal speed across the tape on the floor, turn around," +
                "and come back to sit in the chair.\n" +
                "Instruction TUG with Dual Task: “Count backwards by threes starting at ___. When I say ‘Go’, stand up from chair, walk at" +
                "your normal speed across the tape on the floor, turn around, and come back to sit in the chair. Continue counting backwards" +
                "the entire time."
    )
    val sections = listOf(
        Pair(0, "ANTICIPATORY" to "/6"),
        Pair(3, "REACTIVE POSTURAL CONTROL" to "/6"),
        Pair(6, "SENSORY ORIENTATION" to "/6"),
        Pair(9, "DYNAMIC GAIT" to "/10")
    )
    var answers by remember { mutableStateOf(List(questions.size) { 0 }) }

    var trial1Time by remember { mutableStateOf("") }
    var trial2Time by remember { mutableStateOf("") }
    val totalScore = answers.sum()
    var isSubmitted by remember { mutableStateOf(false) }
    var leftScore by remember { mutableStateOf(0) }
    var rightScore by remember { mutableStateOf(0) }
    val subscores = sections.map { (startIndex, _) ->
        if (startIndex == 0) { // Special case for "Stand on One Leg"
//            val endIndex = startIndex + 3 // Each section contains 3 questions
            val endIndex = startIndex + 2 // Each section contains 3 questions
            answers.subList(startIndex, endIndex).sum() + minOf(leftScore, rightScore)
//            answers.subList(startIndex, endIndex - 1).sum() + answers[startIndex] + answers[startIndex + 1] + minOf(leftScore, rightScore)
        } else {
            val endIndex = startIndex + 3 // Each section contains 3 questions
            answers.subList(startIndex, endIndex).sum()
        }
    }
    val optionsList = listOf(
        listOf(
            "Severe (0): Unable to stand up from chair without assistance, OR needs several attempts with use of hands.",
            "Moderate (1): Comes to stand WITH use of hands on first attempt.",
            "Normal (2): Comes to stand without use of hands and stabilizes independently."
        ),
        listOf(
            "Severe (0): < 3 s.",
            "Moderate (1): Heels up, but not full range (smaller than when holding hands), OR noticeable instability for 3 s.",
            "Normal (2): Stable for 3 s with maximum height."
        ),
        listOf(
            "Severe (0): Unable",
            "Moderate (1): < 20 s.",
            "Normal (2): 20 s.",
        ),
        listOf(
            "Severe (0): No step, OR would fall if not caught, OR falls spontaneously.",
            "Moderate (1): More than one step used to recover equilibrium.",
            "Normal (2): Recovers independently with a single, large step (second realignment step is allowed)."
        ),
        listOf(
            "Severe (0): No step, OR would fall if not caught, OR falls spontaneously.",
            "Moderate (1): More than one step used to recover equilibrium.",
            "Normal (2): Recovers independently with a single, large step."
        ),
        listOf(
            "Severe (0): Falls, or cannot step.",
            "Moderate (1): Several steps to recover equilibrium.",
            "Normal (2): Recovers independently with 1 step (crossover or lateral OK)."
        ),
        listOf(
            "Severe (0): Unable.",
            "Moderate (1): < 30 s.",
            "Normal (2): 30 s."
        ),
        listOf(
            "Severe (0): Unable.",
            "Moderate (1): < 30 s.",
            "Normal (2): 30 s."
        ),
        listOf(
            "Severe (0): Unable.",
            "Moderate (1): Stands independently <30 s OR aligns with surface.",
            "Normal (2): Stands independently 30 s and aligns with gravity."
        ),
        listOf(
            "Severe (0): Unable to achieve significant change in walking speed AND signs of imbalance.",
            "Moderate (1): Unable to change walking speed or signs of imbalance.",
            "Normal (2): Significantly changes walking speed without imbalance."
        ),
        listOf(
            "Severe (0): Performs head turns with imbalance.",
            "Moderate (1): Performs head turns with reduction in gait speed.",
            "Normal (2): Performs head turns with no change in gait speed and good balance."
        ),
        listOf(
            "Severe (0): Cannot turn with feet close at any speed without imbalance.",
            "Moderate (1): Turns with feet close SLOW (>4 steps) with good balance.",
            "Normal (2): Turns with feet close FAST (< 3 steps) with good balance."
        ),
        listOf(
            "Severe (0): Unable to step over box OR steps around box.",
            "Moderate (1): Steps over box but touches box OR displays cautious behavior by slowing gait.",
            "Normal (2): Able to step over box with minimal change of gait speed and with good balance."
        ),
        listOf(
            "Severe (0): Stops counting while walking OR stops walking while counting.",
            "Moderate (1): Dual Task affects either counting OR walking (>10%) when compared to the TUG without Dual Task.",
            "Normal (2): No noticeable change in sitting, standing or walking while backward counting when compared to TUG without Dual Task."
        ),
    )
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
                        answers = List(30) { 0 }
                        isSubmitted = false
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
        questions.zip(instructions).zip(optionsList).forEachIndexed { index, (questionAndInstruction, options) ->
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
                // Special case for the "Stand on One Leg" question
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Question 3: STAND ON ONE LEG",
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = "Instruction: Look straight ahead. Keep your hands on your hips. Lift your leg off of the ground behind you without touching or resting your raised leg upon your other standing leg. Stay standing on one leg as long as you can. Look straight ahead. Lift now.",
                            fontSize = 14.sp
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Bottom
                        ){
                            Text(
                                text = "Left:",
                                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Time in Seconds Trial 1: ", style = TextStyle(fontSize = 16.sp))
                            Spacer(modifier = Modifier.width(12.dp))
                            TextField(
                                value = trial1Time,
                                onValueChange = {
                                    if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                                        trial1Time = it }
                                },
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
                                modifier = Modifier.width(100.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Trial 2: ", style = TextStyle(fontSize = 16.sp))
                            Spacer(modifier = Modifier.width(12.dp))
                            TextField(
                                value = trial2Time,
                                onValueChange = {
                                    if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                                        trial2Time = it }
                                },
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
                                modifier = Modifier.width(75.dp)
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Bottom
                        ){
                            Text(
                                text = "Right:",
                                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Time in Seconds Trial 1: ", style = TextStyle(fontSize = 16.sp))
                            Spacer(modifier = Modifier.width(12.dp))
                            TextField(
                                value = trial1Time,
                                onValueChange = {
                                    if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                                        trial1Time = it
                                    } },
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
                                modifier = Modifier.width(100.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Trial 2: ", style = TextStyle(fontSize = 16.sp))
                            Spacer(modifier = Modifier.width(12.dp))
                            TextField(
                                value = trial2Time,
                                onValueChange = {
                                    if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                                        trial2Time = it
                                    }},
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
                                modifier = Modifier.width(75.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ){
                        TimeInputSection("Left") {
                            leftScore = it
                        }

                        TimeInputSection("Right") {
                            rightScore = it
                        }
                    }
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
                        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                            Text(
                                text = "${index + 1}. $question",
                                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = instruction,
                                style = TextStyle(fontSize = 16.sp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Column{
                                Text(
                                    text = "Left:",
                                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                )
                                options.take(6).forEachIndexed { optionIndex, option ->
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
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = option, style = TextStyle(fontSize = 16.sp))
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.width(40.dp))
                            Column{
                                Text(
                                    text = "Right:",
                                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                )
                                options.take(6).forEachIndexed { optionIndex, option ->
                                    Row(
                                        //   modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = answers[index + 1] == optionIndex,
                                            onClick = {
                                                answers = answers.toMutableList().apply { set(index + 1, optionIndex) }
                                            },
                                            colors = RadioButtonDefaults.colors(
                                                selectedColor = Color(0xFF005749)
                                            )
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = option, style = TextStyle(fontSize = 16.sp))
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Use the side with the lowest score to calculate sub-score and total score.",
                                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            else if(index ==8){
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
                                text = instruction,
                                style = TextStyle(fontSize = 16.sp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Bottom
                            ){
                                Text("Time in Seconds: ", style = TextStyle(fontSize = 16.sp))
                                Spacer(modifier = Modifier.width(12.dp))
                                TextField(
                                    value = trial1Time,
                                    onValueChange = {
                                        if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                                            trial1Time = it }
                                    },
                                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
                                    modifier = Modifier.width(100.dp)
                                )
                            }
                            Column{
                                options.take(9).forEachIndexed { optionIndex, option ->
                                    Row(
                                        // modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.Bottom
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
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = option, style = TextStyle(fontSize = 16.sp))
                                    }
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
                                    Text("Instruction TUG with Dual Task: “Count backwards by threes starting at: ")
                                    Spacer(modifier = Modifier.width(12.dp))
                                    TextField(
                                        value = trial1Time,
                                        onValueChange = {
                                            if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                                                trial1Time = it }
                                        },
                                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                        singleLine = true,
                                        colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
                                        modifier = Modifier.width(100.dp)
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
                                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    TextField(
                                        value = trial1Time,
                                        onValueChange = {
                                            if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                                                trial1Time = it }
                                        },
                                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                        singleLine = true,
                                        textStyle = TextStyle(
                                            fontSize = 16.sp,
                                            textAlign = TextAlign.Start, // Align text horizontally
                                            baselineShift = BaselineShift.Subscript // Align text vertically towards bottom
                                        ),
                                        colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
                                        modifier = Modifier.width(60.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Seconds:",
                                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text("Dual Task TUG: ", style = TextStyle(fontSize = 16.sp),fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    TextField(
                                        value = trial2Time,
                                        onValueChange = {
                                            if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                                                trial2Time = it }
                                        },
                                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                        singleLine = true,
                                        colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
                                        modifier = Modifier.width(75.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text("Seconds", style = TextStyle(fontSize = 16.sp), fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                // horizontalArrangement = Arrangement.SpaceBetween
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
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(text = option, style = TextStyle(fontSize = 16.sp))
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "When scoring item 14, if subject’s gait speed slows more than 10% between the TUG without and with a Dual Task the score should be decreased by a point.",
                                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            else {
                // Default rendering for other questions
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
                .padding(12.dp)
        ) {
            Text(text = question, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Instruction: $instruction", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(6.dp))
            options.chunked(4).forEach { optionPair ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    //  horizontalArrangement = Arrangement.SpaceBetween
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
}

@Composable
fun OptionRowminibest(option: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF005749)
            )
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(text = option, style = MaterialTheme.typography.bodyLarge)
    }
}