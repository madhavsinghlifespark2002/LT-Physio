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
import com.lifesparktech.lsphysio.android.Controller.fetchPatients
import com.lifesparktech.lsphysio.android.data.GASResult
import com.lifesparktech.lsphysio.android.data.MiniBestResult
import com.lifesparktech.lsphysio.android.data.Patient
import com.lifesparktech.lsphysio.android.data.instructions
import com.lifesparktech.lsphysio.android.data.minibestquestions
import com.lifesparktech.lsphysio.android.data.optionsList
import org.bouncycastle.math.raw.Mod
import java.text.SimpleDateFormat
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun sampleTest() {
    val patients = remember { mutableStateOf<List<Patient>>(emptyList()) }
    LaunchedEffect(Unit) {
        try{
            val result = fetchPatients()
            patients.value = result
        }
        finally {
        }
    }
    val sections = listOf(
        Pair(0, "ANTICIPATORY" to "/6"),
        Pair(3, "REACTIVE POSTURAL CONTROL" to "/6"),
        Pair(6, "SENSORY ORIENTATION" to "/6"),
        Pair(9, "DYNAMIC GAIT" to "/10")
    )
    var answers by remember { mutableStateOf(List(minibestquestions.size) { 2 }) }
    var totalScore = answers.sum()
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
                        modifier = Modifier.clickable{ }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Mini-BESTest:", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(text = " Balance Evaluation Systems Test", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Normal)
                }
            }
        }
        minibestquestions.zip(instructions).zip(optionsList).forEachIndexed { index, (questionAndInstruction, options) ->
            val (question, instruction) = questionAndInstruction
            sections.find { it.first == index }?.let { (_, titleAndScore) ->
                val (title, maxScore) = titleAndScore
                val sectionIndex = sections.indexOfFirst { it.first == index }
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
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
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
        item{
            Spacer(modifier = Modifier.height(6.dp))
        }
        item{
            Text(
                text = "Total Score:  $totalScore/28",
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )
        }
    }
}
