package com.lifesparktech.lsphysio.android.pages
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.lifesparktech.lsphysio.android.Controller.addedGasData
import com.lifesparktech.lsphysio.android.Controller.fetchPatients
import com.lifesparktech.lsphysio.android.data.GASResult
import com.lifesparktech.lsphysio.android.data.Patient
import java.io.File
import java.io.FileOutputStream
import com.github.barteksc.pdfviewer.PDFView

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun GasScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFf4f4f4)),
    ) {
        GasCard(navController)
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GasCard(navController: NavController){
    val patients = remember { mutableStateOf<List<Patient>>(emptyList()) }
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("") }
    var patient by remember { mutableStateOf(Patient()) }
    var context = LocalContext.current
    var pdfFile by remember { mutableStateOf<File?>(null) }
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
        "My heart raced or beat strongly.",
        "My breath was short.",
        "I had an upset stomach.",
        "I felt like things were not real or like I was outside of myself.",
        "I felt like I was losing control.",
        "I was afraid of being judged by others.",
        "I was afraid of being humiliated or embarrassed.",
        "I had difficulty falling asleep.",
        "I had difficulty staying asleep.",
        "I was irritable.",
        "I had outbursts of anger.",
        "I had difficulty concentrating.",
        "I was easily startled or upset.",
        "I was less interested in doing something I typically enjoy.",
        "I felt detached or isolated from others.",
        "I felt like I was in a daze.",
        "I had a hard time sitting still.",
        "I worried too much.",
        "I could not control my worry.",
        "I felt restless, keyed up, or on edge.",
        "I felt tired.",
        "My muscles were tense.",
        "I had back pain, neck pain, or muscle cramps.",
        "I felt like I had no control over my life.",
        "I felt like something terrible was going to happen to me.",
        "I was concerned about my finances.",
        "I was concerned about my health.",
        "I was concerned about my children.",
        "I was afraid of dying.",
        "I was afraid of becoming a burden to my family or children."
    )
    val options = listOf("Not at all (0)", "Sometimes (1)", "Most of the time (2)", "All of the time (3)")
    var answers by remember { mutableStateOf(List(30) { 0 }) }  // Default to 0 for each item
    var totalScore by remember { mutableStateOf(0) }
    var somaticScore by remember { mutableStateOf(0) }
    var cognitiveScore by remember { mutableStateOf(0) }
    var affectiveScore by remember { mutableStateOf(0) }
    var isSubmitted by remember { mutableStateOf(false) }
    fun calculateScores() {
        totalScore = answers.take(25).sum()
        somaticScore = listOf(0, 1, 2, 7, 8, 16, 20, 21, 22).sumOf { answers[it] }
        cognitiveScore = listOf(3, 4, 11, 15, 17, 18, 23, 24).sumOf { answers[it] }
        affectiveScore = listOf(5, 6, 9, 10, 12, 13, 14, 19).sumOf { answers[it] }
    }
    fun getAnxietyCategory(): String {
        return when {
            totalScore in 0..7 -> "Mild Anxiety"
            totalScore in 8..13 -> "Moderate Anxiety"
            totalScore in 14..21 -> "Severe Anxiety"
            totalScore >= 22 -> "Very Severe Anxiety"
            else -> "No Anxiety"  // This is a fallback, just in case
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item{
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = "Geriatric Anxiety Scale (GAS)", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Button(
                    onClick = {
                        answers = List(30) { 0 }
                        isSubmitted = false
                        calculateScores()
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
           Spacer(modifier = Modifier.height(20.dp))
       }
        item{
            Column{
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
        questions.forEachIndexed { index, question ->
            item{
                QuestionCard(
                    question = "${index + 1}. $question",
                    options = options,
                    selectedOption = answers[index],
                    onOptionSelected = { selectedAnswer ->
                        answers = answers.toMutableList().apply { set(index, selectedAnswer) }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        item{
            Spacer(modifier = Modifier.height(8.dp))
        }
        item{
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Button(
                    onClick = {
                        if (selectedOption.isEmpty()) {
                            Toast.makeText(context, "Please select a patient", Toast.LENGTH_LONG).show()
                            return@Button
                        }
                        calculateScores()
                        isSubmitted = true
                        val result = GASResult(
                            totalScore = totalScore,
                            somaticScore = somaticScore,
                            cognitiveScore = cognitiveScore,
                            affectiveScore = affectiveScore,
                            anxietyCategory = getAnxietyCategory()
                        )
                        val result2 = """
                            totalScore = ${totalScore},
                            somaticScore = ${somaticScore},
                            cognitiveScore = ${cognitiveScore},
                            affectiveScore = ${affectiveScore},
                            anxietyCategory = ${getAnxietyCategory()}
                        """.trimIndent()
                        val patientDetail = """
                            Name: ${patient.name},
                            Age: ${patient.age},
                            Sex: = ${patient.gender},
                        """.trimIndent()
                        addedGasData(patient = patient, result = result)
                        pdfFile = savePdfToDocumentsUsingMediaStore(result2, patientDetail, context)
                        navController.currentBackStackEntry?.savedStateHandle?.set("result", result)
                        navController.navigate("gasResultScreen")
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = Color(0xFF005749)
                    )
                ) {
                    Text("Submit", color = Color.White)
                }
            }
        }
    }
}
@Composable
fun QuestionCard(
    question: String,
    options: List<String>,
    selectedOption: Int,
    onOptionSelected: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(125.dp)
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
            Text(text = question, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(10.dp))
            options.chunked(4).forEach { optionPair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    optionPair.forEachIndexed { index, option ->
                        OptionRow(
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
fun OptionRow(option: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = option, style = MaterialTheme.typography.bodyLarge)
    }
}
@Composable
fun GasResultScreen(navController: NavController, result: GASResult, pdfFile: File?) {
    var context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Assessment Result",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Total Score: ${result.totalScore}")
        Text("Somatic Subscale: ${result.somaticScore}")
        Text("Cognitive Subscale: ${result.cognitiveScore}")
        Text("Affective Subscale: ${result.affectiveScore}")
        Text("Anxiety Category: ${result.anxietyCategory}")
        Spacer(modifier = Modifier.height(16.dp))

        pdfFile?.let {
            Button(
                onClick = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("pdfFile", it)
                    navController.navigate("previewPdfScreen")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005749))
            ) {
                Text("Preview PDF", color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    sharePdfFile(context, it)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005749))
            ) {
                Text("Share PDF", color = Color.White)
            }
        } ?: Text("PDF file not available", color = Color.Red)
    }
}
fun sharePdfFile(context: Context, file: File) {
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "application/pdf"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share PDF using"))
}

@RequiresApi(Build.VERSION_CODES.Q)
fun savePdfToDocumentsUsingMediaStore(content: String, patientDetail: String,  context: Context): File? {
    val resolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, "GeneratedReport.pdf")
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
                //.(100f) // Adjust width
                //.setHeight(50f) // Adjust height
                // .setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER)
                document.add(image)
            }
            document.add(title)
            var patientDetail = Paragraph(patientDetail).setFontSize(12f)
            val paragraph = Paragraph(content).setFontSize(12f)
            // document.add()
            document.add(patientDetail)
            document.add(paragraph)
            document.close()
        }
        val tempFile = File(context.cacheDir, "GeneratedReport.pdf")
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
fun PreviewPdfScreen(navController: NavController) {
    val pdfFile = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<File>("pdfFile")

    pdfFile?.let {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(
                text = "PDF Preview",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
            Box(
                modifier = Modifier
                    .border(width = 1.dp, color = Color.Black)
                    .height(400.dp)
            ) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    factory = { context ->
                        PDFView(context, null).apply {
                            fromFile(it).load()
                        }
                    }
                )
            }
        }
    } ?: Text("PDF file not found", color = Color.Red)
}

