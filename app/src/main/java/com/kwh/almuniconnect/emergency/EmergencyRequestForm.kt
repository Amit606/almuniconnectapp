package com.kwh.almuniconnect.emergency

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun EmergencyRequestForm(
    viewModel: EmergencyViewModel = viewModel(),
    onSubmit: () -> Unit
) {
    // same UI
    Button(
        onClick = {
            viewModel.submitEmergency(
                type = "Medical",
                description = "Emergency",
                amount = "100000"
            )
            onSubmit()
        }
    ) {
        Text("Submit Emergency Request")
    }
}
