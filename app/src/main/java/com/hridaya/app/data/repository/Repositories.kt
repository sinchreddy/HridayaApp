package com.hridaya.app.data.repository

import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.hridaya.app.data.db.JournalDao
import com.hridaya.app.data.db.MessageDao
import com.hridaya.app.data.db.MoodDao
import com.hridaya.app.data.model.JournalEntry
import com.hridaya.app.data.model.Message
import com.hridaya.app.data.model.Mood
import com.hridaya.app.data.model.MoodEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val messageDao: MessageDao,
    private val generativeModel: GenerativeModel
) {
    fun getMessages(sessionId: String): Flow<List<Message>> =
        messageDao.getMessagesForSession(sessionId)

    fun getAllSessionIds(): Flow<List<String>> = messageDao.getAllSessionIds()

    suspend fun sendMessage(userText: String, sessionId: String): String {
        // Save user message
        messageDao.insertMessage(
            Message(content = userText, isUser = true, sessionId = sessionId)
        )

        return try {
            // Build conversation history for context
            val history = messageDao.getMessagesForSession(sessionId).first()
                .takeLast(10) // last 10 messages for context
                .dropLast(1)  // exclude the message we just added

            val chat = generativeModel.startChat(
                history = history.map { msg ->
                    content(if (msg.isUser) "user" else "model") { text(msg.content) }
                }
            )

            val response = chat.sendMessage(userText)
            val responseText = response.text ?: "I'm here with you. 💙"

            // Save AI response
            messageDao.insertMessage(
                Message(content = responseText, isUser = false, sessionId = sessionId)
            )

            responseText
        } catch (e: Exception) {
            Log.e("ChatRepository", "Error getting AI response", e)
            val fallback = "I'm having trouble connecting right now, but I'm here. Take a deep breath — you're not alone. 💙"
            messageDao.insertMessage(
                Message(content = fallback, isUser = false, sessionId = sessionId)
            )
            fallback
        }
    }

    suspend fun deleteSession(sessionId: String) = messageDao.deleteSession(sessionId)
}

@Singleton
class MoodRepository @Inject constructor(private val moodDao: MoodDao) {
    fun getAllMoods(): Flow<List<MoodEntry>> = moodDao.getAllMoods()
    fun getMoodsSince(since: Long): Flow<List<MoodEntry>> = moodDao.getMoodsSince(since)
    suspend fun getLatestMood(): MoodEntry? = moodDao.getLatestMood()
    suspend fun logMood(mood: Mood, note: String = "", energy: Int = 5): Long =
        moodDao.insertMood(MoodEntry(mood = mood, note = note, energyLevel = energy))
    suspend fun deleteMood(entry: MoodEntry) = moodDao.deleteMood(entry)
}

@Singleton
class JournalRepository @Inject constructor(private val journalDao: JournalDao) {
    fun getAllEntries(): Flow<List<JournalEntry>> = journalDao.getAllEntries()
    fun getFavoriteEntries(): Flow<List<JournalEntry>> = journalDao.getFavoriteEntries()
    suspend fun saveEntry(entry: JournalEntry): Long = journalDao.insertEntry(entry)
    suspend fun updateEntry(entry: JournalEntry) = journalDao.updateEntry(entry)
    suspend fun deleteEntry(entry: JournalEntry) = journalDao.deleteEntry(entry)
    suspend fun getEntryById(id: Long): JournalEntry? = journalDao.getEntryById(id)
}
