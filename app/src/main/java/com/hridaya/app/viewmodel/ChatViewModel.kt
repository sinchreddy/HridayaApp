package com.hridaya.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hridaya.app.data.model.Message
import com.hridaya.app.data.model.Mood
import com.hridaya.app.data.repository.ChatRepository
import com.hridaya.app.data.repository.MoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val moodRepository: MoodRepository
) : ViewModel() {

    private val _currentSessionId = MutableStateFlow("session_${UUID.randomUUID()}")
    val currentSessionId = _currentSessionId.asStateFlow()

    val messages: StateFlow<List<Message>> = _currentSessionId
        .flatMapLatest { sessionId ->
            chatRepository.getMessages(sessionId)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allSessions: StateFlow<List<String>> = chatRepository.getAllSessionIds()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isTyping = MutableStateFlow(false)
    val isTyping = _isTyping.asStateFlow()

    private val _inputText = MutableStateFlow("")
    val inputText = _inputText.asStateFlow()

    private val _currentMood = MutableStateFlow<Mood?>(null)
    val currentMood = _currentMood.asStateFlow()

    init {
        viewModelScope.launch {
            moodRepository.getLatestMood()?.let {
                _currentMood.value = it.mood
            }
        }
    }

    fun onInputChange(text: String) {
        _inputText.value = text
    }

    fun sendMessage() {
        val text = _inputText.value.trim()
        if (text.isBlank()) return

        _inputText.value = ""
        _isTyping.value = true

        viewModelScope.launch {
            try {
                chatRepository.sendMessage(text, _currentSessionId.value)
            } finally {
                _isTyping.value = false
            }
        }
    }

    fun startNewSession() {
        _currentSessionId.value = "session_${UUID.randomUUID()}"
    }

    fun loadSession(sessionId: String) {
        _currentSessionId.value = sessionId
    }

    fun deleteCurrentSession() {
        viewModelScope.launch {
            chatRepository.deleteSession(_currentSessionId.value)
            startNewSession()
        }
    }
}
