package com.hridaya.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

// Chat message model
@Entity(tableName = "messages")
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val content: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val sessionId: String = "default",
    val emotion: String? = null  // detected emotion in user message
)

// Mood entry for tracking
@Entity(tableName = "mood_entries")
data class MoodEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val mood: Mood,
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val energyLevel: Int = 5  // 1-10
)

enum class Mood(val label: String, val emoji: String, val color: Long) {
    JOYFUL("Joyful", "😄", 0xFFFFB347),
    CALM("Calm", "😌", 0xFF7ECECA),
    GRATEFUL("Grateful", "🙏", 0xFFE07A90),
    ANXIOUS("Anxious", "😰", 0xFFB8A0E8),
    SAD("Sad", "😢", 0xFF6AABCF),
    ANGRY("Angry", "😤", 0xFFFF6B6B),
    CONFUSED("Confused", "😕", 0xFF98B8A0),
    HOPEFUL("Hopeful", "🌟", 0xFFE8D5A0)
}

// Music track model
data class MusicTrack(
    val id: String,
    val title: String,
    val category: MusicCategory,
    val durationSeconds: Int,
    val description: String,
    val fileName: String  // raw resource name
)

enum class MusicCategory(val label: String, val emoji: String) {
    MEDITATION("Meditation", "🧘"),
    SLEEP("Sleep", "🌙"),
    FOCUS("Focus", "🎯"),
    HEALING("Healing", "💚"),
    NATURE("Nature", "🌿"),
    MANTRAS("Mantras", "🕉️")
}

// Journal entry
@Entity(tableName = "journal_entries")
data class JournalEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val mood: Mood? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)

// Breathing exercise
data class BreathingPattern(
    val name: String,
    val inhaleSeconds: Int,
    val holdInSeconds: Int,
    val exhaleSeconds: Int,
    val holdOutSeconds: Int,
    val description: String,
    val emoji: String
)

val BREATHING_PATTERNS = listOf(
    BreathingPattern("Box Breathing", 4, 4, 4, 4, "Equal breathing for calm focus", "📦"),
    BreathingPattern("4-7-8 Relax", 4, 7, 8, 0, "Deep relaxation and sleep", "🌙"),
    BreathingPattern("Coherent", 5, 0, 5, 0, "Heart rate variability balance", "💚"),
    BreathingPattern("Energizing", 6, 0, 2, 0, "Boost energy and alertness", "⚡"),
    BreathingPattern("Nadi Shodhana", 4, 4, 8, 0, "Alternate nostril breathing", "🕉️")
)
