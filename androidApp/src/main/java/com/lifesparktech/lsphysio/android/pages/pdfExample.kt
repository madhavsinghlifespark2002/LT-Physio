package com.lifesparktech.lsphysio.android.pages

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfGeneratorScreen(onPreviewPdf: (File) -> Unit, navController: NavController) {
    var text by remember { mutableStateOf("") }
    var pdfFile by remember { mutableStateOf<File?>(null) }
    val context = LocalContext.current
    val shareLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {}
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "PDF Generator",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Enter Text") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        val result = Result(
            totalScore = 95,
            somaticScore = 30,
            cognitiveScore = 25,
            affectiveScore = 20,
            anxietyCategory = "Severe"
        )
        val reportContent = """
            Total Score: ${result.totalScore}
            Somatic Subscale: ${result.somaticScore}
            Cognitive Subscale: ${result.cognitiveScore}
            Affective Subscale: ${result.affectiveScore}
            Anxiety Category: ${result.anxietyCategory}
        """.trimIndent()
        Button(
            onClick = {
                pdfFile = savePdfToDocumentsUsingMediaStore(reportContent, context)
            },
            enabled = text.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Generate PDF")
        }
        Spacer(modifier = Modifier.height(16.dp))
        pdfFile?.let { file ->
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
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Share PDF")
            }
            Spacer(modifier = Modifier.height(16.dp))
            pdfFile?.let { file ->
                Button(
                    onClick = {
                        pdfFile?.let { file ->
                            navController.navigate("PreviewPdfScreen/${Uri.encode(file.path)}")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Preview PDF")
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
@Composable
fun PreviewPdfScreen(pdfFile: File) {
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
            modifier = Modifier.border(width = 1.dp, color = Color.Black).height(400.dp)
        ){
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
}
@RequiresApi(Build.VERSION_CODES.Q)
fun savePdfToDocumentsUsingMediaStore(content: String, context: Context): File? {
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
                .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                .setBold()
            document.add(title)
            val logoBitmap = getLogoBitmap(context) // Get your logo as Bitmap
            if (logoBitmap != null) {
                val logoStream = logoBitmapToByteArray(logoBitmap)
                val imageData = com.itextpdf.io.image.ImageDataFactory.create(logoStream)
                val image = com.itextpdf.layout.element.Image(imageData)
                //.(100f) // Adjust width
                //.setHeight(50f) // Adjust height
                // .setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER)
                document.add(image)
            }
            val paragraph = Paragraph(content).setFontSize(12f)
            // document.add()
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

data class Result(
    val totalScore: Int,
    val somaticScore: Int,
    val cognitiveScore: Int,
    val affectiveScore: Int,
    val anxietyCategory: String
)