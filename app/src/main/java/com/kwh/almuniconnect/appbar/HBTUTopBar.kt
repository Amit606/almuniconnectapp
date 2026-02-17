package com.kwh.almuniconnect.appbar

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HBTUTopBar(
    title: String,
    navController: NavController,
    showBack: Boolean = true,
    onFilterClick: (() -> Unit)? = null,
    rightAction: (@Composable () -> Unit)? = null   // 👈 NEW (optional)
) {
    var isNavigating by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(text = title, fontWeight = FontWeight.SemiBold)
        },
        navigationIcon = {
            if (showBack && navController.previousBackStackEntry != null) {
                IconButton(
                    enabled = !isNavigating,
                    onClick = {
                        isNavigating = true
                        navController.navigateUp()
                    }
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            // 🔹 Existing filter (unchanged)
            if (onFilterClick != null) {
                IconButton(onClick = onFilterClick) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filter"
                    )
                }
            }

            // 🔹 New optional right action (Add Job, etc.)
            rightAction?.invoke()
        }
    )
}


