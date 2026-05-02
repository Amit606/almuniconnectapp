package com.kwh.almuniconnect.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class JobProfileViewModel : ViewModel() {

    // Page 1 States
    var fullName by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var phone by mutableStateOf("")
        private set
    var location by mutableStateOf("")
        private set

    // Page 2 States
    var currentJobTitle by mutableStateOf("")
        private set
    var yearsOfExp by mutableStateOf("")
        private set
    var skillsList by mutableStateOf(mutableListOf<String>())
        private set
    var newSkill by mutableStateOf("")
        private set
    var selectedJobType by mutableStateOf("")
        private set

    // Validation
    val isEmailValid: Boolean
        get() = email.isBlank() || android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    val isPage1Valid: Boolean
        get() = fullName.isNotBlank() && 
                email.isNotBlank() && 
                isEmailValid && 
                phone.isNotBlank() && 
                location.isNotBlank()

    val isPage2Valid: Boolean
        get() = currentJobTitle.isNotBlank() && 
                yearsOfExp.isNotBlank() && 
                skillsList.isNotEmpty() && 
                selectedJobType.isNotBlank()

    fun updateFullName(name: String) { fullName = name }
    fun updateEmail(newEmail: String) { email = newEmail }
    fun updatePhone(newPhone: String) { phone = newPhone }
    fun updateLocation(newLocation: String) { location = newLocation }

    fun updateJobTitle(title: String) { currentJobTitle = title }
    fun updateYearsOfExp(years: String) { yearsOfExp = years }
    fun updateNewSkill(skill: String) { newSkill = skill }

    fun addSkill() {
        if (newSkill.isNotBlank()) {
            skillsList = (skillsList + newSkill.trim()).toMutableList()
            newSkill = ""
        }
    }

    fun removeSkill(skill: String) {
        skillsList = skillsList.filter { it != skill }.toMutableList()
    }

    fun selectJobType(type: String) {
        selectedJobType = type
    }

    // Final Save Action
    fun saveAndApply() {
        viewModelScope.launch {
            // TODO: Save all data to Firebase or your backend
            val profileData = mapOf(
                "fullName" to fullName,
                "email" to email,
                "phone" to phone,
                "location" to location,
                "jobTitle" to currentJobTitle,
                "experience" to yearsOfExp,
                "jobType" to selectedJobType,
                "skills" to skillsList,
                "timestamp" to System.currentTimeMillis()
            )

            // You can call your repository here
            // jobProfileRepository.saveProfile(profileData)

            println("Profile Saved: $profileData") // For testing
        }
    }
}