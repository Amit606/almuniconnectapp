package com.kwh.almuniconnect.login

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import java.util.*

@Composable
fun DatePickerField(label: String, selectedDate: String, onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    OutlinedTextField(
        value = selectedDate,
        onValueChange = {},
        label = { Text(label, color = Color.White) },
        trailingIcon = { Icon(Icons.Default.DateRange, tint = Color.White, contentDescription = null) },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .clickable {
                DatePickerDialog(
                    context,
                    { _, year, month, day ->
                        val date = "%02d/%02d/%04d".format(day, month + 1, year)
                        onDateSelected(date)
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            },
        textStyle = LocalTextStyle.current.copy(color = Color.White),

                enabled = false
    )
}
