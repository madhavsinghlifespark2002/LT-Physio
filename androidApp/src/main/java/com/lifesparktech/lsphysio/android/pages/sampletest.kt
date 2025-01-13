import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun sampleTest() {
        var leftScore by remember { mutableStateOf(0) }
        var rightScore by remember { mutableStateOf(0) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Question 1: STAND ON ONE LEG",
                fontSize = 20.sp,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Instruction: Look straight ahead. Keep your hands on your hips. Lift your leg off of the ground behind you without touching or resting your raised leg upon your other standing leg. Stay standing on one leg as long as you can. Look straight ahead. Lift now.",
                fontSize = 14.sp
            )

            Divider(modifier = Modifier.fillMaxWidth())

            TimeInputSection("Left") {
                leftScore = it
            }

            TimeInputSection("Right") {
                rightScore = it
            }

            Divider(modifier = Modifier.fillMaxWidth())

            Text(text = "Left Score: $leftScore", fontSize = 16.sp)
            Text(text = "Right Score: $rightScore", fontSize = 16.sp)
        }
    }
    @Composable
    fun TimeInputSection(
        side: String,
        onScoreCalculated: (Int) -> Unit
    ) {
        var selectedScore by remember { mutableStateOf(0) }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "$side: ", fontSize = 16.sp, modifier = Modifier.padding(start = 12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedScore == 2,
                    onClick = {
                        selectedScore = 2
                        onScoreCalculated(selectedScore)
                    }
                )
                Text(text = "(2) Normal: 20 s.", fontSize = 14.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedScore == 1,
                    onClick = {
                        selectedScore = 1
                        onScoreCalculated(selectedScore)
                    }
                )
                Text(text = "(1) Moderate: < 20 s.", fontSize = 14.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedScore == 0,
                    onClick = {
                        selectedScore = 0
                        onScoreCalculated(selectedScore)
                    }
                )
                Text(text = "(0) Severe: Unable.", fontSize = 14.sp)
            }
        }
    }