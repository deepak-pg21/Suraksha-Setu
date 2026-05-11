package com.mindmatrix.surakshasetu.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mindmatrix.surakshasetu.data.Contact

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SafeCircleScreen(
    contacts: List<Contact>,
    onAddContact: (String, String) -> Unit,
    onDeleteContact: (Contact) -> Unit,
    onBack: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var newPhone by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Safe Circle (Max 5)") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        // Icon would be back, but keeping it simple
                        Text("<-")
                    }
                }
            )
        },
        floatingActionButton = {
            if (contacts.size < 5) {
                FloatingActionButton(onClick = { showAddDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(contacts) { contact ->
                ListItem(
                    headlineContent = { Text(contact.name) },
                    supportingContent = { Text(contact.phoneNumber) },
                    trailingContent = {
                        IconButton(onClick = { onDeleteContact(contact) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                )
                HorizontalDivider()
            }
        }

        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("Add Trusted Neighbor") },
                text = {
                    Column {
                        TextField(value = newName, onValueChange = { newName = it }, label = { Text("Name") })
                        Spacer(Modifier.height(8.dp))
                        TextField(value = newPhone, onValueChange = { newPhone = it }, label = { Text("Phone Number") })
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        if (newName.isNotBlank() && newPhone.isNotBlank()) {
                            onAddContact(newName, newPhone)
                            newName = ""
                            newPhone = ""
                            showAddDialog = false
                        }
                    }) {
                        Text("Add")
                    }
                }
            )
        }
    }
}
