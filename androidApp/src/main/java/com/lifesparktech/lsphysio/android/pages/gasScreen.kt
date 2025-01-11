package com.lifesparktech.lsphysio.android.pages
import android.content.ContentValues
import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
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
import java.text.SimpleDateFormat
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GasScreen(onPreviewPdf: (File) -> Unit, navController: NavController) {
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
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item{
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
               Row{
                   Icon(
                       imageVector = Icons.Default.ArrowBack,
                       contentDescription = "Back",
                       modifier = Modifier.clickable{navController.popBackStack() }
                   )
                   Spacer(modifier = Modifier.width(12.dp))
                   Text(text = "Geriatric Anxiety Scale (GAS)", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
               }
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
                    val dateFormatter = SimpleDateFormat("dd MMM yy - hh.mm a", Locale.getDefault())
                    val currentTimestamp = dateFormatter.format(System.currentTimeMillis())
                    val pdfName = "${patient.name} - $currentTimestamp.pdf"
                    addedGasData(patient = patient, result = result)
                    pdfFile = savePdfToDocumentsUsingMediaStore(result, pdfName, patient, context)
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color(0xFF005749), // Normal state color
                    contentColor = Color.White, // Normal text color
                ),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)
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
fun PreviewPdfScreen(pdfFile: File) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            factory = { context ->
                PDFView(context, null).apply {
                    fromFile(pdfFile).load()
                }
            }
        )
    }
}
@RequiresApi(Build.VERSION_CODES.Q)
fun savePdfToDocumentsUsingMediaStore(content: GASResult, patientName: String, patientDetail: Patient, context: Context): File? {
    val resolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, patientName)
        put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
    }
    fun getAnxietyCategory(): String {
        return when {
            content.totalScore in 0..7 -> "Mild Anxiety"
            content.totalScore in 8..13 -> "Moderate Anxiety"
            content.totalScore in 14..21 -> "Severe Anxiety"
            content.totalScore >= 22 -> "Very Severe Anxiety"
            else -> "No Anxiety"  // This is a fallback, just in case
        }
    }
    fun getAnxietyCategoryRange(): String {
        return when {
            content.totalScore in 0..7 -> "0 - 7"
            content.totalScore in 8..13 -> "8 - 13"
            content.totalScore in 14..21 -> "14 - 21"
            content.totalScore >= 22 -> "above 22"
            else -> "No Anxiety"  // This is a fallback, just in case
        }
    }
    val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
    if (uri != null) {
        resolver.openOutputStream(uri)?.use { outputStream ->
            val writer = PdfWriter(outputStream)
            val pdfDoc = PdfDocument(writer)
            val document = Document(pdfDoc)

            // Title
            val title = Paragraph("Assessment Report")
                .setFontSize(18f)
                .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.LEFT)
                .setBold()

            // Add Logo
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

            // Add a Table
            val table = Table(floatArrayOf(2f, 5f)) // Two columns, widths are proportional
            table.setWidth(UnitValue.createPercentValue(100f)) // Table width is 100% of the page

            // Add Table Header
            table.addCell(Paragraph("Category").setBold())
            table.addCell(Paragraph("Score").setBold())

            // Add Rows
            table.addCell("Total Score")
            table.addCell(content.totalScore.toString())
            table.addCell("Somatic Score")
            table.addCell(content.somaticScore.toString())
            table.addCell("Cognitive Score")
            table.addCell(content.somaticScore.toString())
            table.addCell("Affective Score")
            table.addCell(content.somaticScore.toString())
            table.addCell("Anxiety Category")
            table.addCell(getAnxietyCategory())

            // Add Table to Document


            // Add Assessment Details
//            val paragraph = Paragraph(content).setFontSize(12f)
//            document.add(paragraph)
            val table2 = Table(floatArrayOf(3f, 3f, 4f)).setMarginBottom(20f) // Three columns: Scale, Normative Value, Report
            table2.setWidth(UnitValue.createPercentValue(100f)) // Table width is 100% of the page
            val headerBackgroundColor = com.itextpdf.kernel.colors.DeviceRgb(0, 87, 73)
            val fontBackgroundColor = com.itextpdf.kernel.colors.ColorConstants.WHITE

            val scaleHeaderCell = Cell().add(Paragraph("Scale").setBold())
            scaleHeaderCell.setBackgroundColor(headerBackgroundColor).setFontColor(fontBackgroundColor)
            table2.addCell(scaleHeaderCell)

            val normativeValueHeaderCell = Cell().add(Paragraph("Normative Value").setBold())
            normativeValueHeaderCell.setBackgroundColor(headerBackgroundColor).setFontColor(fontBackgroundColor)
            table2.addCell(normativeValueHeaderCell)

            val reportHeaderCell = Cell().add(Paragraph("Report").setBold())
            reportHeaderCell.setBackgroundColor(headerBackgroundColor).setFontColor(fontBackgroundColor)
            table2.addCell(reportHeaderCell)

            // Add Rows (Example Data, adjust as per your logic)
            table2.addCell("GAS")
            table2.addCell(getAnxietyCategoryRange())
            table2.addCell(getAnxietyCategory())
            val summaryTitle = Paragraph("Summary")
                .setFontSize(14f)
                .setBold()
                .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.LEFT)
                .setMarginTop(20f)
            val reference = """
                Segal, D. L., June, A., Payne, M., Coolidge, F. L., & Yochim, B. (2010). Development and initial validation of
                a self-report assessment tool for anxiety among older adults: The Geriatric Anxiety Scale. Journal of Anxiety
                Disorders, 24, 709-714.
            """.trimIndent()

            val referenceParagraph = Paragraph(reference)
                .setFontSize(12f)
                .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.LEFT)
            document.add(table2)
            document.add(table)
            document.add(summaryTitle)
            document.add(referenceParagraph)
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

fun logoBitmapToByteArray(bitmap: android.graphics.Bitmap): ByteArray {
    val stream = java.io.ByteArrayOutputStream()
    bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}
fun getLogoBitmap(context: Context): android.graphics.Bitmap? {
    val logoDrawable = context.getDrawable(R.drawable.logo) // Replace with your logo's name
    return if (logoDrawable is android.graphics.drawable.BitmapDrawable) {
        logoDrawable.bitmap
    } else null
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