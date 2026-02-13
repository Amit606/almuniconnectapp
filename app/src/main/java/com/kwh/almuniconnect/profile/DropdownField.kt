package com.kwh.almuniconnect.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
data class DropdownOption(
    val id: Int,
    val label: String
)
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DropdownFieldBranch(
//    label: String,
//    selected: Branch?,
//    items: List<Branch>,
//    onSelect: (Branch) -> Unit
//) {
//
//    var expanded by remember { mutableStateOf(false) }
//
//    ExposedDropdownMenuBox(
//        expanded = expanded,
//        onExpandedChange = { expanded = !expanded }
//    ) {
//
//        OutlinedTextField(
//            value = selected?.name ?: "",
//            onValueChange = {},
//            readOnly = true,
//            label = { Text(label) },
//            trailingIcon = {
//                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
//            },
//            modifier = Modifier
//                .menuAnchor()
//                .fillMaxWidth(),
//            shape = RoundedCornerShape(12.dp)
//        )
//
//        ExposedDropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//        ) {
//            items.forEach { branch ->
//                DropdownMenuItem(
//                    text = { Text(branch.name) },
//                    onClick = {
//                        onSelect(branch)
//                        expanded = false
//                    }
//                )
//            }
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropdownFieldYear(
    label: String,
    selected: T?,
    items: List<T>,
    onSelect: (T) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {

        OutlinedTextField(
            value = selected?.toString() ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.LightGray
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {

            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.toString()) },
                    onClick = {
                        onSelect(item)
                        expanded = false
                    }
                )
            }
        }
    }
}


