package com.mindmatrix.surakshasetu

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mindmatrix.surakshasetu.data.AppDatabase
import com.mindmatrix.surakshasetu.data.Contact
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.mindmatrix.surakshasetu.ai.GeminiHelper
import com.google.firebase.messaging.FirebaseMessaging
import android.media.RingtoneManager
import android.media.Ringtone

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val contactDao = database.contactDao()
    private val sharedPrefs = application.getSharedPreferences("suraksha_prefs", Context.MODE_PRIVATE)

    val contacts: StateFlow<List<Contact>> = contactDao.getAllContacts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isVolunteer = mutableStateOf(sharedPrefs.getBoolean("is_volunteer", false))
    val isVolunteer: State<Boolean> = _isVolunteer

    private val _isFirstTime = mutableStateOf(sharedPrefs.getBoolean("is_first_time", true))
    val isFirstTime: State<Boolean> = _isFirstTime

    private val _profileName = mutableStateOf(sharedPrefs.getString("user_name", "") ?: "")
    val profileName: State<String> = _profileName

    private val _profileAge = mutableStateOf(sharedPrefs.getString("user_age", "") ?: "")
    val profileAge: State<String> = _profileAge

    private val _profileBloodGroup = mutableStateOf(sharedPrefs.getString("user_blood", "") ?: "")
    val profileBloodGroup: State<String> = _profileBloodGroup

    private val _aiSafetyTip = mutableStateOf("Loading safety tip...")
    val aiSafetyTip: State<String> = _aiSafetyTip

    init {
        fetchAISafetyTip()
    }

    fun completeOnboarding() {
        _isFirstTime.value = false
        sharedPrefs.edit { putBoolean("is_first_time", false) }
    }

    fun updateProfile(name: String, age: String, bloodGroup: String) {
        _profileName.value = name
        _profileAge.value = age
        _profileBloodGroup.value = bloodGroup
        sharedPrefs.edit {
            putString("user_name", name)
            putString("user_age", age)
            putString("user_blood", bloodGroup)
        }
    }

    fun fetchAISafetyTip() {
        viewModelScope.launch {
            try {
                val response = GeminiHelper.model.generateContent("Give a short safety tip for someone walking alone.")
                _aiSafetyTip.value = response.text ?: "Stay alert and keep your phone handy."
            } catch (_: Exception) {
                _aiSafetyTip.value = "Stay aware of your surroundings."
            }
        }
    }

    fun addContact(name: String, phoneNumber: String) {
        viewModelScope.launch {
            if (contactDao.getCount() < 5) {
                contactDao.insertContact(Contact(name = name, phoneNumber = phoneNumber))
            }
        }
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            contactDao.deleteContact(contact)
        }
    }

    fun toggleVolunteer(value: Boolean) {
        _isVolunteer.value = value
        sharedPrefs.edit {
            putBoolean("is_volunteer", value)
        }
        
        if (value) {
            FirebaseMessaging.getInstance().subscribeToTopic("volunteers")
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("volunteers")
        }
    }

    fun triggerSOS(context: Context) {
        val intent = Intent(
            context,
            com.mindmatrix.surakshasetu.service.EmergencyShakeService::class.java
        ).apply {
            action = "ACTION_TRIGGER_SOS"
        }
        context.startForegroundService(intent)
        android.widget.Toast.makeText(context, "SOS Triggered!", android.widget.Toast.LENGTH_SHORT).show()
    }
}
