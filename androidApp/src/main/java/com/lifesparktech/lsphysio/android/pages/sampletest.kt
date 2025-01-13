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
}

@Composable
fun question6(
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
                selected = selectedScore == 0,
                onClick = {
                    selectedScore = 0
                    onScoreCalculated(selectedScore)
                }
            )
            Text(text = "Severe (0): Falls, or cannot step.", fontSize = 14.sp)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = selectedScore == 1,
                onClick = {
                    selectedScore = 1
                    onScoreCalculated(selectedScore)
                }
            )
            Text(text = "Moderate (1): Several steps to recover equilibrium.", fontSize = 14.sp)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = selectedScore == 2,
                onClick = {
                    selectedScore = 2
                    onScoreCalculated(selectedScore)
                }
            )
            Text(text = "Normal (2): Recovers independently with 1 step (crossover or lateral OK).", fontSize = 14.sp)
        }
    }
}