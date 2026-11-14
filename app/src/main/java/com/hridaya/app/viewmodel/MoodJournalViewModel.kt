package com.hridaya.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hridaya.app.data.model.JournalEntry
import com.hridaya.app.data.model.Mood
import com.hridaya.app.data.model.MoodEntry
import com.hridaya.app.data.repository.JournalRepository
import com.hridaya.app.data.repository.MoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoodViewModel @Inject constructor(
    private val moodRepository: MoodRepository
) : ViewModel() {

    val allMoods: StateFlow<List<MoodEntry>> = moodRepository.getAllMoods()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Last 7 days
    val recentMoods: StateFlow<List<MoodEntry>> = moodRepository.getMoodsSince(
        System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000L
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedMood = MutableStateFlow<Mood?>(null)
    val selectedMood = _selectedMood.asStateFlow()

    private val _moodNote = MutableStateFlow("")
    val moodNote = _moodNote.asStateFlow()

    private val _energyLevel = MutableStateFlow(5)
    val energyLevel = _energyLevel.asStateFlow()

    private val _savedSuccess = MutableStateFlow(false)
    val savedSuccess = _savedSuccess.asStateFlow()

    fun selectMood(mood: Mood) { _selectedMood.value = mood }
    fun onNoteChange(note: String) { _moodNote.value = note }
    fun onEnergyChange(level: Int) { _energyLevel.value = level }

    fun saveMood() {
        val mood = _selectedMood.value ?: return
        viewModelScope.launch {
            moodRepository.logMood(mood, _moodNote.value, _energyLevel.value)
            _savedSuccess.value = true
            _selectedMood.value = null
            _moodNote.value = ""
            _energyLevel.value = 5
        }
    }

    fun resetSavedState() { _savedSuccess.value = false }

    fun deleteMood(entry: MoodEntry) {
        viewModelScope.launch { moodRepository.deleteMood(entry) }
    }
}

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val journalRepository: JournalRepository
) : ViewModel() {

    val allEntries: StateFlow<List<JournalEntry>> = journalRepository.getAllEntries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteEntries: StateFlow<List<JournalEntry>> = journalRepository.getFavoriteEntries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _editingEntry = MutableStateFlow<JournalEntry?>(null)
    val editingEntry = _editingEntry.asStateFlow()

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _content = MutableStateFlow("")
    val content = _content.asStateFlow()

    private val _selectedMood = MutableStateFlow<Mood?>(null)
    val selectedMood = _selectedMood.asStateFlow()

    fun onTitleChange(t: String) { _title.value = t }
    fun onContentChange(c: String) { _content.value = c }
    fun onMoodSelect(m: Mood?) { _selectedMood.value = m }

    fun loadEntry(entry: JournalEntry) {
        _editingEntry.value = entry
        _title.value = entry.title
        _content.value = entry.content
        _selectedMood.value = entry.mood
    }

    fun clearEditor() {
        _editingEntry.value = null
        _title.value = ""
        _content.value = ""
        _selectedMood.value = null
    }

    fun saveEntry() {
        val existing = _editingEntry.value
        viewModelScope.launch {
            if (existing != null) {
                journalRepository.updateEntry(
                    existing.copy(
                        title = _title.value,
                        content = _content.value,
                        mood = _selectedMood.value
                    )
                )
            } else {
                journalRepository.saveEntry(
                    JournalEntry(
                        title = _title.value,
                        content = _content.value,
                        mood = _selectedMood.value
                    )
                )
            }
            clearEditor()
        }
    }

    fun toggleFavorite(entry: JournalEntry) {
        viewModelScope.launch {
            journalRepository.updateEntry(entry.copy(isFavorite = !entry.isFavorite))
        }
    }

    fun deleteEntry(entry: JournalEntry) {
        viewModelScope.launch { journalRepository.deleteEntry(entry) }
    }
}
