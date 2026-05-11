package com.mindmatrix.surakshasetu.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    name: String,
    age: String,
    bloodGroup: String,
    onSave: (String, String, String) -> Unit,
    onBack: () -> Unit
) {
    var updatedName by remember { mutableStateOf(name) }
    var updatedAge by remember { mutableStateOf(age) }
    var updatedBloodGroup by remember { mutableStateOf(bloodGroup) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Personal Profile") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = updatedName,
                onValueChange = { updatedName = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = updatedAge,
                onValueChange = { updatedAge = it },
                label = { Text("Age") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = updatedBloodGroup,
                onValueChange = { updatedBloodGroup = it },
                label = { Text("Blood Group") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onSave(updatedName, updatedAge, updatedBloodGroup) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }
        }
    }
}
