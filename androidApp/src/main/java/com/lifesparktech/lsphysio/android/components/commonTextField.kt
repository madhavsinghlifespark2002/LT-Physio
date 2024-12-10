package com.lifesparktech.lsphysio.android.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isRead: Boolean = false,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isNumeric: Boolean = false,
    maxLength: Int = Int.MAX_VALUE // Optional: Pass a maxLength argument if needed
) {
    Column(modifier = modifier) {
        Text(text = label, style = TextStyle(fontSize = 16.sp))
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .border(width = 2.dp, color = Color(0xFFf9f9f8), shape = RoundedCornerShape(8.dp)) // Add border
        ) {
            TextField(

                value = value,
                readOnly = isRead,
                onValueChange = { newValue ->
                    if (isNumeric) {
                        if (newValue.all { it.isDigit() } && newValue.length <= maxLength) {
                            onValueChange(newValue)
                        }
                    } else {
                        onValueChange(newValue)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = keyboardOptions,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                maxLines = 1
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTextFieldgrey(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isRead: Boolean = false,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isNumeric: Boolean = false,
    maxLength: Int = Int.MAX_VALUE // Optional: Pass a maxLength argument if needed
) {
    Column(modifier = modifier) {
        Text(text = label, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold))
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                onValueChange(newValue) },
            //label = { Text("Your Text") },
            placeholder = { Text("Enter ${label}") },
            keyboardOptions = keyboardOptions,
            modifier = Modifier.fillMaxWidth(),
            colors =  TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFf2f4f5),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}
