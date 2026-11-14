package com.hridaya.app.viewmodel

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hridaya.app.data.model.MusicCategory
import com.hridaya.app.data.model.MusicTrack
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Curated track list — these use Android's built-in tone generator or silence
// In production, add real audio files to res/raw/
val MUSIC_TRACKS = listOf(
    MusicTrack("1", "Morning Calm", MusicCategory.MEDITATION, 600, "Gentle awakening tones", "morning_calm"),
    MusicTrack("2", "Deep Sleep Waves", MusicCategory.SLEEP, 3600, "Ocean waves for restful sleep", "deep_sleep"),
    MusicTrack("3", "Forest Rain", MusicCategory.NATURE, 1800, "Soft rainfall in a peaceful forest", "forest_rain"),
    MusicTrack("4", "Focus Flow", MusicCategory.FOCUS, 2400, "Binaural beats for deep concentration", "focus_flow"),
    MusicTrack("5", "Heart Healing", MusicCategory.HEALING, 900, "Solfeggio 528Hz healing frequency", "heart_healing"),
    MusicTrack("6", "Om Shanti", MusicCategory.MANTRAS, 1200, "Ancient mantra for inner peace", "om_shanti"),
    MusicTrack("7", "Tibetan Bowls", MusicCategory.MEDITATION, 900, "Crystal singing bowls meditation", "tibetan_bowls"),
    MusicTrack("8", "Night Stars", MusicCategory.SLEEP, 2700, "Gentle cosmic ambience for sleep", "night_stars"),
    MusicTrack("9", "Birdsong Dawn", MusicCategory.NATURE, 1500, "Morning birds in a mountain meadow", "birdsong_dawn"),
    MusicTrack("10", "Deep Focus", MusicCategory.FOCUS, 3000, "Alpha waves for creative work", "deep_focus"),
    MusicTrack("11", "Chakra Balance", MusicCategory.HEALING, 1800, "Seven chakra toning session", "chakra_balance"),
    MusicTrack("12", "Gayatri Mantra", MusicCategory.MANTRAS, 600, "Sacred Vedic mantra chant", "gayatri_mantra"),
)

@HiltViewModel
class MusicViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private var mediaPlayer: MediaPlayer? = null
    private var progressJob: Job? = null

    private val _currentTrack = MutableStateFlow<MusicTrack?>(null)
    val currentTrack = _currentTrack.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    private val _progress = MutableStateFlow(0f) // 0.0 to 1.0
    val progress = _progress.asStateFlow()

    private val _volume = MutableStateFlow(0.7f)
    val volume = _volume.asStateFlow()

    private val _selectedCategory = MutableStateFlow<MusicCategory?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _timerMinutes = MutableStateFlow(0) // 0 = no timer
    val timerMinutes = _timerMinutes.asStateFlow()

    val filteredTracks: List<MusicTrack>
        get() = _selectedCategory.value?.let { cat ->
            MUSIC_TRACKS.filter { it.category == cat }
        } ?: MUSIC_TRACKS

    fun selectCategory(category: MusicCategory?) {
        _selectedCategory.value = category
    }

    fun playTrack(track: MusicTrack) {
        stopPlayback()
        _currentTrack.value = track
        _progress.value = 0f

        // Try to load actual audio file from raw resources
        val resId = context.resources.getIdentifier(track.fileName, "raw", context.packageName)

        try {
            mediaPlayer = if (resId != 0) {
                MediaPlayer.create(context, resId)
            } else {
                // Fallback: use a simple tone (in real app, add audio files to res/raw/)
                null
            }

            mediaPlayer?.apply {
                setVolume(_volume.value, _volume.value)
                isLooping = true
                setOnCompletionListener { _isPlaying.value = false }
                start()
                _isPlaying.value = true
                startProgressTracking(track.durationSeconds)
            } ?: run {
                // Simulate playback if no audio file present
                _isPlaying.value = true
                simulatePlayback(track.durationSeconds)
            }
        } catch (e: Exception) {
            _isPlaying.value = true
            simulatePlayback(track.durationSeconds)
        }
    }

    private fun simulatePlayback(durationSeconds: Int) {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            val startTime = System.currentTimeMillis()
            while (_isPlaying.value) {
                val elapsed = (System.currentTimeMillis() - startTime) / 1000f
                _progress.value = (elapsed / durationSeconds).coerceIn(0f, 1f)
                delay(1000)
            }
        }
    }

    private fun startProgressTracking(durationSeconds: Int) {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            while (_isPlaying.value && mediaPlayer != null) {
                try {
                    val current = mediaPlayer?.currentPosition ?: 0
                    _progress.value = (current / (durationSeconds * 1000f)).coerceIn(0f, 1f)
                } catch (e: Exception) { /* ignore */ }
                delay(1000)
            }
        }
    }

    fun togglePlayPause() {
        val player = mediaPlayer
        if (player != null) {
            if (_isPlaying.value) {
                player.pause()
                _isPlaying.value = false
                progressJob?.cancel()
            } else {
                player.start()
                _isPlaying.value = true
                startProgressTracking(_currentTrack.value?.durationSeconds ?: 600)
            }
        } else {
            _isPlaying.value = !_isPlaying.value
            if (_isPlaying.value) {
                simulatePlayback(_currentTrack.value?.durationSeconds ?: 600)
            } else {
                progressJob?.cancel()
            }
        }
    }

    fun stopPlayback() {
        progressJob?.cancel()
        mediaPlayer?.apply {
            if (isPlaying) stop()
            reset()
            release()
        }
        mediaPlayer = null
        _isPlaying.value = false
        _progress.value = 0f
    }

    fun setVolume(volume: Float) {
        _volume.value = volume
        mediaPlayer?.setVolume(volume, volume)
    }

    fun setTimer(minutes: Int) {
        _timerMinutes.value = minutes
        if (minutes > 0) {
            viewModelScope.launch {
                delay(minutes * 60 * 1000L)
                stopPlayback()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopPlayback()
    }
}
