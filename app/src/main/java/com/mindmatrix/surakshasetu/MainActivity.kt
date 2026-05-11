package com.mindmatrix.surakshasetu

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mindmatrix.surakshasetu.service.EmergencyShakeService
import com.mindmatrix.surakshasetu.ui.screens.*
import com.mindmatrix.surakshasetu.ui.theme.SurakshaSetuTheme
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    android.util.Log.d(
                        "FCM_TOKEN",
                        task.result
                    )
                }
            }

        setContent {
            SurakshaSetuTheme {
                val navController = rememberNavController()
                val isFirstTime by viewModel.isFirstTime
                
                NavHost(
                    navController = navController, 
                    startDestination = if (isFirstTime) "onboarding" else "permissions"
                ) {
                    composable("onboarding") {
                        OnboardingScreen(
                            onFinished = {
                                viewModel.completeOnboarding()
                                navController.navigate("permissions") {
                                    popUpTo("onboarding") { inclusive = true }
                                }
                            }
                        )
                    }

                    composable("permissions") {
                        RequestPermissionsScreen(
                            onPermissionsGranted = {
                                startEmergencyService()
                                navController.navigate("home") {
                                    popUpTo("permissions") { inclusive = true }
                                }
                            }
                        )
                    }
                    
                    composable("home") {
                        val aiTip by viewModel.aiSafetyTip
                        SOSHomeScreen(
                            onSOSClicked = { viewModel.triggerSOS(this@MainActivity) },
                            onNavigateToContacts = { navController.navigate("contacts") },
                            onNavigateToVolunteer = { navController.navigate("volunteer") },
                            onNavigateToProfile = { navController.navigate("profile") },
                            aiSafetyTip = aiTip
                        )
                    }
                    
                    composable("profile") {
                        ProfileScreen(
                            name = viewModel.profileName.value,
                            age = viewModel.profileAge.value,
                            bloodGroup = viewModel.profileBloodGroup.value,
                            onSave = { name, age, blood ->
                                viewModel.updateProfile(name, age, blood)
                                navController.popBackStack()
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }
                    
                    composable("contacts") {
                        val contacts by viewModel.contacts.collectAsState()
                        SafeCircleScreen(
                            contacts = contacts,
                            onAddContact = { name, phone -> viewModel.addContact(name, phone) },
                            onDeleteContact = { contact -> viewModel.deleteContact(contact) },
                            onBack = { navController.popBackStack() }
                        )
                    }
                    
                    composable("volunteer") {
                        val isVolunteer by viewModel.isVolunteer
                        VolunteerScreen(
                            isVolunteer = isVolunteer,
                            onToggleVolunteer = { viewModel.toggleVolunteer(it) },
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }

    private fun startEmergencyService() {
        val intent = Intent(this, EmergencyShakeService::class.java)
        startForegroundService(intent)
    }
}
