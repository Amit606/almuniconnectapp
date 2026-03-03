package com.kwh.almuniconnect.tallent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TalentViewModel : ViewModel() {

    private val repository = TalentRepository()

    private val _talents = MutableStateFlow<List<Talent>>(emptyList())
    val talents: StateFlow<List<Talent>> = _talents

    init {
        observeTalents()
    }

    private fun observeTalents() {
        repository.observeApprovedTalents {
            _talents.value = it
        }
    }
    fun getTalentById(talentId: String?): Talent? {
        return _talents.value.find { it.id == talentId }
    }
    fun addTalent(
        name: String,
        branch: String,
        year: Int,
        skill: String,
        photo:String,
        videoLink: String,
        description: String,
        userId: String,
        email: String,

    ) {
        viewModelScope.launch {

            val talent = Talent(
                name = name,
                branch = branch,
                year = year,
                skill = skill,
                videoLink = videoLink,
                photo=photo,
                description= description,
                status = TalentStatus.PENDING.name,
                userId = userId,
                userEmail=email,
                // 🔥 Auto Pending
            )

            repository.addTalent(talent)
        }
    }

    fun likeTalent(talentId: String) {
        viewModelScope.launch {
            repository.likeTalent(talentId)
        }
    }
}