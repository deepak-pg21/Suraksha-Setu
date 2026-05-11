package com.mindmatrix.surakshasetu.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    var step by remember { mutableIntStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = when (step) {
                1 -> "Welcome to Suraksha Setu"
                2 -> "Add Your Safe Circle"
                else -> "Shake to Trigger SOS"
            },
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = when (step) {
                1 -> "Your hyper-local safety network connecting you to neighbors and volunteers within 500m."
                2 -> "Add up to 5 trusted family members or neighbors who will receive your location in an emergency."
                else -> "In danger? Just shake your phone. We'll start recording audio and alert everyone immediately."
            },
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = {
                if (step < 3) step++ else onFinished()
            },
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text(if (step < 3) "Next" else "Get Started")
        }
    }
}
