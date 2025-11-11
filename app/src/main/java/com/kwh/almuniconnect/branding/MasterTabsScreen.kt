package com.kwh.almuniconnect.branding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kwh.almuniconnect.api.MasterItem
import com.kwh.almuniconnect.api.MasterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MasterTabsScreen(
    viewModel: MasterViewModel = viewModel(),
    onItemClick: (MasterItem) -> Unit = {}
) {
    val tabs = listOf("Country", "Branch", "Batch", "Roles", "Course")
    var selectedIndex by remember { mutableStateOf(0) }
    val loading by viewModel.loading.collectAsState()
    val countries by viewModel.countries.collectAsState()
    val branches by viewModel.branches.collectAsState()
    val batches by viewModel.batches.collectAsState()
    val roles by viewModel.roles.collectAsState()
    val courses by viewModel.courses.collectAsState()

    // auto-load first tab once
    LaunchedEffect(Unit) {
        viewModel.loadCountries()
    }

    Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        TabRow(selectedTabIndex = selectedIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(selected = selectedIndex == index, onClick = {
                    selectedIndex = index
                    // load corresponding data
                    when (index) {
                        0 -> if (countries.isEmpty()) viewModel.loadCountries()
                        1 -> if (branches.isEmpty()) viewModel.loadBranches()
                        2 -> if (batches.isEmpty())  viewModel.loadBatches()
                        3 -> if (roles.isEmpty())    viewModel.loadRoles()
                        4 -> if (courses.isEmpty())  viewModel.loadCourses()
                    }
                }, text = { Text(title) })
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Simple toolbar with search & refresh
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedTextField(
                value = "",
                onValueChange = { /* implement search binding if needed */ },
                placeholder = { Text("Search...") },
                modifier = Modifier.weight(1f).padding(end = 8.dp),
                singleLine = true
            )
            Button(onClick = {
                // refresh selected tab
                when (selectedIndex) {
                    0 -> viewModel.loadCountries()
                    1 -> viewModel.loadBranches()
                    2 -> viewModel.loadBatches()
                    3 -> viewModel.loadRoles()
                    4 -> viewModel.loadCourses()
                }
            }) {
                Text("Refresh")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Column
        }

        val list: List<MasterItem> = when (selectedIndex) {
            0 -> countries
            1 -> branches
            2 -> batches
            3 -> roles
            4 -> courses
            else -> emptyList()
        }

        MasterList(list = list, onItemClick = onItemClick)
    }
}

@Composable
fun MasterList(list: List<MasterItem>, onItemClick: (MasterItem) -> Unit) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxSize()) {
        items(list) { item ->
            Card(modifier = Modifier
                .fillMaxWidth()
                .clickable { onItemClick(item) }
            ) {
                Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(item.name, style = MaterialTheme.typography.bodyLarge)
                        item.shortName?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
                    }
                    Text("#${item.id}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
